package net.denanu.amazia;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.entity.AmaziaBlockEntities;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.utils.crafting.VillageRecipeManager;
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
	
	public static HashMap<Item,ArrayList<CraftingRecipe>> MINER_CRAFTS;
	
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

	public static void registerCrafters(MinecraftServer server) {
		MINER_CRAFTS = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(), RecipeType.CRAFTING, MinerEntity.CRAFTABLES);
		return;
	}
}
