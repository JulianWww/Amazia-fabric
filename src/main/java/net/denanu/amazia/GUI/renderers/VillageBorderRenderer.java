package net.denanu.amazia.GUI.renderers;

import java.util.HashSet;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import fi.dy.masa.malilib.util.Color4f;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.config.Config;
import net.denanu.amazia.village.VillageBoundingBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

// TODO tiedy up

public class VillageBorderRenderer {
	public static HashSet<VillageBoundingBox> villageBoxes = new HashSet<>();

	public static void renderWorldBorder(final Camera camera, final ClientWorld world, final MinecraftClient client, final VillageBoundingBox worldBorder) {
		double u;
		double t;
		float s, v;
		final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		final double d = Math.min(client.options.getClampedViewDistance() * 16, Config.Generic.BORDER_DISTANCE.getIntegerValue());
		final double dd = d * d;
		if (camera.getPos().x < worldBorder.getBoundEast() - d && camera.getPos().x > worldBorder.getBoundWest() + d && camera.getPos().z < worldBorder.getBoundSouth() - d && camera.getPos().z > worldBorder.getBoundNorth() + d) {
			return;
		}
		final double f = camera.getPos().x;
		final double g = camera.getPos().z;

		final double h = Config.Generic.BORDER_HIGHT.getDoubleValue();
		final double fade_h = Config.Generic.BORDER_FALLAF.getDoubleValue();

		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		RenderSystem.setShaderTexture(0, ((VillageBorderTextureSelectorCycle)Config.Generic.VILLAGE_BORDER_TEXTURES.getOptionListValue()).getKey());
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		final MatrixStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.push();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.polygonOffset(-3.0f, -3.0f);
		RenderSystem.enablePolygonOffset();
		RenderSystem.disableCull();
		final Color4f baseColor = Config.Generic.BORDER_COLOR.getColor();
		final float m = Util.getMeasuringTimeMs() % 3000L / 3000f * (float)Config.Generic.MOVEMENT_SPEED.getDoubleValue();
		final float p = (float) h;//(float)(h - MathHelper.fractionalPart(camera.getPos().y));
		final float fp = (float) fade_h/2;
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		double q = Math.max(MathHelper.floor(g - d), worldBorder.getBoundNorth());
		double r = Math.min(MathHelper.ceil(g + d), worldBorder.getBoundSouth());
		final float y = ((VillageBorderMotionRelativeAnchorCycle)Config.Generic.BORDER_MOTION.getOptionListValue()).getY((float)camera.getPos().y);

		if (f > worldBorder.getBoundEast() - d) {
			t = q;
			s = 0f;
			while (t < r) {
				u = Math.min(1.0, r - t);
				v = (float)u * 0.5f;

				final float e1 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(worldBorder.getBoundEast() - f, t - g) 	 / dd, 0.0, 1.0);
				final float e2 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(worldBorder.getBoundEast() - f, t + u - g) / dd, 0.0, 1.0);

				if (e1 != 0 || e2 != 0) {
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h, t - g)					.texture(m - s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h, t + u - g)				.texture(m - (v + s), y + m + p)	.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, h,  t + u - g)				.texture(m - (v + s), y + m)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, h,  t - g)					.texture(m - s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();

					bufferBuilder.vertex(worldBorder.getBoundEast() - f, h, t - g)					.texture(m - s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, h, t + u - g)				.texture(m - (v + s), y + m )		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, h + fade_h,  t + u - g)	.texture(m - (v + s), y + m - fp)	.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, h + fade_h,  t - g)		.texture(m - s, y + m - fp)			.color(baseColor.r, baseColor.g, baseColor.b, 0).next();

					bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h - fade_h, t - g)		.texture(m - s, y + m + p + fp)		.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h - fade_h, t + u - g)	.texture(m - (v + s), y + m + p +fp).color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h,  t + u - g)			.texture(m - (v + s), y + m + p)	.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h,  t - g)				.texture(m - s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
				}
				t += 1.0;
				s += 0.5;
			}
		}
		//RenderSystem.setShader(GameRenderer::getPositionTexShader);
		if (f < worldBorder.getBoundWest() + d) {
			s = 0.0f;
			t = q;
			while (t < r) {
				u = Math.min(1.0, r - t);
				v = (float)u * 0.5f;

				final float e1 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(worldBorder.getBoundWest() - f, t - g) 	 / dd, 0.0, 1.0);
				final float e2 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(worldBorder.getBoundWest() - f, t + u - g) / dd, 0.0, 1.0);

				if (e1 != 0 || e2 != 0) {
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h, t - g)					.texture(m + s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h, t + u - g)				.texture(m + v + s, y + m + p)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, h, t + u - g)				.texture(m + v + s, y + m)			.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, h, t - g)					.texture(m + s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();

					bufferBuilder.vertex(worldBorder.getBoundWest() - f, h, t - g)					.texture(m + s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, h, t + u - g)				.texture(m + (v + s), y + m)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, h + fade_h,  t + u - g)	.texture(m + (v + s), y + m - fp)	.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, h + fade_h,  t - g)		.texture(m + s, y + m - fp)			.color(baseColor.r, baseColor.g, baseColor.b, 0).next();

					bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h - fade_h, t - g)		.texture(m + s, y + m + p + fp)		.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h - fade_h, t + u - g)	.texture(m + (v + s), y + m + p +fp).color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h,  t + u - g)			.texture(m + (v + s), y + m + p)	.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h,  t - g)				.texture(m + s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
				}
				t += 1.0;
				s += 0.5f;
			}
		}
		q = Math.max(MathHelper.floor(f - d), worldBorder.getBoundWest());
		r = Math.min(MathHelper.ceil(f + d), worldBorder.getBoundEast());
		if (g > worldBorder.getBoundSouth() - d) {
			s = 0.0f;
			t = q;
			while (t < r) {
				u = Math.min(1.0, r - t);
				v = (float)u * 0.5f;

				final float e1 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(t - f, worldBorder.getBoundSouth() - g) 	 / dd, 0.0, 1.0);
				final float e2 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(t + u - f, worldBorder.getBoundSouth() - g) / dd, 0.0, 1.0);

				if (e1 != 0 || e2 != 0) {
					bufferBuilder.vertex(t - f, -h, worldBorder.getBoundSouth() - g)				.texture(m + s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(t + u - f, -h, worldBorder.getBoundSouth() - g)			.texture(m + v + s, y + m + p)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t + u - f, h, worldBorder.getBoundSouth() - g)				.texture(m + v + s, y + m)			.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t - f, h, worldBorder.getBoundSouth() - g)					.texture(m + s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();

					bufferBuilder.vertex(t - f, h, worldBorder.getBoundSouth() - g)					.texture(m + s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(t + u - f, h, worldBorder.getBoundSouth() - g)				.texture(m + v + s, y + m)			.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t + u - f, h + fade_h, worldBorder.getBoundSouth() - g)	.texture(m + v + s, y + m - fp)		.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(t - f, h + fade_h, worldBorder.getBoundSouth() - g)		.texture(m + s, y + m - fp)			.color(baseColor.r, baseColor.g, baseColor.b, 0).next();

					bufferBuilder.vertex(t - f, -h, worldBorder.getBoundSouth() - g)				.texture(m + s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(t + u - f, -h, worldBorder.getBoundSouth() - g)			.texture(m + v + s, y + m + p)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t + u - f, -h - fade_h, worldBorder.getBoundSouth() - g)	.texture(m + v + s, y + m + p+ fp)	.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(t - f, -h - fade_h, worldBorder.getBoundSouth() - g)		.texture(m + s, y +  m + p+ fp)		.color(baseColor.r, baseColor.g, baseColor.b, 0).next();

				}
				t += 1.0;
				s += 0.5f;
			}
		}
		if (g < worldBorder.getBoundNorth() + d) {
			s = 0.0f;
			t = q;
			while (t < r) {
				u = Math.min(1.0, r - t);
				v = (float)u * 0.5f;

				final float e1 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(t - f, worldBorder.getBoundNorth() - g) 	 / dd, 0.0, 1.0);
				final float e2 = (float)MathHelper.clamp(1 - VillageBorderRenderer.squareDistance(t + u - f, worldBorder.getBoundNorth() - g) / dd, 0.0, 1.0);

				if (e1 != 0 || e2 != 0) {
					bufferBuilder.vertex(t - f, -h, worldBorder.getBoundNorth() - g)				.texture(m + s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(t + u - f, -h, worldBorder.getBoundNorth() - g)			.texture(m + v + s, y + m + p)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t + u - f, h, worldBorder.getBoundNorth() - g)				.texture(m + v + s, y + m)			.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t - f, h, worldBorder.getBoundNorth() - g)					.texture(m + s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();

					bufferBuilder.vertex(t - f, h, worldBorder.getBoundNorth() - g)					.texture(m + s, y + m)				.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(t + u - f, h, worldBorder.getBoundNorth() - g)				.texture(m + v + s, y + m)			.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t + u - f, h + fade_h, worldBorder.getBoundNorth() - g)	.texture(m + v + s, y + m - fp)		.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(t - f, h + fade_h, worldBorder.getBoundNorth() - g)		.texture(m + s, y + m - fp)			.color(baseColor.r, baseColor.g, baseColor.b, 0).next();

					bufferBuilder.vertex(t - f, -h, worldBorder.getBoundNorth() - g)				.texture(m + s, y + m + p)			.color(baseColor.r, baseColor.g, baseColor.b, e1).next();
					bufferBuilder.vertex(t + u - f, -h, worldBorder.getBoundNorth() - g)			.texture(m + v + s, y + m + p)		.color(baseColor.r, baseColor.g, baseColor.b, e2).next();
					bufferBuilder.vertex(t + u - f, -h - fade_h, worldBorder.getBoundNorth() - g)	.texture(m + v + s, y + m + p+ fp)	.color(baseColor.r, baseColor.g, baseColor.b, 0).next();
					bufferBuilder.vertex(t - f, -h - fade_h, worldBorder.getBoundNorth() - g)		.texture(m + s, y +  m + p+ fp)		.color(baseColor.r, baseColor.g, baseColor.b, 0).next();

				}
				t += 1.0;
				s += 0.5f;
			}
		}
		BufferRenderer.drawWithShader(bufferBuilder.end());
		RenderSystem.enableCull();
		RenderSystem.polygonOffset(0.0f, 0.0f);
		RenderSystem.disablePolygonOffset();
		RenderSystem.disableBlend();
		matrixStack.pop();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.depthMask(true);
	}

	public static double squareDistance(final double d, final double e) {
		return JJUtils.square(d) + JJUtils.square(e);
	}

	public static void render(final Camera camera, final MinecraftClient clinet, final ClientWorld world) {
		if (Config.Generic.SHOW_VILLAGE_BORDER.getBooleanValue()) {
			for (final VillageBoundingBox box : VillageBorderRenderer.villageBoxes) {
				VillageBorderRenderer.renderWorldBorder(camera, world, clinet, box);
			}
		}
	}

	public static void clear(final ClientPlayNetworkHandler handler, final MinecraftClient client) {
		VillageBorderRenderer.villageBoxes.clear();
	}
}
