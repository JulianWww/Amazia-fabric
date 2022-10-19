package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public abstract class TimedVillageGoal extends AmaziaVillageGoal {
	protected int ticker;
	protected int requiredTime;

	public TimedVillageGoal(AmaziaVillagerEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean shouldContinue() {
		return ticker < this.requiredTime;
	}
	
	@Override
	public boolean shouldRunEveryTick() {
        return true;
    }
	
	@Override
	public void start() {
		super.start();
		this.ticker = 0;
		this.requiredTime = this.getRequiredTime();;
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
