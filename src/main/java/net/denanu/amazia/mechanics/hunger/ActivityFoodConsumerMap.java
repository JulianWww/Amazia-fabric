package net.denanu.amazia.mechanics.hunger;

import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.minecraft.item.Item;

public class ActivityFoodConsumerMap {
	public static void consumeFood(final IAmaziaFoodConsumerEntity entity, final float amount) {
		entity.reduceFood(amount);
	}

	public static void consumeFoodWithProbability(final IAmaziaFoodConsumerEntity entity, final int prob, final float amount) {
		if (entity.getRandom().nextInt(prob) == 0) {
			ActivityFoodConsumerMap.consumeFood(entity, amount);
		}
	}

	public static void lookAroundUseFood(final IAmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consumeFoodWithProbability(entity, 10, 1);
	}

	public static void walkUseFood(final IAmaziaFoodConsumerEntity entity, final int foodUsage) {
		ActivityFoodConsumerMap.consumeFoodWithProbability(entity, foodUsage, 1);
	}

	public static void interactWithContainerUseFood(final IAmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consumeFoodWithProbability(entity, 10, 1);
	}

	public static void craftItemUseFood(final IAmaziaDataProviderEntity entity, final Item itm) {
		CraftingHungerManager.eat(entity, itm);
	}
}
