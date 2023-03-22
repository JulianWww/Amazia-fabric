package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.goal.teacher.GoToDesk;
import net.denanu.amazia.entities.village.server.goal.teacher.TeachAtDesk;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
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

public class TeacherEntity extends AmaziaVillagerEntity implements IAnimatable {
	private static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of();
	private static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();

	private final AnimationFactory factory = new AnimationFactory(this);

	BlockPos deskLocation;

	public TeacherEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
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
	@Nullable
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(TeacherEntity.USABLE_ITEMS, TeacherEntity.REQUIRED_ITEMS);
	}

	@Override
	@Nullable
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.TEACHER;
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(50, new SequenceGoal<>(this, ImmutableList.of(
				new GoToDesk(this, 50),
				new TeachAtDesk(this, 50)
				)));

		super.registerBaseGoals();
	}

	public BlockPos getDeskLocation() {
		return this.deskLocation;
	}

	public BlockPos setDeskLocation(final BlockPos deskLocation) {
		this.deskLocation = deskLocation;
		return this.deskLocation;
	}

	public int getTeachTime() {
		return 500;
	}
}
