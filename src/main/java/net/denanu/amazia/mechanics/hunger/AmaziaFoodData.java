package net.denanu.amazia.mechanics.hunger;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AmaziaFoodData {
	public static  PriorityQueue<AmaziaFood> NUTRISCHOUS_FOODS = new PriorityQueue<AmaziaFood>(new AmaziaFood.FoodComparator());
	public static  Map<Item, AmaziaFood> TO_FOOD_MAP = new HashMap<Item, AmaziaFood>();

	public static void register(final Item item, final float foodValue) {
		final AmaziaFood data = new AmaziaFood(item, foodValue);
		AmaziaFoodData.TO_FOOD_MAP.put(item, data);
		AmaziaFoodData.NUTRISCHOUS_FOODS.add(data);
	}

	public static void setup() {
		AmaziaFoodData.register(Items.APPLE, 			12);
		AmaziaFoodData.register(Items.BAKED_POTATO, 	35);
		AmaziaFoodData.register(Items.BEETROOT, 		7);
		AmaziaFoodData.register(Items.BEETROOT_SOUP, 	50);
		AmaziaFoodData.register(Items.BREAD,			55);
		AmaziaFoodData.register(Items.CAKE,			7);
		AmaziaFoodData.register(Items.CARROT,			12);
		AmaziaFoodData.register(Items.COOKED_CHICKEN, 	60);
		AmaziaFoodData.register(Items.COOKED_MUTTON,	66);
		AmaziaFoodData.register(Items.PORKCHOP,		80);
		AmaziaFoodData.register(Items.COOKIE,			5);
		AmaziaFoodData.register(Items.GOLDEN_CARROT,	70);
		AmaziaFoodData.register(Items.MELON,			6);
		AmaziaFoodData.register(Items.MUSHROOM_STEW,	50);
		AmaziaFoodData.register(Items.POTATO,			7);
		AmaziaFoodData.register(Items.PUMPKIN_PIE,		35);
		AmaziaFoodData.register(Items.COOKED_BEEF,		80);
	};
}
