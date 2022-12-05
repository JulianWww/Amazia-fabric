package net.denanu.amazia.mechanics.hunger;

public class ActivityFoodConsumerMap {
	public static void consumeFood(final AmaziaFoodConsumerEntity entity, final float amount) {
		entity.reduceFood(amount);
	}

	public static void consimeFoodWithProbability(final AmaziaFoodConsumerEntity entity, final int prob, final float amount) {
		if (entity.getRandom().nextInt(prob) == 0) {
			ActivityFoodConsumerMap.consumeFood(entity, amount);
		}
	}

	public static void LookAroundUseFood(final AmaziaFoodConsumerEntity entity) {
		ActivityFoodConsumerMap.consimeFoodWithProbability(entity, 100, 1);
	}

	public static void WalkUseFood(final AmaziaFoodConsumerEntity entity, final int foodUsage) {
		ActivityFoodConsumerMap.consimeFoodWithProbability(entity, foodUsage, 1);
	}
}
