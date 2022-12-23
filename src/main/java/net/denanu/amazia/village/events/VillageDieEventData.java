package net.denanu.amazia.village.events;

import net.minecraft.entity.Entity;

public class VillageDieEventData extends EventData {
	boolean isBaby;

	public VillageDieEventData(final Entity emmiter, final boolean isBaby) {
		super(emmiter);
		this.isBaby = isBaby;
	}

	public boolean getIsBaby() {
		return this.isBaby;
	}
}
