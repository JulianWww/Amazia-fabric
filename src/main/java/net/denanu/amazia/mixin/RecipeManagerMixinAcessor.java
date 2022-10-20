package net.denanu.amazia.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

@Mixin(RecipeManager.class)
public interface RecipeManagerMixinAcessor {
	@Accessor("recipes")
	public Map<RecipeType<?>, Map<Identifier, Recipe<?>>> getRecipes();
	
	@Invoker("listAllOfType")
	public <C extends Inventory, T extends Recipe<C>> List<T> invokeListAllOfType(RecipeType<T> type);
}
