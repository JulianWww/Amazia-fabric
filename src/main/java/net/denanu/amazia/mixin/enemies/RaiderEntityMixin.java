package net.denanu.amazia.mixin.enemies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.mixin.MobEntityAccessor;
import net.denanu.amazia.utils.EnemyGoalBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;

@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntity {
	protected RaiderEntityMixin(final EntityType<? extends PatrolEntity> entityType, final World world) {
		super(entityType, world);
		throw new RuntimeException("Invalid constructor");
	}

	@Inject(method="initGoals", at=@At("TAIL"))
	public void initGoals(final CallbackInfo callback) {
		final RaiderEntity entity = (RaiderEntity)(Object)this;
		((MobEntityAccessor)entity).getTargetSelector().add(3, EnemyGoalBuilder.buildAmaziaTargetGoal(entity));
	}
}
