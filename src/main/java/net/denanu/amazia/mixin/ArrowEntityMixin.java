package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntity {
	protected ArrowEntityMixin(final EntityType<? extends PersistentProjectileEntity> type, final double x,
			final double y, final double z, final World world) {
		super(type, x, y, z, world);
	}

	@Inject(method = "onHit", at = @At("TAIL"))
	public void onHitMixin(final LivingEntity target, final CallbackInfo info) {
		final Entity owner = ((ArrowEntity) (Object) this).getOwner();
		if ((target instanceof FlyingEntity || target instanceof HostileEntity)
				&& owner instanceof final GuardEntity guard) {
			AmaziaXpGainMap.gainAttackXp(guard);
		}
	}
}
