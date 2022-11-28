package net.denanu.amazia.entities.village.server.goal.guard;

import net.denanu.amazia.entities.village.server.GuardEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class GuardMeleeAttackGoal extends MeleeAttackGoal {
	protected GuardEntity guard;

	public GuardMeleeAttackGoal(final GuardEntity mob, final double speed, final boolean pauseWhenMobIdle) {
		super(mob, speed, pauseWhenMobIdle);
		this.guard = mob;
	}

	@Override
	public void start() {
		super.start();
		this.guard.equipWeapon(this.guard.getTarget());
	}

	@Override
	protected int getMaxCooldown() {
		return this.getTickCount(this.guard.getAttackTime());
	}
}
