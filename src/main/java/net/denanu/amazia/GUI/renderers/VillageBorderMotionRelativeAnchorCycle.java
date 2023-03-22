package net.denanu.amazia.GUI.renderers;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum VillageBorderMotionRelativeAnchorCycle implements IConfigOptionListEntry {
	WORLD("selectWorld.world"),
	PLAYER("entity.minecraft.player");

	private String transKey;

	VillageBorderMotionRelativeAnchorCycle(final String transKey) {
		this.transKey = transKey;
	}

	@Override
	public String getStringValue() {
		return this.toString();
	}

	@Override
	public String getDisplayName() {
		return StringUtils.translate(this.transKey);
	}

	@Override
	public IConfigOptionListEntry cycle(final boolean forward) {
		return switch(this) {
		case WORLD -> VillageBorderMotionRelativeAnchorCycle.PLAYER;
		case PLAYER -> VillageBorderMotionRelativeAnchorCycle.WORLD;
		};
	}

	@Override
	public IConfigOptionListEntry fromString(final String value) {
		return VillageBorderMotionRelativeAnchorCycle.valueOf(value);
	}

	public float getY(final float y) {
		return switch(this) {
		case WORLD -> 0;
		case PLAYER -> -y/2;
		};
	}


}
