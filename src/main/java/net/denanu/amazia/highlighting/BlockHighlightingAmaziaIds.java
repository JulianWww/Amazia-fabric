package net.denanu.amazia.highlighting;

import net.denanu.amazia.Amazia;
import net.denanu.blockhighlighting.config.HighlightIds;
import net.minecraft.util.Identifier;

public class BlockHighlightingAmaziaIds {
	public static final Identifier DESK 						= BlockHighlightingAmaziaIds.register("desk");

	public static final Identifier CHAIR						= BlockHighlightingAmaziaIds.register("chair");

	public static final Identifier FARMING_EMPTY_FARMS 			= BlockHighlightingAmaziaIds.register("empty-farms");
	public static final Identifier FARMING_GOWING_FARMS 		= BlockHighlightingAmaziaIds.register("growing-farms");
	public static final Identifier FARMING_HARVISTABLE_FARMS 	= BlockHighlightingAmaziaIds.register("harvistable-farms");
	public static final Identifier FARMING_POSSIBLE_FARMS 		= BlockHighlightingAmaziaIds.register("possible-farms");

	public static final Identifier STORAGE						= BlockHighlightingAmaziaIds.register("storage");

	public static final Identifier MINEING						= BlockHighlightingAmaziaIds.register("mineing");

	public static final Identifier LUMBERJACK_FULL				= BlockHighlightingAmaziaIds.register("lumberjack-full");
	public static final Identifier LUMBERJACK_EMPTY		 		= BlockHighlightingAmaziaIds.register("lumberjack-empty");

	public static final Identifier RANCHING_PENS		 		= BlockHighlightingAmaziaIds.register("ranching-troughs");

	public static final Identifier ENCHANTING			 		= BlockHighlightingAmaziaIds.register("enchanting");

	public static final Identifier LIBRARY				 		= BlockHighlightingAmaziaIds.register("library");

	public static final Identifier FORGE				 		= BlockHighlightingAmaziaIds.register("forge");

	public static final Identifier NORMAL_FURNACES		 		= BlockHighlightingAmaziaIds.register("furnace");
	public static final Identifier BLAST_FURNACES		 		= BlockHighlightingAmaziaIds.register("blast-furnace");
	public static final Identifier SMOKER_FURNACES		 		= BlockHighlightingAmaziaIds.register("smoker");

	public static final Identifier BEDS							= BlockHighlightingAmaziaIds.register("beds");

	private static Identifier register(final String name) {
		return HighlightIds.register(Identifier.of(Amazia.MOD_ID, name));
	}

	public static void setup () {}
}
