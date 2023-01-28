package net.denanu.amazia.highlighting;

import net.denanu.amazia.Amazia;
import net.denanu.blockhighlighting.config.HighlightType;
import net.denanu.blockhighlighting.config.HighlightTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlockHighlightingAmaziaConfig {
	public static HighlightType DESK				 		= BlockHighlightingAmaziaConfig.register("desk",				"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType FARMING_EMPTY_FARMS 		= BlockHighlightingAmaziaConfig.register("empty-farms", 		"#FFFFFFFF", "#20FFFFFF");
	public static HighlightType FARMING_GOWING_FARMS 		= BlockHighlightingAmaziaConfig.register("growing-farms", 		"#FFFFFFFF", "#20FFFFFF");
	public static HighlightType FARMING_HARVISTABLE_FARMS 	= BlockHighlightingAmaziaConfig.register("harvistable-farms", 	"#FF00FF00", "#2000FF00");
	public static HighlightType FARMING_POSSIBLE_FARMS 		= BlockHighlightingAmaziaConfig.register("possible-farms", 		"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType STORAGE					 	= BlockHighlightingAmaziaConfig.register("storage",			 	"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType MINEING					 	= BlockHighlightingAmaziaConfig.register("mineing",			 	"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType LUMBERJACK_FULL			 	= BlockHighlightingAmaziaConfig.register("lumberjack-full",	 	"#FF00FF00", "#2000FF00");
	public static HighlightType LUMBERJACK_EMPTY		 	= BlockHighlightingAmaziaConfig.register("lumberjack-empty", 	"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType RANCHING_PENS		 		= BlockHighlightingAmaziaConfig.register("ranching-troughs",	"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType ENCHANTING			 		= BlockHighlightingAmaziaConfig.register("enchanting",			"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType LIBRARY				 		= BlockHighlightingAmaziaConfig.register("library",				"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType FORGE				 		= BlockHighlightingAmaziaConfig.register("forge",				"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType NORMAL_FURNACES		 		= BlockHighlightingAmaziaConfig.register("furnace",				"#FFFFFFFF", "#20FFFFFF");
	public static HighlightType BLAST_FURNACES		 		= BlockHighlightingAmaziaConfig.register("blast-furnace",		"#FFFFFFFF", "#20FFFFFF");
	public static HighlightType SMOKER_FURNACES		 		= BlockHighlightingAmaziaConfig.register("smoker",				"#FFFFFFFF", "#20FFFFFF");



	private static HighlightType register(final String name, final String outlineColor, final String fillColor) {
		return HighlightTypes.register(Amazia.MOD_ID, name, outlineColor, fillColor);
	}

	public static void setup() {}
}
