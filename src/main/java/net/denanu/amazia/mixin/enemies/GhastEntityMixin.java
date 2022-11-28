package net.denanu.amazia.mixin.enemies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.mixin.MobEntityAccessor;
import net.denanu.amazia.utils.EnemyGoalBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.world.World;

@Mixin(GhastEntity.class)
public class GhastEntityMixin extends FlyingEntity implements Monster{
	protected GhastEntityMixin(final EntityType<? extends FlyingEntity> entityType, final World world) {
		super(entityType, world);
		throw new RuntimeException("Invalid Constructor");
	}

	@Inject(method="initGoals", at=@At("TAIL"))
	public void initGoals(final CallbackInfo callback) {
		final GhastEntity entity = (GhastEntity)(Object)this;
		((MobEntityAccessor)entity).getTargetSelector().add(3, EnemyGoalBuilder.buildAmaziaTargetGoal(entity));
	}
}
