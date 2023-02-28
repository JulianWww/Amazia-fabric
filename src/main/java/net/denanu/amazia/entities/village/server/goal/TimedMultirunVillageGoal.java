package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public abstract class TimedMultirunVillageGoal<E extends AmaziaVillagerEntity> extends AmaziaVillageGoal<E> {
	private int tickCount;
	protected int maxCount;
	private boolean notDone;

	public TimedMultirunVillageGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean shouldContinue() {
		return this.notDone  && super.shouldContinue();
	}

	@Override
	public void start() {
		super.start();
		this.tickCount = 0;
		this.maxCount = this.getRequiredTime();
		this.notDone = true;
	}

	@Override
	public void tick() {
		this.tickCount ++;
		if (this.tickCount > this.maxCount) {
			this.takeAction();
			this.tickCount = 0;
		}
	}

	public void terminate() {
		this.notDone = false;
	}

	protected abstract int getRequiredTime();
	protected abstract void takeAction();
}
