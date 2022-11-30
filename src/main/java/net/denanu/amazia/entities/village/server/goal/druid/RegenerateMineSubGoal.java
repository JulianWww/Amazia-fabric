package net.denanu.amazia.entities.village.server.goal.druid;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;

public class RegenerateMineSubGoal extends TimedVillageGoal<DruidEntity> {
	public RegenerateMineSubGoal(final DruidEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getBlockPlaceTime();
	}

	@Override
	protected void takeAction() {
		Amazia.LOGGER.info("RegeneratedMine");
		this.entity.getMine().resetMine();
	}

}
