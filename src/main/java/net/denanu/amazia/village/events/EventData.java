package net.denanu.amazia.village.events;

import net.minecraft.entity.Entity;

public class EventData {
	private final Entity emmiter;

	public EventData(final Entity emmiter) {
		this.emmiter = emmiter;
	}


	public Entity getEmmiter() {
		return this.emmiter;
	}
}
