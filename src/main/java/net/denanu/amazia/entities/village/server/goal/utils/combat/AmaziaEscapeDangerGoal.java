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
	public void stop() {
		super.stop();
		this.entity.endCurrentlyRunningGoal(0);
	}

	@Override
	public void start() {
		super.start();
		this.entity.setCurrentlyRunnginGoal(0);
		HappynessMap.looseHappynessFromFleeing(this.entity);
	}
}
