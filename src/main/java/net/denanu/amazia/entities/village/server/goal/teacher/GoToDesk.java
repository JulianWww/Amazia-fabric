package net.denanu.amazia.entities.village.server.goal.teacher;

import net.denanu.amazia.entities.village.server.TeacherEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.util.math.BlockPos;

public class GoToDesk extends AmaziaGoToBlockGoal<TeacherEntity> {
	public GoToDesk(final TeacherEntity e, final int priority) {
		super(e, priority);
	}

	public GoToDesk(final TeacherEntity e, final int priority, final int foodUsage) {
		super(e, priority, foodUsage);
	}

	public GoToDesk(final TeacherEntity e, final int priority, final int foodUsage, final float speed) {
		super(e, priority, foodUsage, speed);
	}

	@Override
	protected BlockPos getTargetBlock() {
		final NoHeightPathingData location = this.entity.getVillage().getDesk().getLocation();
		return location == null ? null : this.entity.setDeskLocation(location.getAccessPoint());
	}
}
