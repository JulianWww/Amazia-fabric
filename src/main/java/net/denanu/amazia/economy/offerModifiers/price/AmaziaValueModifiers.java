package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;

public class AmaziaValueModifiers {
	public static void setup() {
		register(Enchantments.FIRE_PROTECTION, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.PROJECTILE_PROTECTION, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.BLAST_PROTECTION, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.PROTECTION, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.FEATHER_FALLING, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.UNBREAKING, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.THORNS, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.DEPTH_STRIDER, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.SOUL_SPEED, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.FROST_WALKER, new BoonModifierEconomy(5, 1, 0.999f));
		register(Enchantments.MENDING, new BoonModifierEconomy(32, 5, 0.999f));
		
		
		
		
		register(Enchantments.BINDING_CURSE, new DeficiteModifierEconomy(5, 1, 0.999f));
		register(Enchantments.VANISHING_CURSE, new DeficiteModifierEconomy(5, 1, 0.999f));
	}
			
			
	private static String register(Enchantment enchant, ModifierEconomy economy) {
		return register(enchant.getTranslationKey(), economy);
	}
	
	public static String register(String key, ModifierEconomy economy) {
		return Economy.registerModifier(key, economy);
	}
}
