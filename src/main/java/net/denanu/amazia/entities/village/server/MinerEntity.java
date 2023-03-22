package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.goal.mineing.EnterMineGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.ExtendMineGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.FixBrokenMineSeroundingsGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.LightUpMine;
import net.denanu.amazia.entities.village.server.goal.mineing.MoveToEndOfMine;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MinerEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableSet<Item> CRAFTABLES = ImmutableSet.of(Items.WOODEN_PICKAXE, Items.TORCH, Items.STICK);
	public static final ImmutableSet<Item> MINE_REQUIRED = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.COBBLESTONE, 32, Items.TORCH, 32, Items.STICK, 32, Items.COAL, 32);
	public static final ImmutableMap<Item, Integer> MAX_PICKUPS = ImmutableMap.of(Items.COBBLESTONE, 64);
	private static final Vec3i ITEM_PICK_UP_RANGE_EXPANDER_WHILE_MINEING = new Vec3i(5, 5, 5);

	private MineStructure mine;
	private boolean inMine;
	private List<BlockPos> toMineLocations;

	private Optional<Integer> pickPos;

	private final AnimationFactory factory = new AnimationFactory(this);

	public MinerEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		this.toMineLocations = new ArrayList<>();
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("toMineLocations", NbtUtils.toNbt(this.toMineLocations));
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.toMineLocations = NbtUtils.toBlockPosList(nbt.getList("toMineLocations", NbtElement.INT_ARRAY_TYPE));
	}

	@Override
	protected void initGoals() {

		this.goalSelector.add(46, new FixBrokenMineSeroundingsGoal(this, 46));
		this.goalSelector.add(45, new LightUpMine(this, 45));
		this.goalSelector.add(48, new ExtendMineGoal(this, 48));
		this.goalSelector.add(49, new MoveToEndOfMine(this, 49));
		this.goalSelector.add(50, new EnterMineGoal(this, 50));

		super.registerBaseGoals();
	}

	private <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.generic.walk", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.generic.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(final AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public boolean canDepositItems() {
		return this.getEmptyInventorySlots() == 0 && this.getDepositableItems() != null;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(MinerEntity.USABLE_ITEMS, MinerEntity.REQUIRED_ITEMS);
	}

	@Override
	public boolean wantToKeepItemInSlot(final int idx) {
		final boolean keepPick = this.pickPos.isPresent() && this.pickPos.get() == idx;
		return keepPick || super.wantToKeepItemInSlot(idx);
	}

	// MINER SPECIFIC

	@Override
	public void onInventoryChanged(final Inventory inventory) {
		super.onInventoryChanged(inventory);
		@Nullable
		ToolMaterial bestMaterial = null;
		this.pickPos = Optional.empty();
		for (int idx=0; idx < inventory.size(); idx++) {
			if (inventory.getStack(idx).getItem() instanceof final PickaxeItem pick && (bestMaterial == null || bestMaterial.getMiningLevel() < pick.getMaterial().getMiningLevel())) {
				bestMaterial = pick.getMaterial();
				this.pickPos = Optional.of(idx);
			}
		}
	}

	@Override
	public void onDeath(final DamageSource damageSource) {
		super.onDeath(damageSource);
		this.leaveMine();
	}

	@Override
	protected void update() {
		super.update();
		if (this.isInMine() && (!this.mine.isIn(this.getBlockPos()) || !this.canMine())) {
			this.leaveMine();
		}
	}

	public void enterMine(final MineStructure mine) {
		if (mine != null && !mine.hasVillager()) {
			this.mine = mine;
			this.mine.registerVillager();
			this.inMine = true;
		}
	}

	public MineStructure setMine() {
		if (this.mine == null) {
			this.mine = this.village.getMineing().getSuggestedMine();
		}
		return this.mine;
	}

	public MineStructure getMine() {
		return this.mine;
	}

	public void leaveMine() {
		if (this.mine != null) {
			this.mine.releaseVillager();
			this.mine = null;
			this.inMine = false;
		}
	}

	public boolean isInMine() {
		return this.inMine && this.getMine() != null;
	}

	public void enterMine() {
		this.enterMine(this.getMine());
	}

	protected void addOreBlock(final BlockPos pos) {
		final Block block = this.getWorld().getBlockState(pos).getBlock();
		if ((block instanceof OreBlock || block instanceof RedstoneOreBlock) && !this.toMineLocations.contains(pos)) {
			this.toMineLocations.add(pos);
			HappynessMap.gainMinerFindOreHappyness(this);
		}
	}

	public BlockPos getNextOreBlock() {
		while (this.toMineLocations.size() > 0) {
			final BlockPos pos = this.toMineLocations.get(0);
			this.toMineLocations.remove(0);
			if (MineStructure.isSafe(this, pos)) {
				return pos;
			}
		}
		return null;
	}

	public boolean canMineOre() {
		return this.toMineLocations.size() > 0;
	}

	public void addSeroundingOreBlock(final BlockPos pos) {
		this.addOreBlock(pos.up());
		this.addOreBlock(pos.down());
		this.addOreBlock(pos.east());
		this.addOreBlock(pos.west());
		this.addOreBlock(pos.north());
		this.addOreBlock(pos.south());
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return Amazia.MINER_CRAFTS;
	}

	@Override
	protected Vec3i getItemPickUpRangeExpander() {
		if (this.isInMine()) {
			return MinerEntity.ITEM_PICK_UP_RANGE_EXPANDER_WHILE_MINEING;
		}
		return super.getItemPickUpRangeExpander();
	}

	@Override
	protected boolean shouldPickUp(final ItemStack stack) {
		if (MinerEntity.MAX_PICKUPS.containsKey(stack.getItem())) {
			return this.countItems(stack.getItem()) < MinerEntity.MAX_PICKUPS.get(stack.getItem())
					+ stack.getMaxCount();
		}
		return true;
	}

	@Override
	public boolean canCraft() {
		return this.hasFreeSlot();
	}

	public int getPick() {
		ItemStack itm;
		for (int idx = 0; idx < this.getInventory().size(); idx++) {
			itm = this.getInventory().getStack(idx);
			if (itm.getItem() instanceof PickaxeItem) {
				return idx;
			}
		}
		return -1;
	}

	private boolean hasPick() {
		for (final ItemStack itm : this.getInventory().stacks) {
			if (itm.getItem() instanceof PickaxeItem) {
				return true;
			}
		}
		this.requestPick();
		return false;
	}

	private void requestPick() {
		if (!this.hasRequestedItems()) {
			this.requestItem(Items.NETHERITE_PICKAXE);
			this.requestItem(Items.DIAMOND_PICKAXE);
			this.requestItem(Items.IRON_PICKAXE);
			this.requestItem(Items.GOLDEN_PICKAXE);
			this.requestItem(Items.STONE_PICKAXE);
			this.requestItem(Items.WOODEN_PICKAXE);
		}
	}

	@Override
	public boolean canMine() {
		for (final Item itm : MinerEntity.MINE_REQUIRED) {
			if (!this.hasItem(itm, 1)) {
				this.requestItem(itm);
				return false;
			}
		}
		return this.hasFreeSlot() && this.hasPick();
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.MINER;
	}
}
