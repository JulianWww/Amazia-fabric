package net.denanu.amazia.highlighting;

import net.denanu.amazia.Amazia;
import net.denanu.clientblockhighlighting.config.HighlightType;
import net.denanu.clientblockhighlighting.config.HighlightTypes;

public class BlockHighlightingAmaziaConfig {
	public static HighlightType FARMING_EMPTY_FARMS 		= BlockHighlightingAmaziaConfig.register("empty-farms", 		"#FFFFFFFF", "#20FFFFFF");
	public static HighlightType FARMING_GOWING_FARMS 		= BlockHighlightingAmaziaConfig.register("growing-farms", 		"#FFFFFFFF", "#20FFFFFF");
	public static HighlightType FARMING_HARVISTABLE_FARMS 	= BlockHighlightingAmaziaConfig.register("harvistable-farms", 	"#FF00FF00", "#2000FF00");
	public static HighlightType FARMING_POSSIBLE_FARMS 		= BlockHighlightingAmaziaConfig.register("possible-farms", 		"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType STORAGE					 	= BlockHighlightingAmaziaConfig.register("storage",			 	"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType MINEING					 	= BlockHighlightingAmaziaConfig.register("mineing",			 	"#FFFFFFFF", "#20FFFFFF");

	public static HighlightType LUMBERJACK_FULL			 	= BlockHighlightingAmaziaConfig.register("lumberjack-full",	 	"#FF00FF00", "#2000FF00");
	public static HighlightType LUMBERJACK_EMPTY		 	= BlockHighlightingAmaziaConfig.register("lumberjack-empty", 	"#FFFFFFFF", "#20FFFFFF");


	private static HighlightType register(final String name, final String outlineColor, final String fillColor) {
		return HighlightTypes.register(Amazia.MOD_ID, name, outlineColor, fillColor);
	}

	public static void setup() {}
}
