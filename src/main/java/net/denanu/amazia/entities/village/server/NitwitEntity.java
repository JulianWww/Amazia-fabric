package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.mechanics.education.IAmaziaEducatedEntity;
import net.denanu.amazia.mechanics.happyness.IAmaziaHappynessEntity;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

// Also doubles as the child

public class NitwitEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.AIR);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of();

	private final AnimationFactory factory = new AnimationFactory(this);

	public NitwitEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
	}

	public static void spawn(final World world, final BlockPos pos) {
		final NitwitEntity entity = new NitwitEntity(AmaziaEntities.NITWIT, world);
		entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
		entity.setup();
		world.spawnEntity(entity);
	}

	@Override
	public void setup() {
		super.setup();
		this.education = IAmaziaEducatedEntity.baseEducation(this);
		this.happyness = IAmaziaHappynessEntity.getDefaultHappyness();
		this.dataTracker.set(AmaziaVillagerEntity.INTELLIGENCE, IAmaziaIntelligenceEntity.getInitalIntelligence());
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
		return this.getDepositableItems(NitwitEntity.USABLE_ITEMS, NitwitEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public boolean canDepositItems() {
		return !this.hasFreeSlot();
	}

	@Override
	protected void initGoals() {
		super.registerBaseGoals();
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.NITWIT;
	}
}
