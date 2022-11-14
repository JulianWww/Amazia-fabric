package net.denanu.amazia.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(final EntityType<? extends LivingEntity> entityType, final World world) {
		super(entityType, world);
	}

	@Inject(method="setTarget", at=@At("HEAD"))
	public void setTarget(@Nullable final LivingEntity target, final CallbackInfo info) {
		if (target instanceof final AmaziaEntity amazia && amazia.hasVillage()) {
			amazia.getVillage().addThreat((MobEntity)(Object) this);
		}
	}
}
