package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.entities.village.server.goal.child.ChildGoToSchoolGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.village.scedule.VillageActivityGroups;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
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

public class ChildEntity extends AmaziaVillagerEntity implements IAnimatable {
	private static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of();
	private static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();

	private final AnimationFactory factory = new AnimationFactory(this);
	protected ChildEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
	}

	public ChildEntity(final World world) {
		super(AmaziaEntities.CHILD, world);
	}

	public static ChildEntity of(final EntityType<? extends PassiveEntity> entityType, final World world) {
		return new ChildEntity(entityType, world);
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
		return AmaziaEntity.setAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f);
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
	@Override
	public @Nullable Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(ChildEntity.USABLE_ITEMS, ChildEntity.REQUIRED_ITEMS);
	}

	@Override
	public @Nullable HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.CHILD;
	}

	@Override
	public void mobTick() {
		super.mobTick();

		if (this.getActivityScedule().getPerformActionGroup() != VillageActivityGroups.WORK && this.hasVehicle()) {
			this.leaveSchool();
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(50, new ChildGoToSchoolGoal(this, 50));

		super.registerBaseGoals();
	}

	public void leaveSchool() {
		this.stopRiding();
		this.dismountVehicle();
	}
}
