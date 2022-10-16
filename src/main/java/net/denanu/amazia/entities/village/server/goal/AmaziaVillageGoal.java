package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.minecraft.entity.ai.goal.Goal;

public class AmaziaVillageGoal extends Goal {
	private AmaziaVillagerEntity entity;
	
	public AmaziaVillageGoal(AmaziaVillagerEntity e) {
		this.entity = e;
	}

	@Override
	public boolean canStart() {
		return entity.hasVillage();
	}	
}
