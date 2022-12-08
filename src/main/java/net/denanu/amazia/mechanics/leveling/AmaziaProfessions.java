package net.denanu.amazia.mechanics.leveling;

import java.util.ArrayList;

import net.denanu.amazia.Amazia;
import net.minecraft.util.Identifier;

public class AmaziaProfessions {
	public static ArrayList<Identifier> PROFESSIONS = new ArrayList<Identifier>();

	public static Identifier BLACKSMITH 	= AmaziaProfessions.register("blacksmith");
	public static Identifier CHEF 			= AmaziaProfessions.register("chef");
	public static Identifier CLERIC 		= AmaziaProfessions.register("cleric");
	public static Identifier DRUID 			= AmaziaProfessions.register("druid");
	public static Identifier ENCHANTER		= AmaziaProfessions.register("enchanter");
	public static Identifier FARMER 		= AmaziaProfessions.register("farmer");
	public static Identifier GUARD 			= AmaziaProfessions.register("guard");
	public static Identifier LUMBERJACK		= AmaziaProfessions.register("lumberjack");
	public static Identifier MINER 			= AmaziaProfessions.register("miner");
	public static Identifier NITWIT			= AmaziaProfessions.register("nitwit");
	public static Identifier RANCHER 		= AmaziaProfessions.register("rancher");

	public static Identifier register(final Identifier profession) {
		AmaziaProfessions.PROFESSIONS.add(profession);
		return profession;
	}

	private static Identifier register(final String profession) {
		return AmaziaProfessions.register(Identifier.of(Amazia.MOD_ID, profession));
	}

	public static void build() {
		AmaziaProfessions.PROFESSIONS.trimToSize();
	}


	public static void setup() {
		AmaziaProfessions.build();
	}
}
