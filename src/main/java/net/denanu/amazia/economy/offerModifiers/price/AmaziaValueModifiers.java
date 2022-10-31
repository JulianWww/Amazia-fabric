package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.minecraft.util.Identifier;

public class AmaziaValueModifiers {
	public static final String PROTECTION = register("enchantment.protection", new BoonModifierEconomy(5, 1, 0.999f));;
	
	
	public static void setup() {
	}
			
			
	private static String register(String name, ModifierEconomy economy) {
		return register(Amazia.MOD_ID, name, economy);
	}
	
	public static String register(String modid, String name, ModifierEconomy economy) {
		return Economy.registerModifier(modid + ":" + name, economy);
	}
}
