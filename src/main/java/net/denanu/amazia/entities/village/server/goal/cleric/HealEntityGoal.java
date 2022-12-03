package net.denanu.amazia.entities.village.server.goal.cleric;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;

public class HealEntityGoal extends TimedVillageGoal<ClericEntity> {

	public HealEntityGoal(final ClericEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return this.entity.getTarget() != null && super.canStart();
	}


	@Override
	protected int getRequiredTime() {
		return this.entity.getHealTime();
	}

	@Override
	protected void takeAction() {
		Amazia.LOGGER.info("Healed entity");
	}

}
