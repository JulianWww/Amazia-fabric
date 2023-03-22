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
import net.denanu.amazia.utils.random.ConstrainedGaussianIntRandom;
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

	public final static EnchantmentModifier SWORD_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.9f)
			.add(Enchantments.FIRE_ASPECT,				5.0f)
			.add(Enchantments.LOOTING,					1.0f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.SHARPNESS,				1.0f)
			.add(Enchantments.SMITE,					1.0f)
			.add(Enchantments.BANE_OF_ARTHROPODS,		1.0f)
			.add(Enchantments.KNOCKBACK,				1.0f)
			.add(Enchantments.SWEEPING,					1.0f)
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


	public final static RandomnessFactory<Integer> DIE_RANDOMNESS_FACTORY = new ConstrainedGaussianIntRandom(16, 4, 64, 1);

	// value       volatility  return rate | stackSize generator							professions
	public final static BaseItemEconomy COAL = 					EconomyFactory.register(Items.COAL, 					0.0666f, 	0.001f, 		0.01f,	new ConstrainedGaussianIntRandom(20f, 8f, 64,1),ImmutableSet.of("armorer", "butcher", "fischerman", "toolsmith", "weaponsmith")).addToVillage();
	public final static BaseItemEconomy IRON_HELMET = 			EconomyFactory.register(Items.IRON_HELMET, 				5, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.HELMET_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_CHESTPLATE = 		EconomyFactory.register(Items.IRON_CHESTPLATE, 			9, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.CHESTPLATE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_LEGGINS =			EconomyFactory.register(Items.IRON_LEGGINGS, 			7, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.LEGGINS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_BOOTS = 			EconomyFactory.register(Items.IRON_BOOTS, 				4, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.BOOTS_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_INGOT = 			EconomyFactory.register(Items.IRON_INGOT, 				0.25f,		0.001f, 	0.01f, 	new LinearRandom(1, 64), 						ImmutableSet.of("armorer", "toolsmith", "weaponsmith"));
	public final static BaseItemEconomy BELL = 					EconomyFactory.register(Items.BELL, 					36, 		2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer", "toolsmith", "weaponsmith"));
	public final static BaseItemEconomy LAVA_BUCKET =			EconomyFactory.register(Items.LAVA_BUCKET, 				1, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).addToVillage();
	public final static BaseItemEconomy DIAMOND = 				EconomyFactory.register(Items.DIAMOND, 					10,			0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16f, 4f, 32,1),ImmutableSet.of("armorer", "toolsmith", "weaponsmith")).addToVillage();
	public final static BaseItemEconomy CHAINMAIL_HELMET = 		EconomyFactory.register(Items.CHAINMAIL_HELMET,			1, 			2f, 		0.001f, new ConstantValue<>(1),	 						ImmutableSet.of("armorer")).modify(EconomyFactory.HELMET_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_CHESTPLATE = 	EconomyFactory.register(Items.CHAINMAIL_CHESTPLATE,		4, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.CHESTPLATE_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_LEGGINGS = 	EconomyFactory.register(Items.CHAINMAIL_LEGGINGS, 		3, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.LEGGINS_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy CHAINMAIL_BOOTS = 		EconomyFactory.register(Items.CHAINMAIL_BOOTS, 			1, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.BOOTS_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy SHIELD = 				EconomyFactory.register(Items.SHIELD, 					5, 			2f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.SHIELD_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_HELMET = 		EconomyFactory.register(Items.DIAMOND_HELMET, 			27, 		4f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.HELMET_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_CHESTPLATE = 	EconomyFactory.register(Items.DIAMOND_CHESTPLATE, 		35, 		4f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.CHESTPLATE_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_LEGGINGS = 		EconomyFactory.register(Items.DIAMOND_LEGGINGS, 		33, 		4f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.LEGGINS_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);
	public final static BaseItemEconomy DIAMOND_BOOTS = 		EconomyFactory.register(Items.DIAMOND_BOOTS, 			27, 		4f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("armorer")).modify(EconomyFactory.BOOTS_ENCHANTMENT_MODIFIER).modify(EconomyFactory.SELL_ONLY_MODIFIER);

	public final static BaseItemEconomy CHICKEN = 				EconomyFactory.register(Items.CHICKEN, 					0.07f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy PORKCHOP = 				EconomyFactory.register(Items.PORKCHOP, 				0.14f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy RABBIT = 				EconomyFactory.register(Items.RABBIT, 					0.25f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy RABBIT_STEW = 			EconomyFactory.register(Items.RABBIT_STEW, 				1, 			1f,			0.001f, new ConstantValue<>(1),							ImmutableSet.of("butcher")); // maybe make this a compund as well
	public final static BaseItemEconomy COOKED_PORKCHOP = 		ItemCompundEconomy.register(Items.COOKED_PORKCHOP, ImmutableSet.of(ImmutablePair.of(EconomyFactory.COAL, 0.2f), ImmutablePair.of(EconomyFactory.PORKCHOP, 1f)), new ConstrainedGaussianIntRandom(32, 16, 64, 1), ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy COOKED_CHICKEN = 		ItemCompundEconomy.register(Items.COOKED_CHICKEN,  ImmutableSet.of(ImmutablePair.of(EconomyFactory.COAL, 0.2f), ImmutablePair.of(EconomyFactory.CHICKEN,  1f)), new ConstrainedGaussianIntRandom(32, 16, 64, 1), ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy MUTTON = 				EconomyFactory.register(Items.MUTTON, 					0.14f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy BEEF = 					EconomyFactory.register(Items.BEEF, 					0.1f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy DRIED_KELP_BLOCK = 		EconomyFactory.register(Items.DRIED_KELP_BLOCK, 		0.1f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();
	public final static BaseItemEconomy SWEET_BERRIES = 		EconomyFactory.register(Items.SWEET_BERRIES, 			0.1f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(32, 16, 64, 1),ImmutableSet.of("butcher")).addToVillage();

	public final static BaseItemEconomy PAPER = 				EconomyFactory.register(Items.PAPER,		 			0.05f, 		1f,			0.001f, new ConstantValue<>(64),						ImmutableSet.of("cartographer", "librarian")).addToVillage();
	public final static BaseItemEconomy MAP = 					EconomyFactory.register(Items.MAP,		 				7f, 		1f,			0.001f, new ConstantValue<>(1),							ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy GLASS_PANE =			EconomyFactory.register(Items.GLASS_PANE,		 		11f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(48, 8, 64, 1),	ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy COMPASS = 				EconomyFactory.register(Items.COMPASS,					13f, 		1f,			0.001f, new ConstantValue<>(1), 						ImmutableSet.of("cartographer", "librarian"));
	public final static BaseItemEconomy EXPLORERS_MAP =			ItemCompundEconomy.register(Items.FILLED_MAP,  ImmutableSet.of(ImmutablePair.of(EconomyFactory.MAP, 2f), ImmutablePair.of(EconomyFactory.COMPASS,  2f)), new ConstantValue<>(1), ImmutableSet.of("cartographer")).finalize(AmaziaFinalModifiers.EXPLORER_MAP_BUILDER);
	public final static BaseItemEconomy ItemFrame = 			EconomyFactory.register(Items.ITEM_FRAME,				7f, 		1f,			0.001f, new ConstantValue<>(1), 						ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy BANNER = 				EconomyFactory.register(Items.BLACK_BANNER,				13f, 		1f,			0.001f, new ConstrainedGaussianIntRandom(8, 4, 16, 1),	ImmutableSet.of("cartographer", "shepherd")).modify(EconomyFactory.BANNER_COLOR_MODIFIER);
	public final static BaseItemEconomy BANNER_PATTERN =		EconomyFactory.register(Items.GLOBE_BANNER_PATTERN,		13f, 		1f,			0.001f, new ConstantValue<>(1), 						ImmutableSet.of("cartographer"));

	public final static BaseItemEconomy ROTTEN_FLESH =			EconomyFactory.register(Items.ROTTEN_FLESH,				0.01f, 		0.01f,		0.001f, new ConstantValue<>(64), 						ImmutableSet.of("cleric")).addToVillage();
	public final static BaseItemEconomy REDSTONE =				EconomyFactory.register(Items.REDSTONE,					2f, 		0.01f,		0.001f, new ConstantValue<>(64), 						ImmutableSet.of("cleric")).addToVillage();
	public final static BaseItemEconomy GOLD_INGOT =			EconomyFactory.register(Items.GOLD_INGOT,				0.3f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric")).addToVillage();
	public final static BaseItemEconomy LAPIS_LAZULI =			EconomyFactory.register(Items.LAPIS_LAZULI,				1f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric")).addToVillage();
	public final static BaseItemEconomy RABBIT_FOOT =			EconomyFactory.register(Items.RABBIT_FOOT,				0.5f, 		0.1f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric")).addToVillage();
	public final static BaseItemEconomy GLOWSTONE =				EconomyFactory.register(Items.GLOWSTONE,				4f, 		0.1f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy SCUTE =					EconomyFactory.register(Items.SCUTE,					4f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric", "leatherworker"));
	public final static BaseItemEconomy GLASS_BOTTLE =			EconomyFactory.register(Items.GLASS_BOTTLE,				0.1f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy ENDER_PEARL =			EconomyFactory.register(Items.ENDER_PEARL,				5f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy NETHER_WART =			EconomyFactory.register(Items.NETHER_WART,				0.05f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));
	public final static BaseItemEconomy EXPERIENCE_BOTTLE =		EconomyFactory.register(Items.EXPERIENCE_BOTTLE,		4f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("cleric"));

	public final static BaseItemEconomy WHEAT =					EconomyFactory.register(Items.WHEAT,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(20, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy POTATO =				EconomyFactory.register(Items.POTATO,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(26, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy CARROT =				EconomyFactory.register(Items.CARROT,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(22, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy BEETROOT =				EconomyFactory.register(Items.BEETROOT,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(15, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy BREAD =					ItemCompundEconomy.register(Items.BREAD, ImmutableSet.of(ImmutablePair.of(EconomyFactory.WHEAT, 3f)), new ConstantValue<>(1), 			ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy PUMPKIN =				EconomyFactory.register(Items.PUMPKIN,					0.2f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(16, 2, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy PUMPKIN_PIE =			EconomyFactory.register(Items.PUMPKIN_PIE,				0.4f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(4,  2, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy APPLE =					EconomyFactory.register(Items.APPLE,					0.2f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(16, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy MELON =					EconomyFactory.register(Items.MELON,					0.2f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy COOKIE =				EconomyFactory.register(Items.COOKIE,					0.16f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy SUSPICIOUS_STEW =		EconomyFactory.register(Items.SUSPICIOUS_STEW,			1f, 		0.01f,		0.001f, new ConstantValue<>(1),							ImmutableSet.of("farmer")).finalize(AmaziaFinalModifiers.SUSPICOUS_STEW_FINALIZER).addToVillage();
	public final static BaseItemEconomy CAKE =					EconomyFactory.register(Items.CAKE,						1f, 		0.01f,		0.001f, new ConstantValue<>(1),							ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy GOLDEN_CARROT =			EconomyFactory.register(Items.GOLDEN_CARROT,			1f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();
	public final static BaseItemEconomy GLISTERING_MELON_SLICE= EconomyFactory.register(Items.GLISTERING_MELON_SLICE,	1.3f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("farmer")).addToVillage();

	public final static BaseItemEconomy STRING = 				EconomyFactory.register(Items.STRING,					0.05f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman", "fletcher"));
	public final static BaseItemEconomy COD = 					EconomyFactory.register(Items.COD,						0.1f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy SALMON = 				EconomyFactory.register(Items.SALMON,					0.1f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy CAMPFIRE = 				EconomyFactory.register(Items.CAMPFIRE,					2f, 		0.01f,		0.001f, new ConstantValue<>(1),							ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy FISCHING_ROD = 			EconomyFactory.register(Items.FISHING_ROD,				22f, 		1f, 		0.001f, new ConstantValue<>(1), 						ImmutableSet.of("fischerman")).modify(EconomyFactory.FISCHING_ROD_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy TROPICAL_FISH = 		EconomyFactory.register(Items.TROPICAL_FISH,			0.4f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy PUFFERFISH = 			EconomyFactory.register(Items.PUFFERFISH,				0.25f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy ACACIA_BOAT = 			EconomyFactory.register(Items.ACACIA_BOAT,				1f, 		0.01f,		0.001f, new ConstantValue<>(1),							ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy COOKED_COD = 			ItemCompundEconomy.register(Items.COOKED_COD,    ImmutableSet.of(ImmutablePair.of(EconomyFactory.COD, 1f), ImmutablePair.of(EconomyFactory.COAL, 0.2f)), new ConstrainedGaussianIntRandom(32, 8, 64, 1), 	ImmutableSet.of("fischerman"));
	public final static BaseItemEconomy COOKED_SALMON = 		ItemCompundEconomy.register(Items.COOKED_SALMON, ImmutableSet.of(ImmutablePair.of(EconomyFactory.COD, 1f), ImmutablePair.of(EconomyFactory.COAL, 0.2f)), new ConstrainedGaussianIntRandom(32, 8, 64, 1), 	ImmutableSet.of("fischerman"));

	public final static BaseItemEconomy STICK = 				EconomyFactory.register(Items.STICK,					0.03f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher")).addToVillage();
	public final static BaseItemEconomy FLINT = 				EconomyFactory.register(Items.FLINT,					0.03f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher", "leatherworker", "toolsmith", "weaponsmith"));
	public final static BaseItemEconomy FEATHER = 				EconomyFactory.register(Items.FEATHER,					0.1f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy BOW = 					EconomyFactory.register(Items.BOW,						2f, 		1f,			0.001f, new ConstantValue<>(1),							ImmutableSet.of("fletcher")).modify(EconomyFactory.BOW_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy CROSSBOW = 				EconomyFactory.register(Items.CROSSBOW,					2f, 		1f,			0.001f, new ConstantValue<>(1),							ImmutableSet.of("fletcher")).modify(EconomyFactory.CROSSBOW_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy TRIPWIRE_HOOK =			EconomyFactory.register(Items.TRIPWIRE_HOOK,			1.3f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy ARROW = 				EconomyFactory.register(Items.ARROW,					1.3f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher"));
	public final static BaseItemEconomy TIPPED_ARROW = 			EconomyFactory.register(Items.TIPPED_ARROW,				2f, 		0.01f,		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("fletcher")).modify(EconomyFactory.ARROW_POTION_EFFECT_MODIFIER);

	public final static BaseItemEconomy LEATHER =				EconomyFactory.register(Items.LEATHER, 					0.1f,	 	0.01f, 		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("leatherworker")).addToVillage();
	public final static BaseItemEconomy RABBIT_HIDE =			EconomyFactory.register(Items.RABBIT_HIDE,				0.111f, 	0.01f, 		0.001f, new ConstrainedGaussianIntRandom(32, 8, 64, 1),	ImmutableSet.of("leatherworker")).addToVillage();
	public final static BaseItemEconomy SADDLE =				EconomyFactory.register(Items.SADDLE,					6f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("leatherworker"));
	public final static BaseItemEconomy LEATHER_HELMET =		EconomyFactory.register(Items.LEATHER_HELMET,			5f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("leatherworker")).modify(EconomyFactory.LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_CHESTPLATE =	EconomyFactory.register(Items.LEATHER_CHESTPLATE,		7f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("leatherworker")).modify(EconomyFactory.LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_LEGGINGS =		EconomyFactory.register(Items.LEATHER_LEGGINGS,			3f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("leatherworker")).modify(EconomyFactory.LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_BOOTS =			EconomyFactory.register(Items.LEATHER_BOOTS,			4f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("leatherworker")).modify(EconomyFactory.LEATHER_ARMOR_MODIFIER);
	public final static BaseItemEconomy LEATHER_HORSE_ARMOR =	EconomyFactory.register(Items.LEATHER_HORSE_ARMOR,		6f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("leatherworker")).modify(EconomyFactory.LEATHER_ARMOR_MODIFIER);

	public final static BaseItemEconomy ENCHANTED_BOOK =		EconomyFactory.register(Items.ENCHANTED_BOOK,			5f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("librarian")).count(3).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.ENCHANTED_BOOK_MODIFIER).addToVillage();
	public final static BaseItemEconomy BOOKSHELF =				EconomyFactory.register(Items.BOOKSHELF,				9f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy BOOK =					EconomyFactory.register(Items.BOOK,						4f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("librarian")).addToVillage();
	public final static BaseItemEconomy LANTERN =				EconomyFactory.register(Items.LANTERN,					1f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy GLASS =					EconomyFactory.register(Items.GLASS,					0.25f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy WRITABLE_BOOK =			EconomyFactory.register(Items.WRITABLE_BOOK,			1f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("librarian"));
	public final static BaseItemEconomy CLOCK =					EconomyFactory.register(Items.CLOCK,					5f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16, 4, 64, 1),	ImmutableSet.of("librarian"));
	public final static BaseItemEconomy NAME_TAG =				EconomyFactory.register(Items.NAME_TAG,					20f, 		0.01f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("librarian"));

	public final static BaseItemEconomy CLAY =					EconomyFactory.register(Items.CLAY,						0.1f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy BRICK =					EconomyFactory.register(Items.BRICK,					0.1f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy STONE =					EconomyFactory.register(Items.STONE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy DIORITE =				EconomyFactory.register(Items.DIORITE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy GRANITE =				EconomyFactory.register(Items.GRANITE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy ANDESITE =				EconomyFactory.register(Items.ANDESITE,					0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy POLISHED_ANDESITE =		EconomyFactory.register(Items.POLISHED_ANDESITE,		0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy POLISHED_DIORITE =		EconomyFactory.register(Items.POLISHED_DIORITE,			0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy POLISHED_GRANITE =		EconomyFactory.register(Items.POLISHED_GRANITE,			0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy DRIPSTONE_BLOCK =		EconomyFactory.register(Items.DRIPSTONE_BLOCK,			0.05f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy CHISELED_STONE_BRICKS =	EconomyFactory.register(Items.CHISELED_STONE_BRICKS,	0.25f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(48, 4, 64, 1),	ImmutableSet.of("mason"));
	public final static BaseItemEconomy QUARTZ =				EconomyFactory.register(Items.QUARTZ,					12f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16, 4, 64, 1),	ImmutableSet.of("mason")).modify(EconomyFactory.BUY_ONLY_MODIFIER);
	public final static BaseItemEconomy TERRACOTTA =			EconomyFactory.register(Items.TERRACOTTA,				1f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(32, 4, 64, 1),	ImmutableSet.of("mason")).modify(EconomyFactory.TERRACOTTA_COLOR_MODIFIER);
	public final static BaseItemEconomy QUARTZ_PILLAR =			EconomyFactory.register(Items.QUARTZ_PILLAR,			1f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(16, 4, 64, 1),	ImmutableSet.of("mason")).modify(EconomyFactory.SELL_ONLY_MODIFIER);

	public final static BaseItemEconomy WOOL =					EconomyFactory.register(Items.BLACK_WOOL,				1f, 		0.01f, 		0.001f, new ConstrainedGaussianIntRandom(32, 4, 64, 1),	ImmutableSet.of("shepherd")).modify(EconomyFactory.WOOL_COLOR_MODIFIER).addToVillage();
	public final static BaseItemEconomy CARPET =				EconomyFactory.register(Items.BLACK_CARPET,				2f, 		1f, 		0.001f, new ConstrainedGaussianIntRandom(32, 4, 64, 1),	ImmutableSet.of("shepherd")).modify(EconomyFactory.CARPET_COLOR_MODIFIER);
	public final static BaseItemEconomy BED =					EconomyFactory.register(Items.BLACK_BED,				2f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("shepherd")).modify(EconomyFactory.BED_COLOR_MODIFIER);
	public final static BaseItemEconomy SHEARS =				EconomyFactory.register(Items.SHEARS,					2f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("shepherd")).addToVillage();
	public final static BaseItemEconomy BLACK_DYE =				EconomyFactory.register(Items.BLACK_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy BLUE_DYE =				EconomyFactory.register(Items.BLUE_DYE,					0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy BROWN_DYE =				EconomyFactory.register(Items.BROWN_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy CYAN_DYE =				EconomyFactory.register(Items.CYAN_DYE,					0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy GRAY_DYE =				EconomyFactory.register(Items.GRAY_DYE,					0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy GREEN_DYE =				EconomyFactory.register(Items.GREEN_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy LIGHT_BLUE_DYE =		EconomyFactory.register(Items.LIGHT_BLUE_DYE,			0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy LIGHT_GRAY_DYE =		EconomyFactory.register(Items.LIGHT_GRAY_DYE,			0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy LIME_DYE =				EconomyFactory.register(Items.LIME_DYE,					0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy MAGENTA_DYE =			EconomyFactory.register(Items.MAGENTA_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy ORANGE_DYE =			EconomyFactory.register(Items.ORANGE_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy PINK_DYE =				EconomyFactory.register(Items.PINK_DYE,					0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy PURPLE_DYE =			EconomyFactory.register(Items.PURPLE_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy RED_DYE =				EconomyFactory.register(Items.RED_DYE,					0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy WHITE_DYE =				EconomyFactory.register(Items.WHITE_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));
	public final static BaseItemEconomy YELLOW_DYE =			EconomyFactory.register(Items.YELLOW_DYE,				0.2f, 		0.01f, 		0.001f, EconomyFactory.DIE_RANDOMNESS_FACTORY,			ImmutableSet.of("shepherd"));

	public final static BaseItemEconomy STONE_AXE =				EconomyFactory.register(Items.STONE_AXE,				1f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.AXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy STONE_PICKAXE =			EconomyFactory.register(Items.STONE_PICKAXE,			1f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.PICKAXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy STONE_SHOVEL =			EconomyFactory.register(Items.STONE_SHOVEL,				1f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.SHOVEL_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy STONE_HOE =				EconomyFactory.register(Items.STONE_HOE,				1f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.HOE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_AXE =				EconomyFactory.register(Items.IRON_AXE,					20f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith", "weaponsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.AXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_PICKAXE =			EconomyFactory.register(Items.IRON_PICKAXE,				20f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.PICKAXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_SHOVEL =			EconomyFactory.register(Items.IRON_SHOVEL,				20f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.SHOVEL_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_HOE =				EconomyFactory.register(Items.IRON_HOE,					20f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.HOE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy IRON_SWORD =			EconomyFactory.register(Items.IRON_SWORD,				20f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("weaponsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.SWORD_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_AXE =			EconomyFactory.register(Items.DIAMOND_AXE,				40f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith", "weaponsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.AXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_PICKAXE =		EconomyFactory.register(Items.DIAMOND_PICKAXE,			40f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.PICKAXE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_SHOVEL =		EconomyFactory.register(Items.DIAMOND_SHOVEL,			40f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.SHOVEL_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_HOE =			EconomyFactory.register(Items.DIAMOND_HOE,				40f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("toolsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.HOE_ENCHANTMENT_MODIFIER);
	public final static BaseItemEconomy DIAMOND_SWORD =			EconomyFactory.register(Items.DIAMOND_SWORD,			40f, 		1f, 		0.001f, new ConstantValue<>(1),							ImmutableSet.of("weaponsmith")).modify(EconomyFactory.SELL_ONLY_MODIFIER).modify(EconomyFactory.SWORD_ENCHANTMENT_MODIFIER);





	public static void setup() {
		AmaziaFinalModifiers.setup();
	}

	private static BaseItemEconomy register(final Item itm, final float baseValue, final float volatility, final float returnRate, final RandomnessFactory<Integer> stackSizeFactory, final Collection<String> professions) {
		return EconomyFactory.register(Amazia.MOD_ID, itm, baseValue, volatility, returnRate, stackSizeFactory, professions);
	}

	public static BaseItemEconomy register(final String modid, final Item itm, final float baseValue, final float volatility, final float returnRate, final RandomnessFactory<Integer> stackSizeFactory, final Collection<String> professions) {
		final ItemEconomy economy = new ItemEconomy(itm, baseValue, volatility, returnRate, stackSizeFactory);
		return EconomyFactory.register(modid, itm, economy, professions);
	}

	public static BaseItemEconomy register(final String modid, final Item itm, final BaseItemEconomy economy, final Collection<String> professions) {
		Economy.addItem(economy);

		for (final String profession : professions) {
			Economy.addProfessionItem(ProfessionFactory.buildProfession(modid, profession), itm.getTranslationKey());
		}
		return economy;
	}
}
