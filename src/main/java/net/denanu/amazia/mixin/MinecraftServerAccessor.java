package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
	@Accessor
	public int getTicks();
}
