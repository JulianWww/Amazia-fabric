package net.denanu.amazia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.entity.AmaziaBlockEntities;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.utils.crafting.VillageRecipeManager;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.VillageManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import software.bernie.geckolib3.GeckoLib;

public class Amazia implements ModInitializer {
	public static final String MOD_ID = "amazia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final int LOWER_WORLD_BORDER = -64;
	
	public static HashMap<Item,ArrayList<CraftingRecipe>> FARMER_CRAFTS;
	public static HashMap<Item,ArrayList<CraftingRecipe>> MINER_CRAFTS;
	public static HashMap<Item,ArrayList<CraftingRecipe>> LUMBERJACK_CRAFTS;
	
	private static VillageManager villageManager;

	@Override
	public void onInitialize() {
		AmaziaItems.registerModItems();
		AmaziaBlocks.registerModBlocks();
		
		AmaziaEntities.registerAttributes();
		
		GeckoLib.initialize();
		
		AmaziaBlockEntities.registerBlockEntities();
		
		villageManager = new VillageManager();
	}

	public static VillageManager getVillageManager() {
		return villageManager;
	}
	
	private static Set<Item> union(ImmutableSet<Item> a, ImmutableSet<Item> b) {
		Set<Item> out = new HashSet<Item>();
		out.addAll(b);
		out.addAll(a);
		return out;
	}

	public static void registerCrafters(MinecraftServer server) {
		FARMER_CRAFTS		= VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(), RecipeType.CRAFTING, union(FarmerEntity.CRAFTABLES, 		AmaziaData.PLANKS));
		MINER_CRAFTS 		= VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(), RecipeType.CRAFTING, union(MinerEntity.CRAFTABLES,			AmaziaData.PLANKS));
		LUMBERJACK_CRAFTS	= VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(), RecipeType.CRAFTING, union(LumberjackEntity.CRAFTABLES,	AmaziaData.PLANKS));
		return;
	}
}
