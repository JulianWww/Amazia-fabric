package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public abstract class TimedVillageGoal<E extends AmaziaVillagerEntity> extends AmaziaVillageGoal<E> {
	protected int ticker;
	protected int requiredTime;

	public TimedVillageGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean shouldContinue() {
		return this.ticker < this.requiredTime && super.shouldContinue();
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
