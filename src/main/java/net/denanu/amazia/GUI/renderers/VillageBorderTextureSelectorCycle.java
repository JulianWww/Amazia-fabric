package net.denanu.amazia.GUI.renderers;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.amazia.Amazia;
import net.minecraft.util.Identifier;

public enum VillageBorderTextureSelectorCycle implements IConfigOptionListEntry {
	VANILLA(new Identifier("textures/misc/forcefield.png")),
	NEO("neo");

	private final Identifier texture;

	VillageBorderTextureSelectorCycle(final String texture) {
		this(Identifier.of(Amazia.MOD_ID, "textures/misc/border/" + texture + ".png"));
	}

	VillageBorderTextureSelectorCycle(final Identifier texture) {
		this.texture = texture;
	}

	@Override
	public String getStringValue() {
		return this.toString();
	}

	@Override
	public String getDisplayName() {
		return StringUtils.translate(this.texture.toTranslationKey());
	}

	@Override
	public IConfigOptionListEntry cycle(final boolean forward) {
		final int ordinal = this.ordinal() + (forward ? 1 : -1);
		if (ordinal < 0) {
			return VillageBorderTextureSelectorCycle.values()[VillageBorderTextureSelectorCycle.values().length - 1];
		}
		if (ordinal >= VillageBorderTextureSelectorCycle.values().length) {
			return VillageBorderTextureSelectorCycle.values()[0];
		}
		return VillageBorderTextureSelectorCycle.values()[ordinal];
	}

	@Nullable
	@Override
	public IConfigOptionListEntry fromString(final String value) {
		return VillageBorderTextureSelectorCycle.valueOf(value);
	}

	Identifier getKey() {
		return this.texture;
	}

}
