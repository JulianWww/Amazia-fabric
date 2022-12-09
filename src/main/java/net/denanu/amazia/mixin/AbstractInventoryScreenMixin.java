package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@Mixin(AbstractInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin {
	@Inject(method="getStatusEffectDescription", at=@At("HEAD"), cancellable=true)
	public void getStatusEffectDescription(final StatusEffectInstance statusEffect, final CallbackInfoReturnable<Text> cir) {
		final MutableText mutableText = statusEffect.getEffectType().getName().copy();
		mutableText.append(" ").append(Text.translatable("enchantment.level." + statusEffect.getAmplifier()));
		cir.setReturnValue(mutableText);
		cir.cancel();
	}
}
