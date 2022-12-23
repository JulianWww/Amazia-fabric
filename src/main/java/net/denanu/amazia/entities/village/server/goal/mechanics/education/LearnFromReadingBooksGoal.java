package net.denanu.amazia.entities.village.server.goal.mechanics.education;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.education.AmaziaGainEducationMap;

public class LearnFromReadingBooksGoal extends TimedVillageGoal<AmaziaVillagerEntity> {

	public LearnFromReadingBooksGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return 6000;
	}

	@Override
	protected void takeAction() {
		AmaziaGainEducationMap.gainLibraryEducation(this.entity);
	}

}
