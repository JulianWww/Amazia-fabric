package net.denanu.clientblockhighlighting.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.denanu.clientblockhighlighting.Mod;
import net.denanu.clientblockhighlighting.config.GuiConfigs;

public class ModMenuImpl implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
    	Mod.LOGGER.info("adding modmenu");
        return screen -> {
            GuiConfigs gui = new GuiConfigs();
            gui.setParent(screen);
            return gui;
        };
    }
}