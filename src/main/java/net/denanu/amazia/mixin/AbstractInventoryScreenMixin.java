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
	private static int[] romanDecimalNumbers = 		{1,   4,    5,   9,    10,  40,   50,  90,   100};
	private static String[] romanStringNumbers = 	{"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C"};

	@Inject(method="getStatusEffectDescription", at=@At("HEAD"), cancellable=true)
	public void getStatusEffectDescription(final StatusEffectInstance statusEffect, final CallbackInfoReturnable<Text> cir) {
		final MutableText mutableText = statusEffect.getEffectType().getName().copy();
		mutableText.append(" ").append(Text.literal(AbstractInventoryScreenMixin.toRoman(statusEffect.getAmplifier())));
		cir.setReturnValue(mutableText);
		cir.cancel();
	}


	private static String toRoman(int number) {
		final StringBuilder out = new StringBuilder();
		int i = AbstractInventoryScreenMixin.romanDecimalNumbers.length - 1;
		while (number > 0) {
			int div = Math.floorDiv(number, AbstractInventoryScreenMixin.romanDecimalNumbers[i]);
			number = number % AbstractInventoryScreenMixin.romanDecimalNumbers[i];
			while (div > 0) {
				out.append(AbstractInventoryScreenMixin.romanStringNumbers[i]);
				div--;
			}
			i--;
		}

		return out.toString();
	}
}
