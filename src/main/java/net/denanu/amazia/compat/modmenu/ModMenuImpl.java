package net.denanu.amazia.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.denanu.amazia.config.GuiConfigs;

public class ModMenuImpl implements ModMenuApi
{
	@SuppressWarnings("resource")
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return screen -> {
			final GuiConfigs gui = new GuiConfigs();
			gui.setParent(screen);
			return gui;
		};
	}
}