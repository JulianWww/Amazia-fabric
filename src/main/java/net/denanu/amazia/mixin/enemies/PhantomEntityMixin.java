package net.denanu.amazia.mixin.enemies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.mixin.MobEntityAccessor;
import net.denanu.amazia.utils.EnemyGoalBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.World;

@Mixin(PhantomEntity.class)
public class PhantomEntityMixin extends FlyingEntity {
	protected PhantomEntityMixin(final EntityType<? extends FlyingEntity> entityType, final World world) {
		super(entityType, world);
		throw new RuntimeException("Invalid constructor");
	}

	@Inject(method="initGoals", at=@At("TAIL"))
	public void initGoals(final CallbackInfo callback) {
		final PhantomEntity entity = (PhantomEntity)(Object)this;
		((MobEntityAccessor)entity).getTargetSelector().add(3, EnemyGoalBuilder.buildAmaziaTargetGoal(entity));
	}
}
