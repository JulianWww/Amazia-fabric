package net.denanu.amazia.entities.village.server.goal.utils;

import net.minecraft.entity.ai.goal.Goal;

public abstract class TimedGoal extends Goal {
	protected int ticker;
	protected int requiredTime;

	@Override
	public boolean shouldContinue() {
		return this.ticker < this.requiredTime;
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public void start() {
		super.start();
		this.ticker = 0;
		this.requiredTime = this.getRequiredTime();
	}

	@Override
	public void stop() {
		super.stop();
		if (this.ticker >= this.requiredTime) {
			this.takeAction();
		}
	}

	@Override
	public void tick() {
		this.ticker++;
	}

	protected abstract int getRequiredTime();
	protected abstract void takeAction();
}
