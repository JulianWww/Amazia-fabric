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
import net.denanu.amazia.commands.AmaziaCommand;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.EconomyFactory;
import net.denanu.amazia.economy.ProfessionFactory;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.utils.crafting.VillageRecipeManager;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.VillageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Item;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import software.bernie.geckolib3.GeckoLib;
import static net.minecraft.server.command.CommandManager.literal;
public class Amazia implements ModInitializer {
	public static final String MOD_ID = "amazia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final int LOWER_WORLD_BORDER = -64;
	
	public static HashMap<Item,ArrayList<CraftingRecipe>> FARMER_CRAFTS;
	public static HashMap<Item,ArrayList<CraftingRecipe>> MINER_CRAFTS;
	public static HashMap<Item,ArrayList<CraftingRecipe>> LUMBERJACK_CRAFTS;
	
	
	
	public static Economy economy;
	
	
	
	private static VillageManager villageManager;

	@Override
	public void onInitialize() {
		// Registries
		AmaziaItems.registerModItems();
		AmaziaBlocks.registerModBlocks();
		AmaziaEntities.registerAttributes();
		AmaziaBlockEntities.registerBlockEntities();
		
		GeckoLib.initialize();
		
		villageManager = new VillageManager();
		
		//Economy
		EconomyFactory.setup();
		ProfessionFactory.setup();
		
		//Networking
		AmaziaNetworking.registerC2SPackets();
		
		
	    CommandRegistrationCallback.EVENT.register(AmaziaCommand::register);
	    
	    
	    // Economy
	    ServerLifecycleEvents.SERVER_STARTED.register(server -> {
	    	economy = (Economy) server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(Economy::fromNbt, Economy::new, MOD_ID);
        });
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
