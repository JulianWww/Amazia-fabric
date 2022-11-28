package net.denanu.amazia.mixin.enemies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.entities.village.server.combat.AttackSensor;
import net.denanu.amazia.mixin.MobEntityAccessor;
import net.denanu.amazia.utils.EnemyGoalBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements RangedAttackMob {
	protected AbstractSkeletonEntityMixin(final EntityType<? extends HostileEntity> entityType, final World world) {
		super(entityType, world);
		throw new RuntimeException("invalidConstructor");
	}

	@Inject(method="attack", at=@At("TAIL"))
	public void attack(final CallbackInfo info) {
		final AbstractSkeletonEntity entity = (AbstractSkeletonEntity)(Object)this;
		if (entity.getTarget() instanceof final AttackSensor sensor) {
			sensor.senceRangedAttack(entity);
		}
	}

	@Inject(method="initGoals", at=@At("TAIL"))
	public void initGoals(final CallbackInfo callback) {
		final AbstractSkeletonEntity entity = (AbstractSkeletonEntity)(Object)this;
		((MobEntityAccessor)entity).getTargetSelector().add(3, EnemyGoalBuilder.buildAmaziaTargetGoal(entity));
	}
}
