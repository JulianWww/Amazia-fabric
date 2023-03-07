package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.goal.cleric.BlessEntityGoal;
import net.denanu.amazia.entities.village.server.goal.cleric.GoToBlessEntityGoal;
import net.denanu.amazia.entities.village.server.goal.cleric.HealEntityGoal;
import net.denanu.amazia.entities.village.server.goal.cleric.SelectHealingGuardTargetGoal;
import net.denanu.amazia.entities.village.server.goal.cleric.SelectHealingTargetGoal;
import net.denanu.amazia.entities.village.server.goal.utils.AmaziaGoToTargetGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.particles.VillageItemDataPropvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public class ClericEntity extends AmaziaVillagerEntity implements IAnimatable {
	private static final ImmutableMap<VillageItemDataPropvider, Integer> REQUIRED_ITEMS = ImmutableMap.of();
	private static final ImmutableSet<VillageItemDataPropvider> USABLE_ITEMS = ImmutableSet.of();

	private final AnimationFactory factory = new AnimationFactory(this);

	public ClericEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
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
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(ClericEntity.USABLE_ITEMS, ClericEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	protected void initGoals() {

		this.targetSelector.add(0, new SelectHealingGuardTargetGoal(this, GuardEntity.class, true));
		this.targetSelector.add(1, new SelectHealingTargetGoal<>(this, AmaziaVillagerEntity.class, true, AmaziaVillagerEntity::isNotFullHealth));
		this.targetSelector.add(2, new SelectHealingTargetGoal<>(this, PlayerEntity.class, true, AmaziaVillagerEntity::isNotFullHealth));

		this.goalSelector.add(40, new SequenceGoal<>(this, ImmutableList.of(
				new AmaziaGoToTargetGoal(this, 40),
				new HealEntityGoal(this, 40)
				)));

		final GoToBlessEntityGoal goToBlessEntityGoal = new GoToBlessEntityGoal(this, 41, 40);
		this.goalSelector.add(41, new SequenceGoal<>(this, ImmutableList.of(
				goToBlessEntityGoal,
				new BlessEntityGoal(this, 41, goToBlessEntityGoal)
				)));

		super.registerBaseGoals();
	}

	public int getHealTime() {
		return this.professionLevelManager.getHealTime(this.isDepressed());
	}

	public int getBlessTime() {
		return this.professionLevelManager.getBlessTime(this.isDepressed());
	}

	public int getBlessLastingTime() {
		return this.professionLevelManager.getBlessLastTime(this.isDepressed());
	}

	public float getHealAmount() {
		return this.professionLevelManager.getHealAmount(this.isDepressed());
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.CLERIC;
	}

}
