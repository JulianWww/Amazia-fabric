package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BlacksmithEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.SHEARS, Items.AIR);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.WHEAT, 32, Items.CARROT, 32, Items.POTATO, 32, Items.BEETROOT, 32, Items.WHEAT_SEEDS, 32);
	public static final ImmutableSet<Item> CRAFTABLE_ITEMS = ImmutableSet.of();
	
	private AnimationFactory factory = new AnimationFactory(this);

	public BlacksmithEntity(EntityType<? extends PassiveEntity> entityType, World world)  {
		super(entityType, world);
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
		data.addAnimationController(new AnimationController<BlacksmithEntity>(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	// Rancher
	@Override
    protected void initGoals() {
        super.registerBaseGoals();
    }

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDepositItems() {
		// TODO Auto-generated method stub
		return false;
	}
}
