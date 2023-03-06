package net.denanu.amazia.compat.malilib;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.GuiBase;
import net.denanu.amazia.config.GuiConfigs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class NameFakerOption implements IConfigOptionListEntry {
	public NamingLanguageOptions selected = NamingLanguageOptions.ENGLISH;

	public NameFakerOption() {
		this(NamingLanguageOptions.ENGLISH);
	}

	public NameFakerOption(final NamingLanguageOptions selected) {
		this.selected = selected;
	}

	@Override
	public String getStringValue() {
		return this.selected.getStringValue();
	}

	@Override
	public String getDisplayName() {
		return this.selected.getDisplayName();
	}

	@Override
	public IConfigOptionListEntry cycle(final boolean forward) {
		final MinecraftClient client = MinecraftClient.getInstance();
		GuiBase.openGui(new NameGeneratorLocalOptionsScreen(GuiConfigs.getInstance(), client.options, this.selected));
		return this;
	}

	@Override
	public IConfigOptionListEntry fromString(final String value) {
		return new NameFakerOption(NamingLanguageOptions.of(value));
	}
}
