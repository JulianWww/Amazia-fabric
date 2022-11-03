package net.denanu.amazia.economy;

import java.util.Collection;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.itemEconomy.BaseItemEconomy;
import net.denanu.amazia.economy.itemEconomy.ItemCompundEconomy;
import net.denanu.amazia.economy.itemEconomy.ItemEconomy;
import net.denanu.amazia.economy.offerModifiers.finalizers.AmaziaFinalModifiers;
import net.denanu.amazia.economy.offerModifiers.item.BuyModifier;
import net.denanu.amazia.economy.offerModifiers.item.DieItemModifier;
import net.denanu.amazia.economy.offerModifiers.item.EnchantmentModifier;
import net.denanu.amazia.economy.offerModifiers.item.ItemReselector;
import net.denanu.amazia.economy.offerModifiers.item.PotionEffectModifier;
import net.denanu.amazia.utils.random.ConstantValue;
import net.denanu.amazia.utils.random.ConstrainedGaussianRandom;
import net.denanu.amazia.utils.random.LinearRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class EconomyFactory {
	public final static BuyModifier BUY_ONLY_MODIFIER = new BuyModifier(true);
	public final static BuyModifier SELL_ONLY_MODIFIER = new BuyModifier(false);
	
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
	
	public final static EnchantmentModifier AXE_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.9f)
			.add(Enchantments.FORTUNE,					1.0f)
			.add(Enchantments.SILK_TOUCH,				1.0f)
			.add(Enchantments.EFFICIENCY,				1.0f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.SHARPNESS,				1.0f)
			.add(Enchantments.SMITE,					1.0f)
			.add(Enchantments.BANE_OF_ARTHROPODS,		1.0f)
			.add(Enchantments.FIRE_ASPECT,				1.0f)
			.add(Enchantments.LOOTING,					1.0f)
			.add(Enchantments.KNOCKBACK,				1.0f)
			.add(Enchantments.SWEEPING,					1.0f)
			.add(Enchantments.MENDING,					0.001f)
			.add(Enchantments.VANISHING_CURSE,			0.2f);
	
	public final static EnchantmentModifier PICKAXE_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.9f)
			.add(Enchantments.EFFICIENCY,				5.0f)
			.add(Enchantments.SILK_TOUCH,				1.0f)
			.add(Enchantments.FORTUNE,					1.0f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.MENDING,					0.001f)
			.add(Enchantments.VANISHING_CURSE,			0.2f);
	
	public final static EnchantmentModifier SHOVEL_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.9f)
			.add(Enchantments.EFFICIENCY,				5.0f)
			.add(Enchantments.SILK_TOUCH,				1.0f)
			.add(Enchantments.FORTUNE,					1.0f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.MENDING,					0.001f)
			.add(Enchantments.VANISHING_CURSE,			0.2f);
	
	public final static EnchantmentModifier HOE_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.9f)
			.add(Enchantments.EFFICIENCY,				5.0f)
			.add(Enchantments.SILK_TOUCH,				1.0f)
			.add(Enchantments.FORTUNE,					1.0f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.MENDING,					0.001f)
			.add(Enchantments.VANISHING_CURSE,			0.2f);
	
	public final static EnchantmentModifier ENCHANTED_BOOK_MODIFIER = new EnchantmentModifier(0.3f, 1.0f)
			.add(Enchantments.PROTECTION, 				1.0f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.FEATHER_FALLING, 			1.0f)
			.add(Enchantments.BLAST_PROTECTION, 		1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION,	1.0f)
			.add(Enchantments.RESPIRATION, 				1.0f)
			.add(Enchantments.AQUA_AFFINITY, 			1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.DEPTH_STRIDER, 			1.0f)
			.add(Enchantments.FROST_WALKER, 			1.0f)
			.add(Enchantments.BINDING_CURSE, 			1.0f)
			.add(Enchantments.SOUL_SPEED, 				1.0f)
			.add(Enchantments.SWIFT_SNEAK, 				1.0f)
			.add(Enchantments.SHARPNESS,				1.0f)
			.add(Enchantments.SMITE,					1.0f)
			.add(Enchantments.BANE_OF_ARTHROPODS,		1.0f)
			.add(Enchantments.KNOCKBACK,				1.0f)
			.add(Enchantments.FIRE_ASPECT,				1.0f)
			.add(Enchantments.LOOTING,					1.0f)
			.add(Enchantments.SWEEPING,					1.0f)
			.add(Enchantments.EFFICIENCY,				1.0f)
			.add(Enchantments.SILK_TOUCH,				1.0f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.FORTUNE,					1.0f)
			.add(Enchantments.POWER,					1.0f)
			.add(Enchantments.PUNCH,					1.0f)
			.add(Enchantments.FLAME,					1.0f)
			.add(Enchantments.INFINITY,					1.0f)
			.add(Enchantments.LUCK_OF_THE_SEA,			1.0f)
			.add(Enchantments.LURE,						1.0f)
			.add(Enchantments.LOYALTY,					1.0f)
			.add(Enchantments.IMPALING,					1.0f)
			.add(Enchantments.RIPTIDE,					1.0f)
			.add(Enchantments.CHANNELING,				1.0f)
			.add(Enchantments.MULTISHOT,				1.0f)
			.add(Enchantments.QUICK_CHARGE,				1.0f)
			.add(Enchantments.PIERCING,					1.0f)
			.add(Enchantments.MENDING,					1.0f)
			.add(Enchantments.VANISHING_CURSE, 			1.0f);
	
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
			.add(Items.YELLOW_BANNER, 		1.0f);
	
	public final static ItemReselector WOOL_COLOR_MODIFIER = new ItemReselector()
			.add(Items.BLACK_WOOL, 	   10.0f)
			.add(Items.BLUE_WOOL, 		1.0f)
			.add(Items.BROWN_WOOL, 		1.0f)
			.add(Items.CYAN_WOOL, 		1.0f)
			.add(Items.GRAY_WOOL, 		1.0f)
			.add(Items.GREEN_WOOL, 		1.0f)
			.add(Items.LIGHT_BLUE_WOOL, 1.0f)
			.add(Items.LIGHT_GRAY_WOOL, 1.0f)
			.add(Items.LIME_WOOL, 		1.0f)
			.add(Items.MAGENTA_WOOL, 	1.0f)
			.add(Items.ORANGE_WOOL, 	1.0f)
			.add(Items.PINK_WOOL, 		1.0f)
			.add(Items.PURPLE_WOOL, 	1.0f)
			.add(Items.RED_WOOL, 		1.0f)
			.add(Items.WHITE_WOOL, 	   20.0f)
			.add(Items.YELLOW_WOOL, 	1.0f);
	
	public final static ItemReselector CARPET_COLOR_MODIFIER = new ItemReselector()
			.add(Items.BLACK_CARPET, 	   10.0f)
			.add(Items.BLUE_CARPET, 		1.0f)
			.add(Items.BROWN_CARPET, 		1.0f)
			.add(Items.CYAN_CARPET, 		1.0f)
			.add(Items.GRAY_CARPET, 		1.0f)
			.add(Items.GREEN_CARPET, 		1.0f)
			.add(Items.LIGHT_BLUE_CARPET, 	1.0f)
			.add(Items.LIGHT_GRAY_CARPET, 	1.0f)
			.add(Items.LIME_CARPET, 		1.0f)
			.add(Items.MAGENTA_CARPET, 		1.0f)
			.add(Items.ORANGE_CARPET, 		1.0f)
			.add(Items.PINK_CARPET, 		1.0f)
			.add(Items.PURPLE_CARPET, 		1.0f)
			.add(Items.RED_CARPET, 			1.0f)
			.add(Items.WHITE_CARPET, 	   20.0f)
			.add(Items.YELLOW_CARPET, 		1.0f);
	
	public final static ItemReselector BED_COLOR_MODIFIER = new ItemReselector()
			.add(Items.BLACK_BED, 	   10.0f)
			.add(Items.BLUE_BED, 		1.0f)
			.add(Items.BROWN_BED, 		1.0f)
			.add(Items.CYAN_BED, 		1.0f)
			.add(Items.GRAY_BED, 		1.0f)
			.add(Items.GREEN_BED, 		1.0f)
			.add(Items.LIGHT_BLUE_BED, 1.0f)
			.add(Items.LIGHT_GRAY_BED, 1.0f)
			.add(Items.LIME_BED, 		1.0f)
			.add(Items.MAGENTA_BED, 	1.0f)
			.add(Items.ORANGE_BED, 	1.0f)
			.add(Items.PINK_BED, 		1.0f)
			.add(Items.PURPLE_BED, 	1.0f)
			.add(Items.RED_BED, 		1.0f)
			.add(Items.WHITE_BED, 	   20.0f)
			.add(Items.YELLOW_BED, 	1.0f);
	
	public final static ItemReselector TERRACOTTA_COLOR_MODIFIER = new ItemReselector()
			.add(Items.BLACK_TERRACOTTA, 			1.0f)
			.add(Items.BLUE_TERRACOTTA, 			1.0f)
			.add(Items.BROWN_TERRACOTTA,	 		1.0f)
			.add(Items.CYAN_TERRACOTTA, 			1.0f)
			.add(Items.GRAY_TERRACOTTA, 			1.0f)
			.add(Items.GREEN_TERRACOTTA, 			1.0f)
			.add(Items.LIGHT_BLUE_TERRACOTTA, 		1.0f)
			.add(Items.LIGHT_GRAY_TERRACOTTA, 		1.0f)
			.add(Items.LIME_TERRACOTTA, 			1.0f)
			.add(Items.MAGENTA_TERRACOTTA, 			1.0f)
			.add(Items.ORANGE_TERRACOTTA, 			1.0f)
			.add(Items.PINK_TERRACOTTA, 			1.0f)
			.add(Items.PURPLE_TERRACOTTA, 			1.0f)
			.add(Items.RED_TERRACOTTA, 				1.0f)
			.add(Items.WHITE_TERRACOTTA, 	    	1.0f)
			.add(Items.YELLOW_TERRACOTTA, 			1.0f)
			.add(Items.TERRACOTTA,			  	   20.0f)
			.add(Items.BLACK_GLAZED_TERRACOTTA, 	1.0f)
			.add(Items.BLUE_GLAZED_TERRACOTTA, 		1.0f)
			.add(Items.BROWN_GLAZED_TERRACOTTA,	 	1.0f)
			.add(Items.CYAN_GLAZED_TERRACOTTA, 		1.0f)
			.add(Items.GRAY_GLAZED_TERRACOTTA, 		1.0f)
			.add(Items.GREEN_GLAZED_TERRACOTTA, 	1.0f)
			.add(Items.LIGHT_BLUE_GLAZED_TERRACOTTA,1.0f)
			.add(Items.LIGHT_GRAY_GLAZED_TERRACOTTA,1.0f)
			.add(Items.LIME_GLAZED_TERRACOTTA, 		1.0f)
			.add(Items.MAGENTA_GLAZED_TERRACOTTA, 	1.0f)
			.add(Items.ORANGE_GLAZED_TERRACOTTA, 	1.0f)
			.add(Items.PINK_GLAZED_TERRACOTTA, 		1.0f)
			.add(Items.PURPLE_GLAZED_TERRACOTTA, 	1.0f)
			.add(Items.RED_GLAZED_TERRACOTTA, 		1.0f)
			.add(Items.WHITE_GLAZED_TERRACOTTA, 	1.0f)
			.add(Items.YELLOW_GLAZED_TERRACOTTA, 	1.0f);

	
	public final static PotionEffectModifier ARROW_POTION_EFFECT_MODIFIER = new PotionEffectModifier();
	public final static DieItemModifier LEATHER_ARMOR_MODIFIER = new DieItemModifier();
	
	
	public final static RandomnessFactory<Integer> DIE_RANDOMNESS_FACTORY = new ConstrainedGaussianRandom(16, 4, 64, 1);
	
	 																								 // value       volatility  return rate | stackSize generator							professions
	public final static BaseItemEconomy COAL = 					register(Items.COAL, 					0.0666f, 	0.0001f, 	0.01f,	new ConstrainedGaussianRandom(20f, 8f, 64, 1), 	ImmutableSet.of("armorer", "butcher", "fischerman", "toolsmith"));
	public final static BaseItemEconomy IRON_HELMET = 			register(Items.IRON_HELMET, 			5, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_CHESTPLATE = 		register(Items.IRON_CHESTPLATE, 		9, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_LEGGINS =			register(Items.IRON_LEGGINGS, 			7, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_BOOTS = 			register(Items.IRON_BOOTS, 				4, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_INGOT = 			register(Items.IRON_INGOT, 				0.25f,		0.001f, 	0.01f, 	new LinearRandom(1, 64), 						ImmutableSet.of("armorer", "toolsmith"));
	public final static BaseItemEconomy BELL = 					register(Items.BELL, 					36, 		2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer", "toolsmith"));
	public final static BaseItemEconomy LAVA_BUCKET =			register(Items.LAVA_BUCKET, 			1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer"));
	public final static BaseItemEconomy DIAMOND = 				register(Items.DIAMOND, 				10,			0.01f, 		0.001f, new ConstrainedGaussianRandom(16f, 4f, 32, 1), 	ImmutableSet.of("armorer", "toolsmith"));
	public final static BaseItemEconomy CHAINMAIL_HELMET = 		register(Items.CHAINMAIL_HELMET,		1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_CHESTPLATE = 	register(Items.CHAINMAIL_CHESTPLATE,	4, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_LEGGINGS = 	register(Items.CHAINMAIL_LEGGINGS, 		3, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_BOOTS = 		register(Items.CHAINMAIL_BOOTS, 		1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy SHIELD = 				register(Items.SHIELD, 					5, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(SHIELD_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_HELMET = 		register(Items.DIAMOND_HELMET, 			27, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_CHESTPLATE = 	register(Items.DIAMOND_CHESTPLATE, 		35, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_LEGGINGS = 		register(Items.DIAMOND_LEGGINGS, 		33, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_BOOTS = 		register(Items.DIAMOND_BOOTS, 			27, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER).modify(SELL_ONLY_MODIFIER);
	
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
	
	public final static BaseItemEconomy PAPER = 				register(Items.PAPER,		 			0.05f, 		1f,			0.001f, new ConstantValue<Integer>(64),					ImmutableSet.of("cartographer", "librarian"));
	public final static BaseItemEconomy MAP = 					register(Items.MAP,		 				7f, 		1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy GLASS_PANE =			register(Items.GLASS_PANE,		 		11f, 		1f,			0.001f, new ConstrainedGaussianRandom(48, 8, 64, 1),	ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy COMPASS = 				register(Items.COMPASS,					13f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer", "librarian"));
	public final static BaseItemEconomy EXPLORERS_MAP =			ItemCompundEconomy.register(Items.FILLED_MAP,  ImmutableSet.of(ImmutablePair.of(MAP, 2f), ImmutablePair.of(COMPASS,  2f)), new ConstantValue<Integer>(1), ImmutableSet.of("cartographer")).finalize(AmaziaFinalModifiers.EXPLORER_MAP_BUILDER);
	public final static BaseItemEconomy ItemFrame = 			register(Items.ITEM_FRAME,				7f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy BANNER = 				register(Items.BLACK_BANNER,			13f, 		1f,			0.001f, new ConstrainedGaussianRandom(8, 4, 16, 1),		ImmutableSet.of("cartographer", "shepherd")).modify(BANNER_COLOR_MODIFIER);
	public final static BaseItemEconomy BANNER_PATTERN =		register(Items.GLOBE_BANNER_PATTERN,	13f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	
	public final static BaseItemEconomy ROTTEN_FLESH =			register(Items.ROTTEN_FLESH,			0.01f, 		0.01f,		0.001f, new ConstantValue<Integer>(64), 				ImmutableSet.of("cleric"));
	public final static BaseItemEconomy REDSTONE =				register(Items.REDSTONE,				2f, 		0.01f,		0.001f, new ConstantValue<Integer>(64), 				ImmutableSet.of("cleric"));
	public final static BaseItemEconomy GOLD_INGOT =			register(Items.GOLD_INGOT,				0.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy LAPIS_LAZULI =			register(Items.LAPIS_LAZULI,			1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy RABBIT_FOOT =			register(Items.RABBIT_FOOT,				0.5f, 		0.1f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy GLOWSTONE =				register(Items.GLOWSTONE,				4f, 		0.1f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy SCUTE =					register(Items.SCUTE,					4f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("cleric", "leatherworker"));
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
	public final static BaseItemEconomy FLINT = 				register(Items.FLINT,					0.03f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher", "leatherworker", "toolsmith"));
	public final static BaseItemEconomy FEATHER = 				register(Items.FEATHER,					0.1f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy BOW = 					register(Items.BOW,						2f, 		1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("fletcher")).modify(BOW_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy CROSSBOW = 				register(Items.CROSSBOW,				2f, 		1f,			0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("fletcher")).modify(CROSSBOW_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy TRIPWIRE_HOOK =			register(Items.TRIPWIRE_HOOK,			1.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy ARROW = 				register(Items.ARROW,					1.3f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy TIPPED_ARROW = 			register(Items.TIPPED_ARROW,			2f, 		0.01f,		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher")).modify(ARROW_POTION_EFFECT_MODIFIER);
	
	public final static BaseItemEconomy LEATHER =				register(Items.LEATHER, 				0.1f,	 	0.01f, 		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("leatherworker"));
	public final static BaseItemEconomy RABBIT_HIDE =			register(Items.RABBIT_HIDE,				0.111f, 	0.01f, 		0.001f, new ConstrainedGaussianRandom(32, 8, 64, 1),	ImmutableSet.of("leatherworker"));
	public final static BaseItemEconomy SADDLE =				register(Items.SADDLE,					6f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("leatherworker"));
	public final static BaseItemEconomy LEATHER_HELMET =		register(Items.LEATHER_HELMET,			5f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("leatherworker")).modify(LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_CHESTPLATE =	register(Items.LEATHER_CHESTPLATE,		7f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("leatherworker")).modify(LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_LEGGINGS =		register(Items.LEATHER_LEGGINGS,		3f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("leatherworker")).modify(LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_BOOTS =			register(Items.LEATHER_BOOTS,			4f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("leatherworker")).modify(LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_HORSE_ARMOR =	register(Items.LEATHER_HORSE_ARMOR,		6f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("leatherworker")).modify(LEATHER_ARMOR_MODIFIER);
	
	public final static BaseItemEconomy ENCHANTED_BOOK =		register(Items.ENCHANTED_BOOK,			5f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("librarian")).count(3).modify(SELL_ONLY_MODIFIER).modify(ENCHANTED_BOOK_MODIFIER);
	public final static BaseItemEconomy BOOKSHELF =				register(Items.BOOKSHELF,				9f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy BOOK =					register(Items.BOOK,					4f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("librarian"));
	public final static BaseItemEconomy LANTERN =				register(Items.LANTERN,					1f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy GLASS =					register(Items.GLASS,					0.25f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy WRITABLE_BOOK =			register(Items.WRITABLE_BOOK,			1f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("librarian"));
	public final static BaseItemEconomy CLOCK =					register(Items.CLOCK,					5f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy NAME_TAG =				register(Items.NAME_TAG,				20f, 		0.01f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("librarian"));
	
	public final static BaseItemEconomy CLAY =					register(Items.CLAY,					0.1f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy BRICK =					register(Items.BRICK,					0.1f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy STONE =					register(Items.STONE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy DIORITE =				register(Items.DIORITE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy GRANITE =				register(Items.GRANITE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy ANDESITE =				register(Items.ANDESITE,				0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy POLISHED_ANDESITE =		register(Items.POLISHED_ANDESITE,		0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy POLISHED_DIORITE =		register(Items.POLISHED_DIORITE,		0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy POLISHED_GRANITE =		register(Items.POLISHED_GRANITE,		0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy DRIPSTONE_BLOCK =		register(Items.DRIPSTONE_BLOCK,			0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy CHISELED_STONE_BRICKS =	register(Items.CHISELED_STONE_BRICKS,	0.25f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy QUARTZ =				register(Items.QUARTZ,					12f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(16, 4, 64, 1),	ImmutableSet.of("mason")).modify(BUY_ONLY_MODIFIER);
	public final static BaseItemEconomy TERRACOTTA =			register(Items.TERRACOTTA,				1f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(32, 4, 64, 1),	ImmutableSet.of("mason")).modify(TERRACOTTA_COLOR_MODIFIER);
	public final static BaseItemEconomy QUARTZ_PILLAR =			register(Items.QUARTZ_PILLAR,			1f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(16, 4, 64, 1),	ImmutableSet.of("mason")).modify(SELL_ONLY_MODIFIER);
	
	public final static BaseItemEconomy WOOL =					register(Items.BLACK_WOOL,				1f, 		0.01f, 		0.001f, new ConstrainedGaussianRandom(32, 4, 64, 1),	ImmutableSet.of("shepherd")).modify(WOOL_COLOR_MODIFIER);
	public final static BaseItemEconomy CARPET =				register(Items.BLACK_CARPET,			2f, 		1f, 		0.001f, new ConstrainedGaussianRandom(32, 4, 64, 1),	ImmutableSet.of("shepherd")).modify(CARPET_COLOR_MODIFIER);
	public final static BaseItemEconomy BED =					register(Items.BLACK_BED,				2f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("shepherd")).modify(BED_COLOR_MODIFIER);
	public final static BaseItemEconomy SHEARS =				register(Items.SHEARS,					2f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy BLACK_DYE =				register(Items.BLACK_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy BLUE_DYE =				register(Items.BLUE_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy BROWN_DYE =				register(Items.BROWN_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy CYAN_DYE =				register(Items.CYAN_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy GRAY_DYE =				register(Items.GRAY_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy GREEN_DYE =				register(Items.GREEN_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy LIGHT_BLUE_DYE =		register(Items.LIGHT_BLUE_DYE,			0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy LIGHT_GRAY_DYE =		register(Items.LIGHT_GRAY_DYE,			0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy LIME_DYE =				register(Items.LIME_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy MAGENTA_DYE =			register(Items.MAGENTA_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy ORANGE_DYE =			register(Items.ORANGE_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy PINK_DYE =				register(Items.PINK_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy PURPLE_DYE =			register(Items.PURPLE_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy RED_DYE =				register(Items.RED_DYE,					0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy WHITE_DYE =				register(Items.WHITE_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy YELLOW_DYE =			register(Items.YELLOW_DYE,				0.2f, 		0.01f, 		0.001f, DIE_RANDOMNESS_FACTORY,							ImmutableSet.of("shepherd"));
	
	public final static BaseItemEconomy STONE_AXE =				register(Items.STONE_AXE,				1f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(AXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy STONE_PICKAXE =			register(Items.STONE_PICKAXE,			1f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(PICKAXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy STONE_SHOVEL =			register(Items.STONE_SHOVEL,			1f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(SHOVEL_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy STONE_HOE =				register(Items.STONE_HOE,				1f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(HOE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_AXE =				register(Items.IRON_AXE,				20f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(AXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_PICKAXE =			register(Items.IRON_PICKAXE,			20f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(PICKAXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_SHOVEL =			register(Items.IRON_SHOVEL,				20f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(SHOVEL_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_HOE =				register(Items.IRON_HOE,				20f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(HOE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_AXE =			register(Items.DIAMOND_AXE,				40f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(AXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_PICKAXE =		register(Items.DIAMOND_PICKAXE,			40f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(PICKAXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_SHOVEL =		register(Items.DIAMOND_SHOVEL,			40f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(SHOVEL_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_HOE =			register(Items.DIAMOND_HOE,				40f, 		1f, 		0.001f, new ConstantValue<Integer>(1),					ImmutableSet.of("toolsmith")).modify(SELL_ONLY_MODIFIER).modify(HOE_ENCHANTMENT_MODIFIER);
	
	
	
	
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
