package net.denanu.amazia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.GUI.AmaziaScreens;
import net.denanu.amazia.advancement.criterion.AmaziaCriterions;
import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.entity.AmaziaBlockEntities;
import net.denanu.amazia.commands.AmaziaCommand;
import net.denanu.amazia.commands.AmaziaGameRules;
import net.denanu.amazia.commands.args.AmaziaArgumentTypes;
import net.denanu.amazia.compat.mc.worldgen.Worldgen;
import net.denanu.amazia.data.AmaziaStatusEffects;
import net.denanu.amazia.data.LootTables;
import net.denanu.amazia.economy.EconomyFactory;
import net.denanu.amazia.economy.ProfessionFactory;
import net.denanu.amazia.economy.offerModifiers.price.AmaziaValueModifiers;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.entities.AmaziaEntityAttributes;
import net.denanu.amazia.entities.village.both.Trackers;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.ChefEntity;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaIds;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.mechanics.hunger.CraftingHungerManager;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.mixin.MinecraftServerWorldAccessor;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.particles.AmaziaParticles;
import net.denanu.amazia.sounds.AmaziaSounds;
import net.denanu.amazia.utils.AmaziaPersistentStateHandler;
import net.denanu.amazia.utils.Predicates;
import net.denanu.amazia.utils.crafting.VillageRecipeManager;
import net.denanu.amazia.utils.registry.AmaziaRegistrys;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.VillageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.scoreboard.AbstractTeam.CollisionRule;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import software.bernie.geckolib3.GeckoLib;

public class Amazia implements ModInitializer {
	public static final String MOD_ID = "amazia";
	public static final Logger LOGGER = LoggerFactory.getLogger(Amazia.MOD_ID);

	public static final int LOWER_WORLD_BORDER = -64;

	public static HashMap<Item, ArrayList<CraftingRecipe>> FARMER_CRAFTS;
	public static HashMap<Item, ArrayList<CraftingRecipe>> MINER_CRAFTS;
	public static HashMap<Item, ArrayList<CraftingRecipe>> LUMBERJACK_CRAFTS;
	public static HashMap<Item, ArrayList<CraftingRecipe>> BLACKSMITH_CRAFTABLES;
	public static HashMap<Item, ArrayList<CraftingRecipe>> CHEF_CRAFTABLES;
	public static HashMap<Item, ArrayList<CraftingRecipe>> GUARD_CRAFTABLES;

	private static VillageManager villageManager;
	public static Team TEAM;

	@Override
	public void onInitialize() {
		// Registries
		AmaziaItems.registerModItems();
		AmaziaBlocks.registerModBlocks();
		AmaziaEntities.registerAttributes();
		AmaziaBlockEntities.registerBlockEntities();

		GeckoLib.initialize();

		Amazia.villageManager = new VillageManager();

		// Economy
		EconomyFactory.setup();
		ProfessionFactory.setup();
		AmaziaValueModifiers.setup();
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(AmaziaPersistentStateHandler::fromNbt, AmaziaPersistentStateHandler::loadDefault, Amazia.MOD_ID);

			final Identifier villageTeam = Identifier.of(Amazia.MOD_ID, "village_internal_team");
			final ServerScoreboard scoreboard = server.getScoreboard();
			if (scoreboard.getTeam(villageTeam.toTranslationKey()) != null) {
				return;
			}
			Amazia.TEAM = scoreboard.addTeam(villageTeam.toTranslationKey());
			Amazia.TEAM.setDisplayName(Text.translatable(villageTeam.toTranslationKey()));
			Amazia.TEAM.setCollisionRule(CollisionRule.NEVER);

			for (final ServerWorld world : server.getWorlds()) {
				final MinecraftServerWorldAccessor worldAccess = (MinecraftServerWorldAccessor)world;
				for (final Iterator<Entity> iter = worldAccess.getEntityManager().getLookup().iterate().iterator(); iter.hasNext();) {
					if (iter.next() instanceof final AmaziaVillagerEntity entity) {
						server.getScoreboard().addPlayerToTeam(entity.getUuidAsString(), Amazia.TEAM);
					}
				}
			}
		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			Amazia.TEAM = null;
		});

		// Networking
		AmaziaNetworking.registerC2SPackets();

		// commands
		AmaziaArgumentTypes.setup();
		CommandRegistrationCallback.EVENT.register(AmaziaCommand::register);
		AmaziaGameRules.setup();

		//Configs
		BlockHighlightingAmaziaIds.setup();

		//SOUND
		AmaziaSounds.setup();

		// Registry
		AmaziaRegistrys.setup();
		AmaziaEntityAttributes.setup();
		Trackers.setup();

		// static data generated on runtime files
		AmaziaData.setup();
		Predicates.setup();
		LootTables.setup();

		// Mechanics
		CraftingHungerManager.setup();
		AmaziaProfessions.setup();

		// Status effects
		AmaziaStatusEffects.setup();
		AmaziaCriterions.setup();
		AmaziaScreens.setup();
		AmaziaParticles.setup();
		Worldgen.setup();
	}

	public static VillageManager getVillageManager() {
		return Amazia.villageManager;
	}

	private static Set<Item> union(final ImmutableSet<Item> a, final ImmutableSet<Item> b) {
		final Set<Item> out = new HashSet<>();
		out.addAll(b);
		out.addAll(a);
		return out;
	}

	public static void registerCrafters(final MinecraftServer server) {
		Amazia.FARMER_CRAFTS = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(),
				RecipeType.CRAFTING, Amazia.union(FarmerEntity.CRAFTABLES, AmaziaData.PLANKS));
		Amazia.MINER_CRAFTS = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(),
				RecipeType.CRAFTING, Amazia.union(MinerEntity.CRAFTABLES, AmaziaData.PLANKS));
		Amazia.LUMBERJACK_CRAFTS = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(),
				RecipeType.CRAFTING, Amazia.union(LumberjackEntity.CRAFTABLES, AmaziaData.PLANKS));
		Amazia.BLACKSMITH_CRAFTABLES = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(),
				RecipeType.CRAFTING, AmaziaData.buildBlacksmithCraftables());
		Amazia.CHEF_CRAFTABLES = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(),
				RecipeType.CRAFTING, ChefEntity.CRAFTABLES);
		Amazia.GUARD_CRAFTABLES = VillageRecipeManager.getAllCraftableRecipes(server.getRecipeManager(),
				RecipeType.CRAFTING, Amazia.union(GuardEntity.CRAFTABLES, AmaziaData.PLANKS));

		AmaziaData.buildBlastables(server);
		AmaziaData.buildSmokables(server);
		AmaziaData.buildGuardUsables();
	}
}
