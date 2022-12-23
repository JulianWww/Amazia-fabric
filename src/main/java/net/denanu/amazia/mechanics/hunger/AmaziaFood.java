package net.denanu.amazia.mechanics.hunger;

import java.util.Comparator;

import net.minecraft.item.Item;

public class AmaziaFood {
	private final float food;
	private final Item item;
	private final float happinessValue;

	public AmaziaFood(final Item item, final float foodValue, final float happinessValue) {
		this.food = foodValue;
		this.item = item;
		this.happinessValue = happinessValue;
	}

	public float getFoodValue() {
		return this.food;
	}

	public Item getItem() {
		return this.item;
	}

	public float getHappinessValue() {
		return this.happinessValue;
	}

	public static class FoodComparator implements Comparator<AmaziaFood> {
		@Override
		public int compare(final AmaziaFood o1, final AmaziaFood o2) {
			return Float.compare(o1.food, o2.food);
		}
	}
}
