package net.denanu.amazia.mixin.enemies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.mixin.MobEntityAccessor;
import net.denanu.amazia.utils.EnemyGoalBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin extends HostileEntity {
	protected ZombieEntityMixin(final EntityType<? extends HostileEntity> entityType, final World world) {
		super(entityType, world);
		throw new RuntimeException("InvalidConstructor");
	}

	@Inject(method="initCustomGoals", at=@At("TAIL"))
	public void initGoals(final CallbackInfo info) {
		final ZombieEntity entity = (ZombieEntity)(Object)this;
		((MobEntityAccessor)entity).getTargetSelector().add(3, EnemyGoalBuilder.buildAmaziaTargetGoal(entity));
	}
}