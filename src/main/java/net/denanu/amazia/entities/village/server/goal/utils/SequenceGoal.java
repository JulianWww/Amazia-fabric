package net.denanu.amazia.entities.village.server.goal.utils;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;

public class SequenceGoal<E extends Entity> extends Goal {
	private final ImmutableList<Goal> goals;
	private int idx;
	private int running;
	public SequenceGoal(final E e, final ImmutableList<Goal> goals) {
		this.goals = goals;
		this.idx = 0;
		this.running = -1;
	}

	@Override
	public boolean canStart() {
		return this.getCurrentGoal().canStart();
	}

	@Override
	public boolean shouldContinue() {
		final boolean canContune = this.getCurrentGoal().shouldContinue();
		if (this.idx < this.goals.size()-1 && !canContune) {
			this.getCurrentGoal().stop();
			this.running = -1;
			this.idx++;
			final boolean startnext =  this.getCurrentGoal().canStart();
			if (startnext) {
				this.getCurrentGoal().start();
				this.running = this.idx;
				return true;
			}
			return false;
		}
		return canContune;
	}

	@Override
	public void tick() {
		this.getCurrentGoal().tick();
	}

	@Override
	public void start() {
		this.idx = 0;
		this.running = 0;
		this.getCurrentGoal().start();
	}

	@Override
	public void stop() {
		if (this.running >= 0) {
			this.getRunningGoal().stop();
		}
		this.idx = 0;
		this.running = -1;
	}

	private Goal getCurrentGoal() {
		return this.goals.get(this.idx);
	}

	private Goal getRunningGoal() {
		return this.goals.get(this.running);
	}
}
