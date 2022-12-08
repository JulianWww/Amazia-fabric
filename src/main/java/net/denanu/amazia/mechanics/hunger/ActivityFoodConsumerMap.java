package net.denanu.amazia.mechanics.hunger;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.denanu.amazia.utils.random.LevelBasedLinearRange;
import net.denanu.amazia.utils.random.LevelBasedRandomnessFactory;
import net.minecraft.item.Item;

public class ActivityFoodConsumerMap {
	private static final LevelBasedRandomnessFactory<Float> ENCHANT = new LevelBasedLinearRange(7, 3);

	static void consumeFood(final IAmaziaFoodConsumerEntity entity, final float amount) {
		entity.reduceFood(amount);
		Amazia.LOGGER.info("ate");
	}

	private static void consumeFoodWithProbability(final IAmaziaFoodConsumerEntity entity, final int prob, final float amount) {
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

	public static void healUseFood(final IAmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 6);
	}

	public static void blessUseFood(final IAmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 6);
	}

	public static void growCropUseFood(final IAmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 2);
	}

	public static void regrowMineUseFood(final IAmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 8);
	}

	public static void enchantUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, ActivityFoodConsumerMap.ENCHANT.next(entity));
	}

	public static void harvestCropUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void plantCropUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void tillLandUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 5);
	}

	public static void melleAttackUseFood(final IAmaziaDataProviderEntity entity) {
		//ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void rangedAttackUseFood(final IAmaziaDataProviderEntity entity) {
		//ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void combatMovementUseFood(final IAmaziaDataProviderEntity entity) {
		//ActivityFoodConsumerMap.consumeFoodWithProbability(entity, 50, 1);
	}

	public static void harvestTreeUseFood(final IAmaziaDataProviderEntity entity, final int logs) {
		ActivityFoodConsumerMap.consumeFood(entity, logs/2);
	}

	public static void plantSaplingUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void mineBlockUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void feedAnimalUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 1);
	}

	public static void harvestFromAnimalUseFood(final IAmaziaDataProviderEntity entity) {
		ActivityFoodConsumerMap.consumeFood(entity, 1);
	}
}
