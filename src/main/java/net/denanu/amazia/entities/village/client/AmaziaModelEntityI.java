package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.entities.village.both.VillagerData;
import net.minecraft.util.Identifier;

public interface AmaziaModelEntityI {
	Identifier getProfession();
	VillagerData getData();
}
