package net.denanu.amazia.entities.village.server.goal.utils.combat;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;

public class AmaziaEscapeDangerGoal extends EscapeDangerGoal {
	AmaziaVillagerEntity entity;

	public AmaziaEscapeDangerGoal(final AmaziaVillagerEntity mob, final double speed) {
		super(mob, speed);
		this.entity = mob;
	}

	@Override
	public void start() {
		super.start();
		HappynessMap.looseHappynessFromFleeing(this.entity);
	}
}
