package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.minecraft.util.math.BlockPos;

public abstract class TimedMineGoal extends TimedVillageGoal {
	protected MinerEntity entity;
	protected BlockPos pos;

	public TimedMineGoal(MinerEntity e, int priority) {
		super(e, priority);
		this.entity = e;
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void stop() {
		super.stop();
		this.pos = null;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.pos != null) { lookAt(this.pos); }
	}
	
	@Override
	public boolean canStart() {
		if (super.canStart() && this.entity.isInMine()) {
			return this.shouldStart();
		}
		return false;
	}

	protected abstract boolean shouldStart();
}
