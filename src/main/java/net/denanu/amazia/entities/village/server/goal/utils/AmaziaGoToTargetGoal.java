package net.denanu.amazia.entities.village.server.goal.utils;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.minecraft.entity.Entity;

public class AmaziaGoToTargetGoal extends AmaziaGoToEntityGoal<AmaziaVillagerEntity> {

	public AmaziaGoToTargetGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}
	public AmaziaGoToTargetGoal(final AmaziaVillagerEntity e, final int priority, final int food) {
		super(e, priority, food);
	}
	public AmaziaGoToTargetGoal(final AmaziaVillagerEntity e, final int priority, final int food, final float speed) {
		super(e, priority, food, speed);
	}

	@Override
	public boolean canStart() {
		return this.entity.getTarget() != null && super.canStart();
	}

	@Override
	protected Entity getTargetEntity() {
		return this.entity.getTarget();
	}
}
