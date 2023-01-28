package net.denanu.amazia.entities.village.server.goal.teacher;

import java.util.List;

import net.denanu.amazia.entities.village.server.NitwitEntity;
import net.denanu.amazia.entities.village.server.TeacherEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.education.AmaziaGainEducationMap;
import net.minecraft.entity.passive.PassiveEntity;

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
		return this.entity.getTeachTime();
	}

	@Override
	protected void takeAction() {
		final List<NitwitEntity> entities = this.entity.world.getEntitiesByClass(
				NitwitEntity.class,
				this.entity.getBoundingBox().expand(20, 20, 20),
				e -> (e instanceof final PassiveEntity passive && passive.isBaby())
				);
		for (final NitwitEntity entity : entities) {
			AmaziaGainEducationMap.learnFromTeacher(entity, this.requiredTime);
		}
		this.entity.setDeskLocation(null);
	}



}
