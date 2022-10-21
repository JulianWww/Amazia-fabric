package net.denanu.amazia.entities.village.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.goal.lumber.GoToLumberLocationGoal;
import net.denanu.amazia.entities.village.server.goal.lumber.HarvestTreeGoal;
import net.denanu.amazia.entities.village.server.goal.lumber.PlantSaplingGoal;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.sceduling.utils.LumberPathingData;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class LumberjackEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.ACACIA_SAPLING, 64, Items.BIRCH_SAPLING, 64, Items.DARK_OAK_SAPLING, 64, Items.JUNGLE_SAPLING, 64, Items.OAK_SAPLING, 64, Items.SPRUCE_SAPLING, 64);
	
	private AnimationFactory factory = new AnimationFactory(this);
	private LumberPathingData lumberingLoc;

	public LumberjackEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
    protected void initGoals() {
		this.goalSelector.add(48, new HarvestTreeGoal(this, 48));
		this.goalSelector.add(49, new PlantSaplingGoal(this, 49));
		this.goalSelector.add(50, new GoToLumberLocationGoal(this, 50));
		
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
		data.addAnimationController(new AnimationController<LumberjackEntity>(this, "controller", 0, this::predicate));
	}
	
	@Override
	public boolean canDepositItems() {
		return this.getEmptyInventorySlots() == 0 && this.getDepositableItems() != null;
	}
	
	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(USABLE_ITEMS, REQUIRED_ITEMS);
	}
	
	// LumberJacking

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	public LumberPathingData getLumberingLoc() {
		return lumberingLoc;
	}

	public void setLumberingLoc(LumberPathingData lumberingLoc) {
		this.lumberingLoc = lumberingLoc;
	}
	public boolean hasLumberLoc() {
		return this.lumberingLoc != null && this.lumberingLoc.getPos().isValid();
	}
	public boolean isInLumberLoc() {
		return this.lumberingLoc.getPos().isIn(this.getBlockPos());
	}
	
	public boolean hasSapling() {
		return this.getInventory().containsAny(AmaziaData.SAPLINGS);
	}
	public int getStackPosOfSapleing() {
		for (int idx=0; idx<this.getInventory().size(); idx++) {
			if (AmaziaData.SAPLINGS.contains(this.getInventory().getStack(idx).getItem())) {
				return idx;
			}
		}
		return -1;
	}
	
	@Override
	public boolean canLumber() {
		return true;
	}

	public void requestSapling() {
		if (!this.hasRequestedItems())
		for (Item itm : AmaziaData.SAPLINGS) { this.requestItem(itm); }
	}

	public void requestAxe() {
		if (!this.hasRequestedItems())
		for (Item itm : LumberjackEntity.USABLE_ITEMS) { this.requestItem(itm); }
	}

}
