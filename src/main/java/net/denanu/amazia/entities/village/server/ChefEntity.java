package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.blacksmithing.PutItemsInFurnaceGoal;
import net.denanu.amazia.entities.village.server.goal.chef.GoToKitchenGoal;
import net.denanu.amazia.entities.village.server.goal.chef.GoToSmokerGoal;
import net.denanu.amazia.entities.village.server.goal.utils.CraftAtCraftingLocationGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.village.AmaziaData;
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

public class ChefEntity extends AmaziaSmelterVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.COAL, 64);


	AnimationFactory factory = new AnimationFactory(this);

	public ChefEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
	}

	@Override
	@Nullable
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(ChefEntity.USABLE_ITEMS, ChefEntity.REQUIRED_ITEMS);
	}

	@Override
	@Nullable
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return Amazia.CHEF_CRAFTABLES;
	}

	@Override
	public boolean canCraft () {
		return true;
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
		data.addAnimationController(new AnimationController<ChefEntity>(this, "controller", 0, this::predicate));
	}
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public void findSmeltingItem() {
		this.findSmeltingItem(AmaziaData.SMOKABLES);
	}

	@Override
	public void requestSmeltable() {
		this.requestItem(JJUtils.getRandomListElement(AmaziaData.SMOKABLES));
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(50, new SequenceGoal<ChefEntity>(this, ImmutableList.of(
				new GoToSmokerGoal(this, 50),
				new PutItemsInFurnaceGoal<ChefEntity>(this, 50)
				)));

		this.goalSelector.add(51, new SequenceGoal<ChefEntity>(this, ImmutableList.of(
				new GoToKitchenGoal(this, 51),
				new CraftAtCraftingLocationGoal(this, 51)
				)));

		super.registerBaseGoals(this::scanForCoal, this::scanForCoal, false);
	}

	@Override
	public void requestCraftable() {
		if (!this.wantsToCraft() && this.hasVillage()) {
			this.tryCraftingStart(JJUtils.getRandomListElement(AmaziaData.COOK_CRAFTABLE));
		}
	}
}
