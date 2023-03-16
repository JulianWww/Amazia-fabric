package net.denanu.amazia.entities.village.server.goal.guard;

import java.util.EnumSet;

import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BowItem;

public class VillageGuardBowAttackGoal extends Goal {
	protected GuardEntity guard;
	protected float speed;
	protected int attackInterval;
	private final double squaredRange;
	private int attackCooldown = 0;
	private int combatTicks = 0;
	private boolean movingToLeft;
	private boolean backward;

	public VillageGuardBowAttackGoal(final GuardEntity guard, final float speed, final int attackInterval,
			final float range) {
		this.guard = guard;
		this.squaredRange = range * range;
		this.speed = speed;
		this.attackInterval = attackInterval;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}

	@Override
	public boolean canStart() {
		if (((MobEntity) this.guard).getTarget() == null) {
			return false;
		}
		final double distance = this.guard.squaredDistanceTo(this.guard.getTarget());
		return this.isHoldingBow() && this.guard.getTarget().isAlive() && this.guard.canSee(this.guard.getTarget())
				&& distance > 25f && distance < this.squaredRange;
	}

	private boolean isHoldingBow() {
		return this.guard.hasBow();
	}

	@Override
	public void start() {
		super.start();
		this.guard.setAttacking(true);
		this.attackCooldown = this.attackInterval;

		this.guard.getNavigation().stop();

		this.movingToLeft = this.guard.getRandom().nextBoolean();
		this.backward = this.guard.getRandom().nextBoolean();

		this.guard.equipStack(EquipmentSlot.MAINHAND, this.guard.getInventory().getStack(this.guard.getBow()));
	}

	/**
	 * @return the guard
	 */
	public GuardEntity getGuard() {
		return this.guard;
	}

	/**
	 * @param guard the guard to set
	 */
	public void setGuard(final GuardEntity guard) {
		this.guard = guard;
	}

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	/**
	 * @return the attack interval
	 */
	public int getAttackInterval() {
		return this.attackInterval;
	}

	/**
	 * @param attackInterval the attack interval to set
	 */
	public void setAttackInterval(final int attackInterval) {
		this.attackInterval = attackInterval;
	}

	@Override
	public void stop() {
		super.stop();
		((MobEntity) this.guard).setAttacking(false);
		((LivingEntity) this.guard).clearActiveItem();
		this.guard.getMoveControl().stop();
	}

	@Override
	public boolean shouldRunEveryTick() {
		return false;
	}

	@Override
	public void tick() {
		this.attackCooldown--;
		this.combatTicks++;
		final LivingEntity target = this.guard.getTarget();

		this.guard.getNavigation().stop();

		final double d = this.guard.squaredDistanceTo(target);

		if (this.combatTicks > 20) {
			if (this.guard.getRandom().nextFloat() < 0.3) {
				this.movingToLeft = !this.movingToLeft;
			}
			if (this.guard.getRandom().nextFloat() < 0.3) {
				this.backward = !this.backward;
			}
			this.combatTicks = 0;
		}
		final float rangeMod = target.isBaby() ? 0.3f : 1f;
		if (d > this.squaredRange * 0.75f * rangeMod) {
			this.backward = false;
		} else if (d < this.squaredRange * 0.25f * rangeMod) {
			this.backward = true;
		}

		this.guard.getMoveControl().strafeTo(this.backward ? -this.speed : this.speed,
				this.movingToLeft ? this.speed : -this.speed, 1.0f);
		this.guard.lookAtEntity(target, 30.0f, 30.0f);
		ActivityFoodConsumerMap.combatMovementUseFood(this.guard);

		if (this.attackCooldown < 0) {
			this.guard.shootBow(target, BowItem.getPullProgress(this.attackInterval));
			ActivityFoodConsumerMap.rangedAttackUseFood(this.guard);
			this.attackCooldown = this.attackInterval;
		}
	}
}
