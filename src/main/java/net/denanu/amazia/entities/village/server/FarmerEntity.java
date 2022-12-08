package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.farming.AmaziaGoToFarmGoal;
import net.denanu.amazia.entities.village.server.goal.farming.HarvestCropsGoal;
import net.denanu.amazia.entities.village.server.goal.farming.HoeFarmLandGoal;
import net.denanu.amazia.entities.village.server.goal.farming.PlantCropsGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.village.AmaziaData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FarmerEntity extends AmaziaVillagerEntity implements IAnimatable  {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE, Items.AIR);
	public static final ImmutableSet<Item> CRAFTABLES = ImmutableSet.of(Items.WOODEN_HOE, Items.STICK);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.CARROT, 16, Items.POTATO, 16, Items.WHEAT_SEEDS, 16, Items.BEETROOT_SEEDS, 16);

	private final AnimationFactory factory = new AnimationFactory(this);

	public FarmerEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(47, new HarvestCropsGoal  (this, 47));
		this.goalSelector.add(48, new PlantCropsGoal    (this, 48));
		this.goalSelector.add(49, new HoeFarmLandGoal   (this, 49));
		this.goalSelector.add(50, new AmaziaGoToFarmGoal(this, 50));

		super.registerBaseGoals();
	}

	private <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.walk", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(final AnimationData data) {
		data.addAnimationController(new AnimationController<FarmerEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// FARMER SPECIFIC

	public int getHoe() {
		ItemStack itm;
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			itm = this.getInventory().getStack(idx);
			if (itm.getItem() instanceof HoeItem) {
				return idx;
			}
		}
		return -1;
	}

	private boolean hasHoe() {
		for (final ItemStack itm : this.getInventory().stacks) {
			if (itm.getItem() instanceof HoeItem) {
				return true;
			}
		}
		this.requestHoe();
		return false;
	}

	private void requestHoe() {
		if (!this.hasRequestedItems()) {
			this.requestItem(Items.NETHERITE_HOE);
			this.requestItem(Items.DIAMOND_HOE);
			this.requestItem(Items.IRON_HOE);
			this.requestItem(Items.GOLDEN_HOE);
			this.requestItem(Items.STONE_HOE);
			this.requestItem(Items.WOODEN_HOE);
		}
	}

	@Override
	public boolean canCraft () {
		return true;
	}

	@Override
	public boolean canHoe() {
		return this.hasHoe();
	}

	@Override
	public boolean canPlant() {
		final boolean can = this.getInventory().containsAny(AmaziaData.CLASSIC_PLANTABLES);
		if (!can && this.age % 100 == 0) {
			this.requestItem(JJUtils.getRandomSetElement(AmaziaData.CLASSIC_PLANTABLES));
		}
		return can;
	}

	@Override
	public boolean canHarvest() {
		return this.hasFreeSlot();
	}

	@Override
	public boolean canDepositItems() {
		return this.getEmptyInventorySlots() == 0 && this.getDepositableItems() != null;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(FarmerEntity.USABLE_ITEMS, FarmerEntity.REQUIRED_ITEMS);
	}

	private void plant(final ServerWorld world, final BlockPos pos) {
		BlockState blockState2;
		final SimpleInventory simpleInventory = this.getInventory();
		for (int i = 0; i < simpleInventory.size(); ++i) {
			final ItemStack itemStack = simpleInventory.getStack(i);
			boolean bl = false;
			if (!itemStack.isEmpty()) {
				if (itemStack.isOf(Items.WHEAT_SEEDS)) {
					blockState2 = Blocks.WHEAT.getDefaultState();
					world.setBlockState(pos, blockState2);
					world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(this, blockState2));
					bl = true;
				} else if (itemStack.isOf(Items.POTATO)) {
					blockState2 = Blocks.POTATOES.getDefaultState();
					world.setBlockState(pos, blockState2);
					world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(this, blockState2));
					bl = true;
				} else if (itemStack.isOf(Items.CARROT)) {
					blockState2 = Blocks.CARROTS.getDefaultState();
					world.setBlockState(pos, blockState2);
					world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(this, blockState2));
					bl = true;
				} else if (itemStack.isOf(Items.BEETROOT_SEEDS)) {
					blockState2 = Blocks.BEETROOTS.getDefaultState();
					world.setBlockState(pos, blockState2);
					world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(this, blockState2));
					bl = true;
				}
			}
			if (!bl) {
				continue;
			}
			ActivityFoodConsumerMap.plantCropUseFood(this);
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0f, 1.0f);
			itemStack.decrement(1);
			if (!itemStack.isEmpty()) {
				break;
			}
			simpleInventory.setStack(i, ItemStack.EMPTY);
			break;
		}
	}
	private void harvest(final ServerWorld world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof final CropBlock crop) {
			if (crop.isMature(state)) {
				world.breakBlock(pos, true);
				ActivityFoodConsumerMap.harvestCropUseFood(this);
			}
		}
		if (state.getBlock() instanceof final SugarCaneBlock crop) {
			world.breakBlock(pos, true);
			ActivityFoodConsumerMap.harvestCropUseFood(this);
		}
	}

	public void plant() {
		this.plant((ServerWorld)this.world, new BlockPos(this.getPos()).up());
	}

	public void harvest() {
		this.harvest((ServerWorld)this.world, new BlockPos(this.getPos()).up());
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return Amazia.FARMER_CRAFTS;
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.FARMER;
	}
}
