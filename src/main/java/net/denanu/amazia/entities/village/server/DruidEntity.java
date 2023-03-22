package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.goal.druid.AdvanceCropAgeGoToGoal;
import net.denanu.amazia.entities.village.server.goal.druid.AdvanceCropAgeGoal;
import net.denanu.amazia.entities.village.server.goal.druid.RegeneratMineGoToSubGoal;
import net.denanu.amazia.entities.village.server.goal.druid.RegenerateMineSubGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
	private BlockPos toRegrow;

	public DruidEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		// TODO Auto-generated constructor stub
	}

	private <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.generic.walk", true));
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
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(DruidEntity.USABLE_ITEMS, DruidEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	protected void initGoals() {

		this.goalSelector.add(49, new SequenceGoal<>(this, ImmutableList.of(
				new RegeneratMineGoToSubGoal(this, 49),
				new RegenerateMineSubGoal(this, 49)
				)));
		this.goalSelector.add(50, new SequenceGoal<>(this, ImmutableList.of(
				new AdvanceCropAgeGoToGoal(this, 50),
				new AdvanceCropAgeGoal(this, 50)
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

	public void leaveMine() {
		this.mine = null;
	}

	public float getMineRagenerationAbility() {
		return this.professionLevelManager.getMineRegeneration(this.isDepressed());
	}

	public int getPlantAdvanceAgeTime() {
		return this.professionLevelManager.getPlantGrowTime(this.isDepressed());
	}

	public int getGrowRadius() {
		return this.professionLevelManager.getGrowRadius(this.isDepressed());
	}

	/**
	 * @return the toRegrow
	 */
	public BlockPos getToRegrow() {
		return this.toRegrow;
	}

	/**
	 * @param toRegrow the toRegrow to set
	 */
	public BlockPos setToRegrow(final BlockPos toRegrow) {
		return this.toRegrow = toRegrow;
	}

	public int getMaxRegrowMine() {
		return this.professionLevelManager.getMaxMineRegrowAbility(this.isDepressed());
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.DRUID;
	}
}
