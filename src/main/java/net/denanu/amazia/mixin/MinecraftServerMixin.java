package net.denanu.amazia.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin
{
	@Inject(at = @At("HEAD"), method="tick")
	public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		Amazia.economy.update(((MinecraftServerAccessor)(Object)this).getTicks());
	}
}