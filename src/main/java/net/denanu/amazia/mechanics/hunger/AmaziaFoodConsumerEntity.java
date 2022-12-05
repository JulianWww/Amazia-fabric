package net.denanu.amazia.mechanics.hunger;

public interface AmaziaFoodConsumerEntity {
	public void reduceFood(float amount);
	public void eatFood(float amount);
	public double getHunger();
}
