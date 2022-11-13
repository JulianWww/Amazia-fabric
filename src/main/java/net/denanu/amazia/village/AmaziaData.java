package net.denanu.amazia.village;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.mixin.RecipeManagerMixinAcessor;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolItem;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AmaziaData {
	public static final ImmutableSet<Item> CLASSIC_PLANTABLES = ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS);
	public static final ImmutableSet<Item> SAPLINGS = ImmutableSet.of(Items.ACACIA_SAPLING, Items.BIRCH_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING, Items.OAK_SAPLING, Items.SPRUCE_SAPLING);
	public static final ImmutableSet<Item> PLANKS = ImmutableSet.of(Items.ACACIA_PLANKS, Items.BIRCH_PLANKS, Items.CRIMSON_PLANKS, Items.DARK_OAK_PLANKS, Items.JUNGLE_PLANKS, Items.MANGROVE_PLANKS, Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.WARPED_PLANKS);
	public static       ArrayList<Item> ENCHANTABLES;
	public static       ArrayList<Item> BLASTABLES;


	public static void setup() {
		AmaziaData.getEnchantables();
	}

	private static void getEnchantables() {
		AmaziaData.ENCHANTABLES = new ArrayList<Item>();
		for (final Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntrySet()) {
			if (entry.getValue().getEnchantability() != 0) {
				AmaziaData.ENCHANTABLES.add(entry.getValue());
			}
		}
		AmaziaData.ENCHANTABLES.trimToSize();
	}

	public static void buildBlastables(final MinecraftServer server) {
		AmaziaData.BLASTABLES = new ArrayList<Item>();
		final List<BlastingRecipe> recipes = ((RecipeManagerMixinAcessor) server.getRecipeManager()).invokeListAllOfType(RecipeType.BLASTING);
		for (final BlastingRecipe recipe : recipes) {
			assert recipe.getIngredients().size() == 1;
			for (final ItemStack stack : recipe.getIngredients().get(0).getMatchingStacks()) {
				if (stack.getItem().getClass() == Item.class) {
					AmaziaData.BLASTABLES.add(stack.getItem());
				}
			}
		}
		AmaziaData.BLASTABLES.trimToSize();
	}

	public static List<Item> buildBlacksmithCraftables() {
		final ArrayList<Item> out = new ArrayList<Item>();
		for (final Item item : Registry.ITEM) {
			if (
					item instanceof final ArmorItem armor && BlacksmithEntity.isCraftable(armor) ||
					item instanceof final RangedWeaponItem ranged && BlacksmithEntity.isCraftable(ranged) ||
					item instanceof final ToolItem tool && BlacksmithEntity.isCraftable(tool)
					) {
				out.add(item);
			}
		}
		out.add(Items.STICK);
		out.addAll(AmaziaData.PLANKS);
		return out;
	}
}
