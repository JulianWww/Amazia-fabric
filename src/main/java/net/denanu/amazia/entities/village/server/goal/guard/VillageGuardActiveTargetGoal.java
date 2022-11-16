package net.denanu.amazia.entities.village.server.goal.guard;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.village.sceduling.opponents.OpponentData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class VillageGuardActiveTargetGoal extends Goal {
	private final int reciprocalChance;
	private final GuardEntity guard;
	private LivingEntity targetEntity;

	public VillageGuardActiveTargetGoal(final GuardEntity entity, final int reciprocalChance) {
		this.guard = entity;
		this.reciprocalChance = reciprocalChance;
	}

	@Override
	public boolean canStart() {
		if (!this.guard.hasVillage() || this.reciprocalChance > 0 && this.guard.getRandom().nextInt(this.reciprocalChance) != 0) {
			return false;
		}
		this.findClosestTarget();
		return this.targetEntity != null;
	}

	@Override
	public boolean shouldContinue() {
		final LivingEntity target = this.guard.getTarget();
		if (target == null) { return false; }

		return target.isAlive() && this.guard.getVillage().isInVillage(target);
	}


	protected void findClosestTarget() {
		if (this.guard.getTarget() != null) {
			this.setTargetEntity(this.guard.getTarget());
		}
		final OpponentData opponent = this.guard.getVillage().getGuarding().getOpponent();
		this.setTargetEntity(opponent == null ? null : opponent.getTarget());
	}

	@Override
	public void start() {
		this.guard.setTarget(this.targetEntity);
		super.start();
	}

	@Override
	public void stop() {
		this.guard.setTarget(null);
	}

	public void setTargetEntity(@Nullable final LivingEntity targetEntity) {
		this.targetEntity = targetEntity;
	}
}
