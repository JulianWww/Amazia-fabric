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
import net.denanu.amazia.utils.random.ConstantValue;
import net.denanu.amazia.utils.random.ConstrainedGaussianRandom;
import net.denanu.amazia.utils.random.LinearRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;
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
	
	 																								 // value       volatility  return rate | stackSize generator							professions
	public final static BaseItemEconomy COAL = 					register(Items.COAL, 					0.0666f, 	0.0001f, 	0.01f,	new ConstrainedGaussianRandom(20f, 8f, 64, 1), 	ImmutableSet.of("armorer", "butcher"));
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
	public final static BaseItemEconomy COOKED_PORKCHOP = 		ItemCompundEconomy.register(Items.COOKED_PORKCHOP, ImmutableSet.of(ImmutablePair.of(COAL, 1/8f), ImmutablePair.of(PORKCHOP, 1f)), new ConstrainedGaussianRandom(32, 16, 64, 1), ImmutableSet.of("butcher"));
	public final static BaseItemEconomy COOKED_CHICKEN = 		ItemCompundEconomy.register(Items.COOKED_CHICKEN,  ImmutableSet.of(ImmutablePair.of(COAL, 1/8f), ImmutablePair.of(CHICKEN,  1f)), new ConstrainedGaussianRandom(32, 16, 64, 1), ImmutableSet.of("butcher"));
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
	public final static BaseItemEconomy BANNER = 				register(Items.BLACK_BANNER,			13f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	public final static BaseItemEconomy BANNER_PATTERN =		register(Items.GLOBE_BANNER_PATTERN,	13f, 		1f,			0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("cartographer"));
	
	
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
