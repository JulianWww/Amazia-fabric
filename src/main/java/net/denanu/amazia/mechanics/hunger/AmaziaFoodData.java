package net.denanu.amazia.mechanics.hunger;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AmaziaFoodData {
	public static  PriorityQueue<AmaziaFood> NUTRISCHOUS_FOODS = new PriorityQueue<>(new AmaziaFood.FoodComparator());
	public static  Map<Item, AmaziaFood> TO_FOOD_MAP = new HashMap<>();

	public static void register(final Item item, final float foodValue, final float happinessValue) {
		final AmaziaFood data = new AmaziaFood(item, foodValue, happinessValue);
		AmaziaFoodData.TO_FOOD_MAP.put(item, data);
		AmaziaFoodData.NUTRISCHOUS_FOODS.add(data);
	}

	public static void setup() {
		AmaziaFoodData.register(Items.APPLE, 			12, 	0);
		AmaziaFoodData.register(Items.BAKED_POTATO, 	35, 	1);
		AmaziaFoodData.register(Items.BEETROOT, 		7, 	   -1);
		AmaziaFoodData.register(Items.BEETROOT_SOUP, 	50,		6);
		AmaziaFoodData.register(Items.BREAD,			55,		4);
		AmaziaFoodData.register(Items.CAKE,				7,		25);
		AmaziaFoodData.register(Items.CARROT,			12,	   -1);
		AmaziaFoodData.register(Items.COOKED_CHICKEN, 	60,		12);
		AmaziaFoodData.register(Items.COOKED_MUTTON,	66,		6);
		AmaziaFoodData.register(Items.PORKCHOP,			80,		4);
		AmaziaFoodData.register(Items.COOKIE,			5,		14);
		AmaziaFoodData.register(Items.GOLDEN_CARROT,	70,		20);
		AmaziaFoodData.register(Items.MELON,			6,		3);
		AmaziaFoodData.register(Items.MUSHROOM_STEW,	50,		4);
		AmaziaFoodData.register(Items.POTATO,			7,	   -1);
		AmaziaFoodData.register(Items.PUMPKIN_PIE,		35,		15);
		AmaziaFoodData.register(Items.COOKED_BEEF,		80,		30);
	}
}
