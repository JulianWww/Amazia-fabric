package net.denanu.amazia.economy;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.random.WeightedRandomCollection;

public class ProfessionFactory {
	private final static WeightedRandomCollection<String> professions = new WeightedRandomCollection<String>();
	
	public static void register(String modid, String name, float weight) {
		professions.add(weight, buildProfession(modid, name));
	}
	
	public static String buildProfession(String modid, String name) {
		return modid + "." + name;
	}
	
	public static String get() {
		return professions.next();
	}
	
	public static void setup() {
		register(Amazia.MOD_ID, "armorer", 1.0f);
		register(Amazia.MOD_ID, "butcher", 1.0f);
		register(Amazia.MOD_ID, "cartographer", 1.0f);
		register(Amazia.MOD_ID, "cleric", 1.0f);
		register(Amazia.MOD_ID, "farmer", 1.0f);
		register(Amazia.MOD_ID, "fischerman", 1.0f);
		register(Amazia.MOD_ID, "fletcher", 1.0f);
		register(Amazia.MOD_ID, "leatherworker", 1.0f);
		register(Amazia.MOD_ID, "librarian", 1.0f);
		register(Amazia.MOD_ID, "mason", 1.0f);
		register(Amazia.MOD_ID, "shepherd", 1.0f);
		register(Amazia.MOD_ID, "toolsmith", 1.0f);
		register(Amazia.MOD_ID, "weaponsmith", 1.0f);
	}
}

