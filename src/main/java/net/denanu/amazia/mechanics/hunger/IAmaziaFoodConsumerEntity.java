package net.denanu.amazia.mechanics.hunger;

import net.minecraft.util.math.random.Random;

public interface IAmaziaFoodConsumerEntity {
	public void reduceFood(float amount);
	public void eatFood(float amount);

	public float getHunger();
	public void setHunger(float value);

	public Random getRandom();

	public default boolean shouldEatFood () {
		return this.isHungry() && this.hasOrRequestFood();
	}

	public default boolean isHungry() {
		return this.getHunger() < 30;
	}

	public void consumeNutrishousItem();
	public boolean hasOrRequestFood();
}
