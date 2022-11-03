package net.denanu.amazia.economy;

import java.util.Collection;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.itemEconomy.BaseItemEconomy;
import net.denanu.amazia.economy.itemEconomy.ItemCompundEconomy;
import net.denanu.amazia.economy.itemEconomy.ItemEconomy;
import net.denanu.amazia.economy.offerModifiers.finalizers.AmaziaFinalModifiers;
import net.denanu.amazia.economy.offerModifiers.item.EnchantmentModifier;
import net.denanu.amazia.economy.offerModifiers.item.ItemReselector;
import net.denanu.amazia.economy.offerModifiers.item.PotionEffectModifier;
import net.denanu.amazia.utils.random.ConstantValue;
import net.denanu.amazia.utils.random.ConstrainedGaussianRandom;
import net.denanu.amazia.utils.random.LinearRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

public class EconomyFactory {
	public final static EnchantmentModifier HELMET_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.RESPIRATION, 				1.0f)
			.add(Enchantments.AQUA_AFFINITY,			1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier CHESTPLATE_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier LEGGINS_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.SWIFT_SNEAK, 				1.0f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier BOOTS_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.FEATHER_FALLING,			1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.DEPTH_STRIDER, 			1.0f)
			.add(Enchantments.SOUL_SPEED, 				1.0f)
			.add(Enchantments.FROST_WALKER, 			1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier SHIELD_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.5f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier FISCHING_ROD_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.5f)
			.add(Enchantments.LURE,						1.0f)
			.add(Enchantments.LUCK_OF_THE_SEA, 			1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier BOW_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.5f)
			.add(Enchantments.POWER, 					5.0f)
			.add(Enchantments.PUNCH, 					1.0f)
			.add(Enchantments.FLAME, 					1.0f)
			.add(Enchantments.INFINITY, 				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier CROSSBOW_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.5f)
			.add(Enchantments.QUICK_CHARGE, 			5.0f)
			.add(Enchantments.MULTISHOT, 				1.0f)
			.add(Enchantments.PIERCING, 				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static ItemReselector BANNER_COLOR_MODIFIER = new ItemReselector()
			.add(Items.BLACK_BANNER, 	   10.0f)
			.add(Items.BLUE_BANNER, 		1.0f)
			.add(Items.BROWN_BANNER, 		1.0f)
			.add(Items.CYAN_BANNER, 		1.0f)
			.add(Items.GRAY_BANNER, 		1.0f)
			.add(Items.GREEN_BANNER, 		1.0f)
			.add(Items.LIGHT_BLUE_BANNER, 	1.0f)
			.add(Items.LIGHT_GRAY_BANNER, 	1.0f)
			.add(Items.LIME_BANNER, 		1.0f)
			.add(Items.MAGENTA_BANNER, 		1.0f)
			.add(Items.ORANGE_BANNER, 		1.0f)
			.add(Items.PINK_BANNER, 		1.0f)
			.add(Items.PURPLE_BANNER, 		1.0f)
			.add(Items.RED_BANNER, 			1.0f)
			.add(Items.WHITE_BANNER, 	   20.0f)
			.add(Items.YELLOW_BANNER, 		1.0f)
			;
	
	public final static PotionEffectModifier ARROW_POTION_EFFECT_MODIFIER = new PotionEffectModifier();
	
	 																								 // value       volatility  return rate | stackSize generator							professions
	public final static BaseItemEconomy COAL = 					register(Items.COAL, 					0.0666f, 	0.0001f, 	0.01f,	new ConstrainedGaussianRandom(20f, 8f, 64, 1), 	ImmutableSet.of("armorer", "butcher", "fischerman"));
	public final static BaseItemEconomy IRON_HELMET = 			register(Items.IRON_HELMET, 			5, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_CHESTPLATE = 		register(Items.IRON_CHESTPLATE, 		9, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_LEGGINS =			register(Items.IRON_LEGGINGS, 			7, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_BOOTS = 			register(Items.IRON_BOOTS, 				4, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_INGOT = 			register(Items.IRON_INGOT, 				0.25f,		0.001f, 	0.01f, 	new LinearRandom(1, 64), 						ImmutableSet.of("armorer"));
	public final static BaseItemEconomy BELL = 					register(Items.BELL, 					36, 		2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer"));
	public final static BaseItemEconomy LAVA_BUCKET =			register(Items.LAVA_BUCKET, 			1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer"));
	public final static BaseItemEconomy DIAMOND = 				register(Items.DIAMOND, 				10,			0.01f, 		0.001f, new ConstrainedGaussianRandom(16f, 4f, 32, 1), 	ImmutableSet.of("armorer"));
	public final static BaseItemEconomy CHAINMAIL_HELMET = 		register(Items.CHAINMAIL_HELMET,		1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_CHESTPLATE = 	register(Items.CHAINMAIL_CHESTPLATE,	4, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_LEGGINGS = 	register(Items.CHAINMAIL_LEGGINGS, 		3, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_BOOTS = 		register(Items.CHAINMAIL_BOOTS, 		1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy SHIELD = 				register(Items.SHIELD, 					5, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(SHIELD_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_HELMET = 		register(Items.DIAMOND_HELMET, 			27, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_CHESTPLATE = 	register(Items.DIAMOND_CHESTPLATE, 		35, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_LEGGINGS = 		register(Items.DIAMOND_LEGGINGS, 		33, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_BOOTS = 		register(Items.DIAMOND_BOOTS, 			27, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
	
	public final static BaseItemEconomy CHICKEN = 				register(Items.CHICKEN, 				0.07f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	public final static BaseItemEconomy PORKCHOP = 				register(Items.PORKCHOP, 				0.14f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	public final static BaseItemEconomy RABBIT = 				register(Items.RABBIT, 					0.25f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	public final static BaseItemEconomy RABBIT_STEW = 			register(Items.RABBIT_STEW, 			1, 			1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("butcher")); // maybe make this a compund as well
	public final static BaseItemEconomy COOKED_PORKCHOP = 		ItemCompundEconomy.register(Items.COOKED_PORKCHOP, ImmutableSet.of(ImmutablePair.of(COAL, 0.2f), ImmutablePair.of(PORKCHOP, 1f)), new ConstrainedGaussianRandom(32, 16, 64, 1), ImmutableSet.of("butcher"));
	public final static BaseItemEconomy COOKED_CHICKEN = 		ItemCompundEconomy.register(Items.COOKED_CHICKEN,  ImmutableSet.of(ImmutablePair.of(COAL, 0.2f), ImmutablePair.of(CHICKEN,  1f)), new ConstrainedGaussianRandom(32, 16, 64, 1), ImmutableSet.of("butcher"));
	public final static BaseItemEconomy MUTTON = 				register(Items.MUTTON, 					0.14f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	public final static BaseItemEconomy BEEF = 					register(Items.BEEF, 					0.1f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	public final static BaseItemEconomy DRIED_KELP_BLOCK = 		register(Items.DRIED_KELP_BLOCK, 		0.1f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	public final static BaseItemEconomy SWEET_BERRIES = 		register(Items.SWEET_BERRIES, 			0.1f, 		1f,			0.001f, new ConstrainedGaussianRandom(32, 16, 64, 1),	ImmutableSet.of("butcher"));
	
	public final static BaseItemEconomy PAPER = 				register(Items.PAPER,		 			0.05f, 		1f,			0.001f, new ConstantValue<Integer>(64),					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy MAP = 					register(Items.MAP,		 				7f, 		1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy GLASS_PANE =			register(Items.GLASS_PANE,		 		11f, 		1f,			0.001f, new ConstrainedGaussianRandom(48, 8, 64, 1),	ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy COMPASS = 				register(Items.COMPASS,					13f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy EXPLORERS_MAP =			ItemCompundEconomy.register(Items.FILLED_MAP,  ImmutableSet.of(ImmutablePair.of(MAP, 2f), ImmutablePair.of(COMPASS,  2f)), new ConstantValue<Integer>(1), ImmutableSet.of("cartographer")).finalize(AmaziaFinalModifiers.EXPLORER_MAP_BUILDER);
	public final static BaseItemEconomy ItemFrame = 			register(Items.ITEM_FRAME,				7f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy BANNER = 				register(Items.BLACK_BANNER,			13f, 		1f,			0.001f, new ConstrainedGaussianRandom(8, 4, 16, 1),		ImmutableSet.of("cartographer")).modify(BANNER_COLOR_MODIFIER);
	public final static BaseItemEconomy BANNER_PATTERN =		register(Items.GLOBE_BANNER_PATTERN,	13f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	
	public final static BaseItemEconomy ROTTEN_FLESH =			register(Items.ROTTEN_FLESH,			0.01f, 		0.01f,		0.001f, new ConstantValue<Integer>(64), 				ImmutableSet.of("cleric"));
	public final static BaseItemEconomy REDSTONE =				register(Items.REDSTONE,				2f, 		0.01f,		0.001f, new ConstantValue<Integer>(64), 				ImmutableSet.of("cleric"));
	public final static BaseItemEconomy GOLD_INGOT =			register(Items.GOLD_INGOT,				0.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy LAPIS_LAZULI =			register(Items.LAPIS_LAZULI,			1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy RABBIT_FOOT =			register(Items.RABBIT_FOOT,				0.5f, 		0.1f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy GLOWSTONE =				register(Items.GLOWSTONE,				4f, 		0.1f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy SCUTE =					register(Items.SCUTE,					4f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy GLASS_BOTTLE =			register(Items.GLASS_BOTTLE,			0.1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy ENDER_PEARL =			register(Items.ENDER_PEARL,				5f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy NETHER_WART =			register(Items.NETHER_WART,				0.05f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy EXPERIENCE_BOTTLE =		register(Items.EXPERIENCE_BOTTLE,		4f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	
	public final static BaseItemEconomy WHEAT =					register(Items.WHEAT,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(20, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy POTATO =				register(Items.POTATO,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(26, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy CARROT =				register(Items.CARROT,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(22, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy BEETROOT =				register(Items.BEETROOT,				0.05f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(15, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy BREAD =					ItemCompundEconomy.register(Items.BREAD, ImmutableSet.of(ImmutablePair.of(WHEAT, 3f)), new ConstantValue<Integer>(1), 	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy PUMPKIN =				register(Items.PUMPKIN,					0.2f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(16, 2, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy PUMPKIN_PIE =			register(Items.PUMPKIN_PIE,				0.4f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(4,  2, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy APPLE =					register(Items.APPLE,					0.2f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(16, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy MELON =					register(Items.MELON,					0.2f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy COOKIE =				register(Items.COOKIE,					0.16f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy SUSPICIOUS_STEW =		register(Items.SUSPICIOUS_STEW,			1f, 		0.01f,		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("farmer")).finalize(AmaziaFinalModifiers.SUSPICOUS_STEW_FINALIZER);
	public final static BaseItemEconomy CAKE =					register(Items.CAKE,					1f, 		0.01f,		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("farmer"));
	public final static BaseItemEconomy GOLDEN_CARROT =			register(Items.GOLDEN_CARROT,			1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("farmer"));
	public final static BaseItemEconomy GLISTERING_MELON_SLICE= register(Items.GLISTERING_MELON_SLICE,	1.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("farmer"));
	
	public final static BaseItemEconomy STRING = 				register(Items.STRING,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman", "fletcher"));
	public final static BaseItemEconomy COD = 					register(Items.COD,						0.1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy SALMON = 				register(Items.SALMON,					0.1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy CAMPFIRE = 				register(Items.CAMPFIRE,				2f, 		0.01f,		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy FISCHING_ROD = 			register(Items.FISHING_ROD,				22f, 		1f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("fischerman")).modify(FISCHING_ROD_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy TROPICAL_FISH = 		register(Items.TROPICAL_FISH,			0.4f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy PUFFERFISH = 			register(Items.PUFFERFISH,				0.25f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy ACACIA_BOAT = 			register(Items.ACACIA_BOAT,				1f, 		0.01f,		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy COOKED_COD = 			ItemCompundEconomy.register(Items.COOKED_COD,    ImmutableSet.of(ImmutablePair.of(COD, 1f), ImmutablePair.of(COAL, 0.2f)), new ConstrainedGaussianRandom(32, 8, 64, 1), 	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy COOKED_SALMON = 		ItemCompundEconomy.register(Items.COOKED_SALMON, ImmutableSet.of(ImmutablePair.of(COD, 1f), ImmutablePair.of(COAL, 0.2f)), new ConstrainedGaussianRandom(32, 8, 64, 1), 	ImmutableSet.of("fischerman"));

	public final static BaseItemEconomy STICK = 				register(Items.STICK,					0.03f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy FLINT = 				register(Items.FLINT,					0.03f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy FEATHER = 				register(Items.FEATHER,					0.1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy BOW = 					register(Items.BOW,						2f, 		1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("fletcher")).modify(BOW_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy CROSSBOW = 				register(Items.CROSSBOW,				2f, 		1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("fletcher")).modify(CROSSBOW_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy TRIPWIRE_HOOK =			register(Items.TRIPWIRE_HOOK,			1.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy ARROW = 				register(Items.ARROW,					1.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy TIPPED_ARROW = 			register(Items.TIPPED_ARROW,			2f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher")).modify(ARROW_POTION_EFFECT_MODIFIER);
	
	
	
	
	public static void setup() { 
		AmaziaFinalModifiers.setup();
	}
	
	private static BaseItemEconomy register(Item itm, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory, Collection<String> professions) {
		return register(Amazia.MOD_ID, itm, baseValue, volatility, returnRate, stackSizeFactory, professions);
	}
	
	public static BaseItemEconomy register(String modid, Item itm, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory, Collection<String> professions) {
		ItemEconomy economy = new ItemEconomy(itm, baseValue, volatility, returnRate, stackSizeFactory);
		return register(modid, itm, economy, professions);
	}
	
	public static BaseItemEconomy register(String modid, Item itm, BaseItemEconomy economy, Collection<String> professions) {	
		Economy.addItem(economy);
		
		for (String profession : professions) {
			Economy.addProfessionItem(ProfessionFactory.buildProfession(modid, profession), itm.getTranslationKey());
		}
		return economy;
	}
}
