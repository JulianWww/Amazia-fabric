package net.denanu.amazia.highlighting;

import net.denanu.amazia.Amazia;
import net.denanu.clientblockhighlighting.config.HighlightType;
import net.denanu.clientblockhighlighting.config.HighlightTypes;

public class BlockHighlightingAmaziaConfig {
	public static HighlightType FARMING_EMPTY_FARMS = BlockHighlightingAmaziaConfig.register("emptyfarms");


	private static HighlightType register(final String name) {
		return HighlightTypes.register(Amazia.MOD_ID, name);
	}

	public static void setup() {}
}
