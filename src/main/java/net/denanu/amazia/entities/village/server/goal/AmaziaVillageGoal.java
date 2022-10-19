package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class AmaziaVillageGoal extends Goal {
	protected AmaziaVillagerEntity entity;
	private final int priority;
	
	public AmaziaVillageGoal(AmaziaVillagerEntity e, int priority) {
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
		return entity.hasVillage() && this.entity.canStartGoal(this.priority);
	}
	
	protected void lookAt(BlockPos pos) {
		this.entity.getLookControl().lookAt(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}
}
