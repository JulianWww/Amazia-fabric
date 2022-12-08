package net.denanu.amazia.entities.village.server.goal.rancher;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.components.AmaziaEntityComponents;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mixin.SheepEntityAccessor;
import net.denanu.amazia.utils.registry.AmaziaRegistrys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FeedAnimalGoal extends TimedVillageGoal<RancherEntity> {
	protected static Map<Identifier, InteractionCallbackPredicate> interactionCallbacks  = new HashMap<Identifier, InteractionCallbackPredicate>();
	protected static Map<Identifier, Predicate<RancherEntity>>     requirementsCallbacks = new HashMap<Identifier, Predicate<RancherEntity>>();

	public FeedAnimalGoal(final RancherEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return this.genreallShouldRun() && !this.getFeedItem().isEmpty();
	}

	@Override
	public boolean shouldContinue() {
		return super.shouldContinue();
	}

	private boolean genreallShouldRun() {
		return this.entity.hasTargetAnimal() &&
				super.canStart() &&
				AmaziaEntityComponents.getIsPartOfVillage(this.entity.targetAnimal) &&
				this.entity.getPos().isInRange(this.entity.targetAnimal.getPos(), 2);
	}

	private boolean chekRequrements() {
		final Predicate<RancherEntity> callback = FeedAnimalGoal.requirementsCallbacks.get(EntityType.getId(this.entity.targetAnimal.getType()));
		if (callback!= null) {
			return callback.test(this.entity);
		}
		return false;
	}

	@Override
	public void stop() {
		super.stop();
		this.entity.releaseTargetEntity();
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getFeedTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.hasTargetAnimal() && !this.entity.targetAnimal.isLeashed()) {
			final Optional<Integer> food = this.getFeedItem();
			if (!food.isEmpty()) {
				this.feed(food.get());
			}

			if (this.entity.canInteractWithEntity() && this.chekRequrements()) {
				final InteractionCallbackPredicate func = FeedAnimalGoal.interactionCallbacks.get(Registry.ENTITY_TYPE.getId(this.entity.targetAnimal.getType()));
				if (func != null) {
					func.run(this.entity);
					this.entity.setInteractionAge();
				}
			}
		}

	}

	private Optional<Integer> getFeedItem() {
		final SimpleInventory inventory = this.entity.getInventory();
		for (int idx = 0; idx < inventory.size(); idx++) {
			if (this.entity.targetAnimal.isBreedingItem(inventory.getStack(idx))) {
				return Optional.of(idx);
			}
		}
		this.requestFeedItem();
		return Optional.empty();
	}

	private void requestFeedItem() {
		final Optional<Item> itm = AmaziaRegistrys.ANIMAL_FEED_ITEMS.getRandom(Registry.ENTITY_TYPE.getId(this.entity.targetAnimal.getType()));
		this.entity.requestItem(itm.get());
		this.entity.releaseTargetEntity();
	}

	private void feed(final int val) {
		final ItemStack itm = this.entity.getInventory().getStack(val);
		if (!itm.isEmpty()) {
			this.entity.targetAnimal.lovePlayer(null);
			itm.decrement(1);
			this.entity.getInventory().setStack(val, itm);
			ActivityFoodConsumerMap.feedAnimalUseFood(this.entity);
		}
	}

	@Nullable
	public static InteractionCallbackPredicate registreInteractionCallback(final EntityType<? extends AnimalEntity> entity, final InteractionCallbackPredicate pred) {
		return FeedAnimalGoal.interactionCallbacks.put(EntityType.getId(entity), pred);
	}

	@Nullable
	public static InteractionCallbackPredicate registreInteractionCallbackIfAbsent(final EntityType<? extends AnimalEntity> entity, final InteractionCallbackPredicate pred) {
		return FeedAnimalGoal.interactionCallbacks.putIfAbsent(EntityType.getId(entity), pred);
	}

	public static Predicate<RancherEntity> registreRequirementsCallback(final EntityType<? extends AnimalEntity> entity, final Predicate<RancherEntity> pred) {
		return FeedAnimalGoal.requirementsCallbacks.put(EntityType.getId(entity), pred);
	}

	@Nullable
	public static Predicate<RancherEntity> registreRequirementsCallbackIfAbsent(final EntityType<? extends AnimalEntity> entity, final Predicate<RancherEntity> pred) {
		return FeedAnimalGoal.requirementsCallbacks.putIfAbsent(EntityType.getId(entity), pred);
	}

	public static void setup() {
		FeedAnimalGoal.registreInteractionCallback (EntityType.COW,   FeedAnimalGoal::milk);
		FeedAnimalGoal.registreRequirementsCallback(EntityType.COW,   FeedAnimalGoal::canMilk);

		FeedAnimalGoal.registreInteractionCallback (EntityType.SHEEP, FeedAnimalGoal::shear);
		FeedAnimalGoal.registreRequirementsCallback(EntityType.SHEEP, FeedAnimalGoal::canShear);
	}

	private static void milk(final RancherEntity rancher) {
		final int loc = rancher.getItemStack(Items.BUCKET);
		if (loc >= 0 && rancher.hasFreeSlot()) {
			ItemStack stack = rancher.getInventory().getStack(loc);
			stack.decrement(1);
			if (stack.getCount() == 0) {
				stack = ItemStack.EMPTY;
			}
			rancher.getInventory().setStack(loc, stack);
			rancher.getInventory().addStack(new ItemStack(Items.MILK_BUCKET, 1));
			ActivityFoodConsumerMap.harvestFromAnimalUseFood(rancher);
		}
	}

	private static boolean canMilk(final RancherEntity rancher) {
		if (rancher.getItemStack(Items.BUCKET) >= 0 && rancher.hasFreeSlot()) {
			return true;
		}
		rancher.requestItem(Items.BUCKET);
		return false;
	}

	private static void shear(final RancherEntity rancher) {
		final int loc = rancher.getItemStack(Items.SHEARS);
		if (((SheepEntity)rancher.targetAnimal).isShearable() && loc >= 0 && rancher.hasFreeSlot()) {
			rancher.damage(loc);
			final SheepEntity sheep = (SheepEntity)rancher.targetAnimal;
			sheep.world.playSoundFromEntity(null, sheep, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.AMBIENT, 1.0f, 1.0f);
			sheep.setSheared(true);
			rancher.getInventory().addStack(
					new ItemStack(SheepEntityAccessor.getDrops().get(sheep.getColor()), JJUtils.rand.nextInt(3) + 1)
					);
			ActivityFoodConsumerMap.harvestFromAnimalUseFood(rancher);
		}
	}

	private static boolean canShear(final RancherEntity rancher) {
		if (!((SheepEntity)rancher.targetAnimal).isShearable() ||  !rancher.hasFreeSlot()) {
			return false;
		}
		if (rancher.getItemStack(Items.SHEARS) >= 0) {
			return true;
		}
		rancher.requestItem(Items.SHEARS);
		return false;
	}

	private interface InteractionCallbackPredicate {
		public void run(RancherEntity rancher);
	}

}
