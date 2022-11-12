package net.denanu.amazia.village;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AmaziaData {
	public static final ImmutableSet<Item> CLASSIC_PLANTABLES = ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS);
	public static final ImmutableSet<Item> SAPLINGS = ImmutableSet.of(Items.ACACIA_SAPLING, Items.BIRCH_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING, Items.OAK_SAPLING, Items.SPRUCE_SAPLING);
	public static final ImmutableSet<Item> PLANKS = ImmutableSet.of(Items.ACACIA_PLANKS, Items.BIRCH_PLANKS, Items.CRIMSON_PLANKS, Items.DARK_OAK_PLANKS, Items.JUNGLE_PLANKS, Items.MANGROVE_PLANKS, Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.WARPED_PLANKS);
	public static       ArrayList<Item> ENCHANTABLES;
	
	
	public static void setup() {
		AmaziaData.getEnchantables();
	}

	private static void getEnchantables() {
		AmaziaData.ENCHANTABLES = new ArrayList<Item>();
		for (Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntrySet()) {
			if (entry.getValue().getEnchantability() != 0) {
				AmaziaData.ENCHANTABLES.add(entry.getValue());
			}
		}
		AmaziaData.ENCHANTABLES.trimToSize();
	}
}
