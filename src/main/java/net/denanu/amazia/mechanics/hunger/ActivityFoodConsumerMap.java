package net.denanu.amazia.mechanics.hunger;

public class ActivityFoodConsumerMap {
	public static void consumeFood(final AmaziaFoodConsumerEntity entity, final float amount) {
		entity.reduceFood(amount);
	}
}
