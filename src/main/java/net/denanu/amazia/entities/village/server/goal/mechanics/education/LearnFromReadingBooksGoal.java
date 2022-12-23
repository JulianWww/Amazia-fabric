package net.denanu.amazia.entities.village.server.goal.mechanics.education;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.education.AmaziaGainEducationMap;
import net.denanu.amazia.mechanics.happyness.HappynessMap;

public class LearnFromReadingBooksGoal extends TimedVillageGoal<AmaziaVillagerEntity> {

	public LearnFromReadingBooksGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		AmaziaGainEducationMap.gainLibraryEducation(this.entity);
		HappynessMap.gainRadBookHappyness(this.entity);
	}

}
