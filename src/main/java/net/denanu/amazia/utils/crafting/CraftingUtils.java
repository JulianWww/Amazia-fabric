package net.denanu.amazia.utils.crafting;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.denanu.amazia.JJUtils;
import net.minecraft.item.Item;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;

public class CraftingUtils {
	public static HashMap<Ingredient, Integer> getUniqueIngredients(CraftingRecipe recipe) {
		HashMap<Ingredient, Integer> out = new HashMap<Ingredient, Integer>();
		for (Ingredient ingredient : recipe.getIngredients()) {
			if (!out.containsKey(ingredient)) {
				out.put(ingredient, 0);
			}
			out.put(ingredient, out.get(ingredient)+1);
		}
		return out;
	}
	
	public static Map<Item, Integer> getRecipyInput(CraftingRecipe recipe) {
		Map<Item, Integer> counter = new HashMap<Item, Integer>();
		HashMap<Ingredient, Integer> ingredints = getUniqueIngredients(recipe);
		
		for (Entry<Ingredient, Integer> ingredient : ingredints.entrySet()) {
			if (ingredient.getKey().getMatchingStacks().length > 0) {
				counter.put(
						JJUtils.getRandomArrayElement(ingredient.getKey().getMatchingStacks()).getItem()
						, ingredient.getValue());
			}
		}
		return counter;
	}
}
