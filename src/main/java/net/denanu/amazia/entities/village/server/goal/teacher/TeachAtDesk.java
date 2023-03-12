package net.denanu.amazia.entities.village.server.goal.teacher;

import java.util.List;

import net.denanu.amazia.entities.moods.VillagerMoods;
import net.denanu.amazia.entities.village.server.ChildEntity;
import net.denanu.amazia.entities.village.server.TeacherEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.education.AmaziaGainEducationMap;

public class TeachAtDesk extends TimedVillageGoal<TeacherEntity> {

	public TeachAtDesk(final TeacherEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && this.isNearDesk();
	}

	@Override
	public boolean shouldContinue() {
		return super.shouldContinue() && this.isNearDesk();
	}

	private boolean isNearDesk() {
		return this.entity.getDeskLocation() != null && this.entity.squaredDistanceTo(
				this.entity.getDeskLocation().getX(),
				this.entity.getDeskLocation().getY(),
				this.entity.getDeskLocation().getZ()
				) < 9;
	}

	@Override
	protected int getRequiredTime() {
		return 20;//this.entity.getTeachTime();
	}

	@Override
	protected void takeAction() {
		final List<ChildEntity> entities = this.entity.world.getEntitiesByClass(
				ChildEntity.class,
				this.entity.getBoundingBox().expand(20, 20, 20),
				e -> true
				);
		for (final ChildEntity entity : entities) {
			AmaziaGainEducationMap.learnFromTeacher(entity, this.requiredTime);
			entity.emmitMood(VillagerMoods.HAPPY);
		}
		this.entity.gainXp(0.001f);
		this.entity.setDeskLocation(null);
	}



}
