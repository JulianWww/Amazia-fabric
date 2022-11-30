package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.goal.druid.RegeneratMineGoToSubGoal;
import net.denanu.amazia.entities.village.server.goal.druid.RegenerateMineSubGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class DruidEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of();

	private final AnimationFactory factory = new AnimationFactory(this);

	private MineStructure mine;

	public DruidEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		// TODO Auto-generated constructor stub
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
		data.addAnimationController(new AnimationController<DruidEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(DruidEntity.USABLE_ITEMS, DruidEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	protected void initGoals() {

		this.goalSelector.add(50, new SequenceGoal<DruidEntity>(this, ImmutableList.of(
				new RegeneratMineGoToSubGoal(this, 50),
				new RegenerateMineSubGoal(this, 50)
				)));

		super.registerBaseGoals();
	}

	@Override
	public boolean canDepositItems() {
		return !this.hasFreeSlot();
	}

	@Nullable
	public MineStructure getMine() {
		return this.mine;
	}

	public MineStructure setMine() {
		return this.mine = this.getVillage().getMineing().getSugerstedRegenerationMine();
	}

}
