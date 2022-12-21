package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.effect.StatusEffectInstance;

@Mixin(StatusEffectInstance.class)
public interface StatusEffectInstanceMixin {
	@Accessor
	void setDuration(int duration);
}
