package net.denanu.amazia.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.GUI.renderers.VillageBorderRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.resource.SynchronousResourceReloader;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SynchronousResourceReloader, AutoCloseable {
	@Inject(method="renderWorldBorder", at=@At("HEAD"))
	public void renderVillageBorders(final Camera camera, final CallbackInfo info) {
		final WorldRendererAccessor render = (WorldRendererAccessor)this;
		VillageBorderRenderer.render(camera, render.getClient(), render.getWorld());
	}
}
