package net.denanu.amazia.highlighting;

import net.denanu.amazia.Amazia;
import net.denanu.clientblockhighlighting.config.HighlightType;
import net.denanu.clientblockhighlighting.config.HighlightTypes;

public class BlockHighlightingAmaziaConfig {
	public static HighlightType FARMING_EMPTY_FARMS = BlockHighlightingAmaziaConfig.register("emptyfarms", "#FFFFFFFF", "#20FFFFFF");


	private static HighlightType register(final String name, final String outlineColor, final String fillColor) {
		return HighlightTypes.register(Amazia.MOD_ID, name, outlineColor, fillColor);
	}

	public static void setup() {}
}
