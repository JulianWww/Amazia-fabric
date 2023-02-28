package net.denanu.amazia.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.denanu.amazia.config.GuiConfigs;
import net.minecraft.client.MinecraftClient;

public class ModMenuImpl implements ModMenuApi
{
	@SuppressWarnings("resource")
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return screen -> {
			if (MinecraftClient.getInstance().player != null) {
				final GuiConfigs gui = new GuiConfigs();
				gui.setParent(screen);
				return gui;
			}
			return null;
		};
	}
}