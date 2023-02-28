package net.denanu.amazia.entities.village.both;

import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class Trackers {
	public static void setup() {
		TrackedDataHandlerRegistry.register(VillagerData.VILLAGER_DATA);
	}
}
