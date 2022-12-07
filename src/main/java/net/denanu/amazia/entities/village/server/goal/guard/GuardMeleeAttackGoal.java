package net.denanu.amazia.entities.village.server.goal.guard;

import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

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
	public void tick() {
		super.tick();
		if (this.guard.getNavigation().isFollowingPath()) {
			ActivityFoodConsumerMap.combatMovementUseFood(this.guard);
		}
	}

	@Override
	protected void attack(final LivingEntity target, final double squaredDistance) {
		final double d = this.getSquaredMaxAttackDistance(target);
		if (squaredDistance <= d && this.getCooldown() <= 0) {
			this.resetCooldown();
			this.mob.swingHand(Hand.MAIN_HAND);
			this.mob.tryAttack(target);
			ActivityFoodConsumerMap.melleAttackUseFood(this.guard);
		}
	}

	@Override
	protected int getMaxCooldown() {
		return this.getTickCount(this.guard.getAttackTime());
	}
}
