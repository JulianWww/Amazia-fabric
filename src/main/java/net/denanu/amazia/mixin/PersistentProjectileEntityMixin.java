package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.projectile.PersistentProjectileEntity;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@ModifyVariable(method="tick()V", at = @At("STORE"), ordinal = 0)
	public float modifyTickDragInAri(final float val) {
		return val;
	}
}
