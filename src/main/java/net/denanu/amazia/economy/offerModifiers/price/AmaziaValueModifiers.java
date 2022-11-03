package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;

public class AmaziaValueModifiers {
	public static void setup() {
		register(new BoonEnchantmentModifierEconomy(Enchantments.PROTECTION, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.FIRE_PROTECTION, 			5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.FEATHER_FALLING, 			5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.BLAST_PROTECTION, 			5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.PROJECTILE_PROTECTION, 	5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.RESPIRATION, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.AQUA_AFFINITY, 			5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.THORNS, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.DEPTH_STRIDER, 			5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.FROST_WALKER, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.SOUL_SPEED, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.SWIFT_SNEAK, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.SHARPNESS, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.SMITE, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.BANE_OF_ARTHROPODS, 		5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.KNOCKBACK, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.FIRE_ASPECT, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.LOOTING, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.SWEEPING, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.EFFICIENCY, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.SILK_TOUCH, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.UNBREAKING, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.FORTUNE, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.POWER, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.PUNCH, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.FLAME, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.INFINITY, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.LUCK_OF_THE_SEA, 			5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.LURE, 						5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.LOYALTY, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.IMPALING, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.RIPTIDE, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.CHANNELING, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.MULTISHOT, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.QUICK_CHARGE, 				5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.PIERCING, 					5,  1, 0.001f));
		register(new BoonEnchantmentModifierEconomy(Enchantments.MENDING, 					5,  1, 0.001f));
		
		register(new DeficiteEnchantmentModifierEconomy(Enchantments.BINDING_CURSE, 		5,  1, 0.001f));
		register(new DeficiteEnchantmentModifierEconomy(Enchantments.VANISHING_CURSE, 		5,  1, 0.001f));
	}
	
	private static String register(EnchantmentValueModifier mod) {
		return register(mod.getEnchant(), mod);
	}
			
	private static String register(Enchantment enchant, ModifierEconomy economy) {
		return register(enchant.getTranslationKey(), economy);
	}
	
	public static String register(String key, ModifierEconomy economy) {
		return Economy.registerModifier(key, economy);
	}
}
