package net.denanu.amazia.mechanics.hunger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.denanu.amazia.utils.random.LevelBasedLinearRange;
import net.denanu.amazia.utils.random.LevelBasedRandomnessFactory;
import net.denanu.amazia.village.AmaziaData;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CraftingHungerManager {
	public static final Map<Item, LevelBasedRandomnessFactory<Float>> ITEM_HUNGER_MAP = new HashMap<Item, LevelBasedRandomnessFactory<Float>>();

	public static void register(final Item item, final LevelBasedRandomnessFactory<Float> factory) {
		CraftingHungerManager.ITEM_HUNGER_MAP.put(item, factory);
	}

	public static void setup() {
		CraftingHungerManager.registerPlanks();

		CraftingHungerManager.registerAll(ImmutableList.of(Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL),		new LevelBasedLinearRange(21, 10));
		CraftingHungerManager.registerAll(ImmutableList.of(Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SWORD, Items.STONE_SHOVEL),			new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.registerAll(ImmutableList.of(Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL),		new LevelBasedLinearRange(17, 3));
		CraftingHungerManager.registerAll(ImmutableList.of(Items.IRON_HOE, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SWORD, Items.IRON_SHOVEL),				new LevelBasedLinearRange(19, 4));
		CraftingHungerManager.registerAll(ImmutableList.of(Items.DIAMOND_HOE, Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL),	new LevelBasedLinearRange(21, 5));

		CraftingHungerManager.register(Items.BOW,					new LevelBasedLinearRange(11, 2));

		CraftingHungerManager.register(Items.LEATHER_BOOTS,			new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.register(Items.LEATHER_LEGGINGS,		new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.register(Items.LEATHER_CHESTPLATE,	new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.register(Items.LEATHER_HELMET,		new LevelBasedLinearRange(15, 2));

		CraftingHungerManager.register(Items.GOLDEN_BOOTS,			new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.register(Items.GOLDEN_LEGGINGS,		new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.register(Items.GOLDEN_CHESTPLATE,		new LevelBasedLinearRange(15, 2));
		CraftingHungerManager.register(Items.GOLDEN_HELMET,			new LevelBasedLinearRange(15, 2));

		CraftingHungerManager.register(Items.IRON_BOOTS,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.IRON_LEGGINGS,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.IRON_CHESTPLATE,		new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.IRON_HELMET,			new LevelBasedLinearRange(11, 2));

		CraftingHungerManager.register(Items.DIAMOND_BOOTS,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.DIAMOND_LEGGINGS,		new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.DIAMOND_CHESTPLATE,	new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.DIAMOND_HELMET,		new LevelBasedLinearRange(11, 2));

		CraftingHungerManager.register(Items.STICK,					new LevelBasedLinearRange(1, 0));
		CraftingHungerManager.register(Items.TORCH,					new LevelBasedLinearRange(5,  2));

		CraftingHungerManager.register(Items.GOLDEN_APPLE,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.COOKIE,				new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.PUMPKIN_PIE,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.BREAD,					new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.CAKE,					new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.BEETROOT_SOUP,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.GOLDEN_CARROT,			new LevelBasedLinearRange(11, 2));
		CraftingHungerManager.register(Items.SUGAR,					new LevelBasedLinearRange(11, 2));
	}

	public static void eat(final IAmaziaDataProviderEntity entity, final Item itm) {
		final LevelBasedRandomnessFactory<Float> foodCostFactory = CraftingHungerManager.ITEM_HUNGER_MAP.get(itm);
		if (foodCostFactory == null) {
			final StringBuilder builder = new StringBuilder();
			builder
			.append("Missing food cost factory for: ")
			.append(itm)
			.append(" add using CraftingHungerManager.register");
			Amazia.LOGGER.info(
					builder.toString()
					);
		}
		else {
			ActivityFoodConsumerMap.consumeFood(entity, foodCostFactory.next(entity));
		}
	}

	private static void registerPlanks() {
		CraftingHungerManager.registerAll(AmaziaData.PLANKS, new LevelBasedLinearRange(11, 2));
	}

	private static void registerAll(final Collection<Item> items, final LevelBasedRandomnessFactory<Float> factory) {
		for (final Item item : items) {
			CraftingHungerManager.register(item, factory);
		}
	}
}
