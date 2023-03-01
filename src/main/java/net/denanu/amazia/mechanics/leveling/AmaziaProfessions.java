package net.denanu.amazia.mechanics.leveling;

import java.util.ArrayList;

import net.denanu.amazia.Amazia;
import net.minecraft.util.Identifier;

public class AmaziaProfessions {
	public static ArrayList<Identifier> PROFESSIONS = new ArrayList<>();

	public static final Identifier BARD 		= AmaziaProfessions.register("bard");
	public static final Identifier BLACKSMITH 	= AmaziaProfessions.register("blacksmith");
	public static final Identifier CHEF 		= AmaziaProfessions.register("chef");
	public static final Identifier CLERIC 		= AmaziaProfessions.register("cleric");
	public static final Identifier DRUID 		= AmaziaProfessions.register("druid");
	public static final Identifier ENCHANTER	= AmaziaProfessions.register("enchanter");
	public static final Identifier FARMER 		= AmaziaProfessions.register("farmer");
	public static final Identifier GUARD 		= AmaziaProfessions.register("guard");
	public static final Identifier LUMBERJACK	= AmaziaProfessions.register("lumberjack");
	public static final Identifier MINER 		= AmaziaProfessions.register("miner");
	public static final Identifier NITWIT		= AmaziaProfessions.register("nitwit");
	public static final Identifier RANCHER 		= AmaziaProfessions.register("rancher");
	public static final Identifier TEACHER  	= AmaziaProfessions.register("teacher");
	public static final Identifier CHILD		= Identifier.of(Amazia.MOD_ID, "child");

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
