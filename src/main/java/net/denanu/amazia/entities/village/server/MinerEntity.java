package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.mineing.EnterMineGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.ExtendMineGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.FixBrokenMineSeroundingsGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.LightUpMine;
import net.denanu.amazia.entities.village.server.goal.mineing.MoveToEndOfMine;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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

public class MinerEntity extends AmaziaVillagerEntity implements IAnimatable  {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE, Items.AIR);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.COBBLESTONE, 64);
	public static final ImmutableMap<Item, Integer> MAX_PICKUPS = ImmutableMap.of(Items.COBBLESTONE, 64);
	private static final Vec3i ITEM_PICK_UP_RANGE_EXPANDER_WHILE_MINEING = new Vec3i(5, 5, 5);
	
	private MineStructure mine;
	private boolean inMine;
	private List<BlockPos> toMineLocations;
	
	private AnimationFactory factory = new AnimationFactory(this);

	public MinerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
		this.toMineLocations = new ArrayList<BlockPos>();
	}
	
	public static DefaultAttributeContainer.Builder setAttributes() {
        return PassiveEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }
	
	@Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        JJUtils.writeNBT(nbt, this.toMineLocations, "amazia.toMineLocations");
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.toMineLocations = JJUtils.readNBT(nbt, "amazia.toMineLocations");
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
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.idle", true));
        return PlayState.CONTINUE;
    }

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<MinerEntity>(this, "controller", 0, this::predicate));
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
		return this.getDepositableItems(USABLE_ITEMS, REQUIRED_ITEMS);
	}
	
	// MINER SPECIFIC
	
	@Override 
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);
		this.leaveMine();
	}
	
	@Override
	protected void update() {
		super.update();
		if (this.isInMine()) {
			if (!this.mine.isIn(new BlockPos(this.getPos()))) {
				this.leaveMine();
			}
		}
	}
	
	public void enterMine(MineStructure mine) {
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
	
	protected void addOreBlock(BlockPos pos) {
		Block block = this.getWorld().getBlockState(pos).getBlock();
		if (block instanceof OreBlock && !this.toMineLocations.contains(pos)) {
			this.toMineLocations.add(pos);
		}
	}
	public BlockPos getNextOreBlock() {
		while (this.toMineLocations.size() > 0) {
			BlockPos pos = this.toMineLocations.get(0);
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

	public void addSeroundingOreBlock(BlockPos pos) {
		this.addOreBlock(pos.up());
		this.addOreBlock(pos.down());
		this.addOreBlock(pos.east());
		this.addOreBlock(pos.west());
		this.addOreBlock(pos.north());
		this.addOreBlock(pos.south());
	}
	
	@Override
	protected Vec3i getItemPickUpRangeExpander() {
		if (this.isInMine()) {
			return ITEM_PICK_UP_RANGE_EXPANDER_WHILE_MINEING;
		}
        return super.getItemPickUpRangeExpander();
    }
	
	@Override
	protected boolean shouldPickUp(ItemStack stack) {
		if (MAX_PICKUPS.containsKey(stack.getItem())) {
			int count = this.countItems(stack.getItem());
			return this.countItems(stack.getItem()) < MAX_PICKUPS.get(stack.getItem()) + stack.getMaxCount();
		}
		return true;
	}
}
