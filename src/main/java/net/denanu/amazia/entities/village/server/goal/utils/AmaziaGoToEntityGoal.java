package net.denanu.amazia.entities.village.server.goal.utils;

import javax.annotation.Nullable;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public abstract class AmaziaGoToEntityGoal<E extends AmaziaVillagerEntity> extends AmaziaGoToBlockGoal<E> {
	protected Entity targetEntity;

	public AmaziaGoToEntityGoal(final E e, final int priority, final float speed) {
		super(e, priority, speed);
	}
	public AmaziaGoToEntityGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public void stop() {
		super.stop();
		this.targetEntity = null;
	}

	@Override
	public void tick() {
		super.tick();
		final BlockPos pos = this.getEntityPos();
		if (pos.getManhattanDistance(this.targetPos) > 2) {
			this.targetPos = pos;
			this.recalcPath();
		}
	}

	public BlockPos getEntityPos() {
		return new BlockPos(
				this.targetEntity.getBlockX(),
				Math.floor(this.targetEntity.getY() + 0.5),
				this.targetEntity.getBlockZ()
				);

	}

	@Override
	public double getDesiredDistanceToTarget() {
		return 3;
	}

	@Override
	@Nullable
	protected BlockPos getTargetBlock() {
		if (this.targetEntity == null) {
			this.targetEntity = this.getTargetEntity();
			if (this.targetEntity == null) {
				return null;
			}
		}
		return this.targetEntity.getBlockPos();
	}

	@Nullable
	protected abstract Entity getTargetEntity();
}
