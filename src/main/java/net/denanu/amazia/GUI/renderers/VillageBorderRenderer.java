package net.denanu.amazia.GUI.renderers;

import java.util.HashSet;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.village.VillageBoundingBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class VillageBorderRenderer {
	private static final Identifier FORCEFIELD = new Identifier("textures/misc/forcefield.png");
	public static HashSet<VillageBoundingBox> villageBoxes = new HashSet<VillageBoundingBox>();

	public static void renderWorldBorder(final Camera camera, final ClientWorld world, final MinecraftClient client, final VillageBoundingBox worldBorder) {
		float v;
		double u;
		double t;
		float s;
		final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		final double d = client.options.getClampedViewDistance() * 16;
		if (camera.getPos().x < worldBorder.getBoundEast() - d && camera.getPos().x > worldBorder.getBoundWest() + d && camera.getPos().z < worldBorder.getBoundSouth() - d && camera.getPos().z > worldBorder.getBoundNorth() + d) {
			return;
		}
		double e = 1.0 - worldBorder.getDistanceInsideBorder(camera.getPos().x, camera.getPos().z) / d;
		e = Math.pow(e, 4.0);
		e = MathHelper.clamp(e, 0.0, 1.0);
		final double f = camera.getPos().x;
		final double g = camera.getPos().z;

		final double h = 2.0f;

		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		RenderSystem.setShaderTexture(0, VillageBorderRenderer.FORCEFIELD);
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		final MatrixStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.push();
		RenderSystem.applyModelViewMatrix();
		final int i = worldBorder.getStage().getColor();
		final float j = (i >> 16 & 0xFF) / 255.0f;
		final float k = (i >> 8 & 0xFF) / 255.0f;
		final float l = (i & 0xFF) / 255.0f;
		RenderSystem.setShaderColor(j, k, l, (float)e);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.polygonOffset(-3.0f, -3.0f);
		RenderSystem.enablePolygonOffset();
		RenderSystem.disableCull();
		final float m = Util.getMeasuringTimeMs() % 3000L / 3000.0f;
		final float p = (float) h;//(float)(h - MathHelper.fractionalPart(camera.getPos().y));
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		double q = Math.max(MathHelper.floor(g - d), worldBorder.getBoundNorth());
		double r = Math.min(MathHelper.ceil(g + d), worldBorder.getBoundSouth());
		if (f > worldBorder.getBoundEast() - d) {
			s = 0.0f;
			t = q;
			while (t < r) {
				u = Math.min(1.0, r - t);
				v = (float)u * 0.5f;
				bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h, t - g).texture(m - s, m + p).next();
				bufferBuilder.vertex(worldBorder.getBoundEast() - f, -h, t + u - g).texture(m - (v + s), m + p).next();
				bufferBuilder.vertex(worldBorder.getBoundEast() - f, h, t + u - g).texture(m - (v + s), m + 0.0f).next();
				bufferBuilder.vertex(worldBorder.getBoundEast() - f, h, t - g).texture(m - s, m + 0.0f).next();
				t += 1.0;
				s += 0.5f;
			}
		}
		if (f < worldBorder.getBoundWest() + d) {
			s = 0.0f;
			t = q;
			while (t < r) {
				u = Math.min(1.0, r - t);
				v = (float)u * 0.5f;
				bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h, t - g).texture(m + s, m + p).next();
				bufferBuilder.vertex(worldBorder.getBoundWest() - f, -h, t + u - g).texture(m + v + s, m + p).next();
				bufferBuilder.vertex(worldBorder.getBoundWest() - f, h, t + u - g).texture(m + v + s, m + 0.0f).next();
				bufferBuilder.vertex(worldBorder.getBoundWest() - f, h, t - g).texture(m + s, m + 0.0f).next();
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
				bufferBuilder.vertex(t - f, -h, worldBorder.getBoundSouth() - g).texture(m + s, m + p).next();
				bufferBuilder.vertex(t + u - f, -h, worldBorder.getBoundSouth() - g).texture(m + v + s, m + p).next();
				bufferBuilder.vertex(t + u - f, h, worldBorder.getBoundSouth() - g).texture(m + v + s, m + 0.0f).next();
				bufferBuilder.vertex(t - f, h, worldBorder.getBoundSouth() - g).texture(m + s, m + 0.0f).next();
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
				bufferBuilder.vertex(t - f, -h, worldBorder.getBoundNorth() - g).texture(m - s, m + p).next();
				bufferBuilder.vertex(t + u - f, -h, worldBorder.getBoundNorth() - g).texture(m - (v + s), m + p).next();
				bufferBuilder.vertex(t + u - f, h, worldBorder.getBoundNorth() - g).texture(m - (v + s), m + 0.0f).next();
				bufferBuilder.vertex(t - f, h, worldBorder.getBoundNorth() - g).texture(m - s, m + 0.0f).next();
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

	public static void render(final Camera camera, final MinecraftClient clinet, final ClientWorld world) {
		for (final VillageBoundingBox box : VillageBorderRenderer.villageBoxes) {
			VillageBorderRenderer.renderWorldBorder(camera, world, clinet, box);
		}
	}

	public static void clear(final MinecraftClient client) {
		VillageBorderRenderer.villageBoxes.clear();
	}
}
