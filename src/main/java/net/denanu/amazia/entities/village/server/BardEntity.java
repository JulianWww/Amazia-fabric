package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.goal.bard.PlayMusicToVillagersGoal;
import net.denanu.amazia.entities.village.server.goal.utils.AmaziaGoToTargetGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.Inventory;
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

	private static Predicate<LivingEntity> IS_UNHAPPY = e -> {
		if (e instanceof final AmaziaVillagerEntity villager) {
			return villager.getHappyness() < 50f;
		}
		return false;
	};

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
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bard.walk", true));
			return PlayState.CONTINUE;
		}

		if (this.isSleeping()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.generic.sleep", true));
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
	protected void initGoals() {
		this.targetSelector.add(0, new ActiveTargetGoal<>(this, AmaziaVillagerEntity.class, true, BardEntity.IS_UNHAPPY));


		this.goalSelector.add(40, new SequenceGoal<>(this, ImmutableList.of(
				new AmaziaGoToTargetGoal(this, 40),
				new PlayMusicToVillagersGoal(this, 40)
				)));

		super.registerBaseGoals();
	}

	@Override
	public void onInventoryChanged(final Inventory inventory) {
		super.onInventoryChanged(inventory);
		for (int idx=0; idx < inventory.size(); idx++) {
			if (inventory.getStack(idx).isOf(AmaziaItems.FLUTE)) {
				this.equipStack(EquipmentSlot.MAINHAND, inventory.getStack(idx));
			}
		}
	}
}
