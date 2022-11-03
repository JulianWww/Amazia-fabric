package net.denanu.amazia.economy;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.random.RandomCollection;

public class ProfessionFactory {
	private final static RandomCollection<String> professions = new RandomCollection<String>();
	
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
		/*register(Amazia.MOD_ID, "armorer", 1.0f);
		register(Amazia.MOD_ID, "butcher", 1.0f);
		register(Amazia.MOD_ID, "cartographer", 1.0f);
		register(Amazia.MOD_ID, "cleric", 1.0f);*/
		register(Amazia.MOD_ID, "farmer", 1.0f);
	}
}

