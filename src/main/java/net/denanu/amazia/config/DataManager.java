package net.denanu.clientblockhighlighting.config;

import fi.dy.masa.malilib.util.LayerRange;

public class DataManager {
	private static GuiConfigs.ConfigGuiTab tab = GuiConfigs.ConfigGuiTab.GENERIC;
	private static LayerRange renderRange = new LayerRange(null);

	public static GuiConfigs.ConfigGuiTab getConfigGuiTab() {
		return DataManager.tab;
	}

	public static void setConfigGuiTab(final GuiConfigs.ConfigGuiTab tab) {
		DataManager.tab = tab;
	}

	public static LayerRange getRenderRange() {
		return DataManager.renderRange;
	}
}
