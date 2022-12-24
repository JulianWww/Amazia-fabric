package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BardEntity extends AmaziaVillagerEntity implements IAnimatable {
	private static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of();
	private static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();

	AnimationFactory factory = new AnimationFactory(this);

	public BardEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(BardEntity.USABLE_ITEMS, BardEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.BARD;
	}

	@Override
	public boolean canDepositItems() {
		return !this.hasFreeSlot();
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
		data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
	}
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
}
