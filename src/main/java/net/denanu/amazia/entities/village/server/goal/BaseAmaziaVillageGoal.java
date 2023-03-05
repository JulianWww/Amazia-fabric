package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.goal.BaseAmaziaVillageGoal.BaseAmaziaVillagerEntity;
import net.denanu.amazia.village.scedule.BaseVillagerScedule;
import net.denanu.amazia.village.scedule.VillageActivityGroups;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class BaseAmaziaVillageGoal<E extends BaseAmaziaVillagerEntity> extends Goal {
	protected E entity;
	private final int priority;

	public BaseAmaziaVillageGoal(final E e, final int priority) {
		this.priority = priority;
		this.entity = e;
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true; // maybe remove this
	}

	@Override
	public void start() {
		super.start();
		this.entity.setCurrentlyRunnginGoal(this.priority);
	}

	@Override
	public void stop() {
		super.start();
		this.entity.endCurrentlyRunningGoal(this.priority);
	}

	@Override
	public boolean canStart() {
		return this.canRun() && this.isStartable();
	}

	@Override
	public boolean shouldContinue() {
		return this.canRun();
	}

	protected boolean canRun() {
		return this.entity.getActivityScedule().getPerformActionGroup() == VillageActivityGroups.WORK;
	}

	public boolean isStartable() {
		return BaseAmaziaVillageGoal.isStartable(this.entity, this.priority);
	}

	public static boolean isStartable(final BaseAmaziaVillagerEntity entity, final int priority) {
		return entity.hasVillage() && entity.canStartGoal(priority);
	}

	protected void lookAt(final BlockPos pos) {
		this.entity.getLookControl().lookAt(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}

	interface BaseAmaziaVillagerEntity {
		void setCurrentlyRunnginGoal(int priority);

		LookControl getLookControl();

		boolean canStartGoal(int priority);

		boolean hasVillage();

		BaseVillagerScedule getActivityScedule();

		void endCurrentlyRunningGoal(int priority);
	}
}
