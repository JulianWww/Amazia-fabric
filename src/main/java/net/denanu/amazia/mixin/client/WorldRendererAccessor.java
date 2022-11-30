package net.denanu.amazia.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
	@Accessor
	public ClientWorld getWorld();

	@Accessor
	public MinecraftClient getClient();
}
