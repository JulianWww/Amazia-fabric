package net.denanu.amazia.utils.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import net.denanu.amazia.mixin.RecipeManagerMixinAcessor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;

public class VillageRecipeManager {
	public static <C extends Inventory, T extends Recipe<C>> HashMap<Item, ArrayList<T>> getAllCraftableRecipes(RecipeManager manager, RecipeType<T> crafting, @Nullable Set<Item> set) {
		HashMap<Item, ArrayList<T>> out = new HashMap<Item, ArrayList<T>>();
		List<T> recipes = ((RecipeManagerMixinAcessor) manager).invokeListAllOfType(crafting);
		Item key;
		for (T recipe : recipes) {
			key = recipe.getOutput().getItem();
			if (set == null || set.contains(key)) {
				if (!out.containsKey(key)) {
					out.put(key, new ArrayList<T>());
				}
				out.get(key).add(recipe);
			}
		}
		for (Entry<Item, ArrayList<T>> element : out.entrySet()) {
			element.getValue().trimToSize();
		}
		return out;
	}
	public static <C extends Inventory, T extends Recipe<C>> HashMap<Item, ArrayList<T>> getAllCraftableRecipes(RecipeManager manager, RecipeType<T> crafting) {
		return VillageRecipeManager.getAllCraftableRecipes(manager, crafting, null);
	}
}
