package net.denanu.amazia.GUI.debug;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.node.ClientPathingNode;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class VillageProjectileTargetingDebugOverlay {
	public static ArrayList<ClientPathingNode> nodes = new ArrayList<ClientPathingNode>();
	public static boolean render = false;

	public static final String KEY = "key." + Amazia.MOD_ID + ".projectileOverlay";
	public static KeyBinding keybind;

	private static float v_x = 1.0f;
	private static float v_y = 1.0f;
	private static float v_z = 1.0f;
	private static float g = 0.05f;
	private static float d = -0.01f;

	public static void render(final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Camera cam, final GameRenderer renderer) {
		VillageProjectileTargetingDebugOverlay.d = -0.0104f;
		VillageProjectileTargetingDebugOverlay.g = 0.047f;
		VillageProjectileTargetingDebugOverlay.v_x = VillageProjectileTargetingDebugOverlay.v_y = VillageProjectileTargetingDebugOverlay.v_z = 0.1f;

		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.disableTexture();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(5.0f);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

		final Vec3d camPos = cam.getPos();

		matrices.push();

		final Frustum frustum = new Frustum(matrices.peek().getPositionMatrix(), RenderSystem.getProjectionMatrix());
		frustum.setPosition(camPos.x, camPos.y, camPos.z);

		float time;
		for (time = 0; time < 100; time = time + 1) {
			VillageProjectileTargetingDebugOverlay.drawLine(time, bufferBuilder, cam, 1);
		}
		VillageProjectileTargetingDebugOverlay.drawLine(time, bufferBuilder, cam, 0);
		VillageProjectileTargetingDebugOverlay.drawLine(0, bufferBuilder, cam, 0);
		for (time = 0; time < 100; time = time + 1) {
			VillageProjectileTargetingDebugOverlay.drawLineOld(time, bufferBuilder, cam, 1);
		}

		matrices.pop();

		tessellator.draw();
		RenderSystem.lineWidth(1.0f);
		RenderSystem.enableBlend();
		RenderSystem.enableTexture();
	}

	public static float getX(final float t) {
		return (float) (VillageProjectileTargetingDebugOverlay.v_x * Math.exp(VillageProjectileTargetingDebugOverlay.d*t)-VillageProjectileTargetingDebugOverlay.v_x) / VillageProjectileTargetingDebugOverlay.d;
	}

	public static float getZ(final float t) {
		return (float) ((VillageProjectileTargetingDebugOverlay.v_z * Math.exp(VillageProjectileTargetingDebugOverlay.d*t)-VillageProjectileTargetingDebugOverlay.v_z) / VillageProjectileTargetingDebugOverlay.d);
	}

	public static float getY(final float t) {
		final float a = VillageProjectileTargetingDebugOverlay.v_y - VillageProjectileTargetingDebugOverlay.g/VillageProjectileTargetingDebugOverlay.d;
		return (float) (a*Math.exp(VillageProjectileTargetingDebugOverlay.d*t)+VillageProjectileTargetingDebugOverlay.g*t - a)/VillageProjectileTargetingDebugOverlay.d;
	}


	private static void drawLine(final float time, final BufferBuilder bufferBuilder, final Camera camera, final float color) {
		VillageProjectileTargetingDebugOverlay.drawLine(
				VillageProjectileTargetingDebugOverlay.getOldX(time),
				VillageProjectileTargetingDebugOverlay.getOldY(time),
				VillageProjectileTargetingDebugOverlay.getOldZ(time),
				bufferBuilder,
				camera,
				1,1,1,color);
	}

	private static void drawLineOld(final float time, final BufferBuilder bufferBuilder, final Camera camera, final float color) {
		VillageProjectileTargetingDebugOverlay.drawLine(
				VillageProjectileTargetingDebugOverlay.getX(time),
				VillageProjectileTargetingDebugOverlay.getY(time),
				VillageProjectileTargetingDebugOverlay.getZ(time),
				bufferBuilder,
				camera,
				0,0,1,color);
	}

	public static float getOldZ(final float time) {
		return VillageProjectileTargetingDebugOverlay.v_z * time;
	}

	public static float getOldX(final float time) {
		return VillageProjectileTargetingDebugOverlay.v_x * time;
	}

	public static float getOldY(final float time) {
		return VillageProjectileTargetingDebugOverlay.v_y*time - VillageProjectileTargetingDebugOverlay.g * time*time/2;
	}

	private static void drawLine(final float tx, final float ty, final float tz, final BufferBuilder bufferBuilder, final Camera camera, final float r, final float g, final float b, final float alpha) {
		final double cameraX = camera.getPos().x;
		final double cameraY = camera.getPos().y - .005D;
		final double cameraZ = camera.getPos().z;

		bufferBuilder.vertex(tx - cameraX, ty - cameraY, tz - cameraZ).color(r, g, b, alpha).next();
	}

	public static void render(final WorldRenderContext wrc)
	{
		if (true)
		{
			VillageProjectileTargetingDebugOverlay.render(wrc.matrixStack(), wrc.consumers(), wrc.camera(), wrc.gameRenderer());
		}
	}

	public static void onEndTick(final ServerWorld world) {
		if (world.getTime() % 20 == 1) {
			final ArrowEntity arrowEntity = new ArrowEntity(world, 0, 0,0);
			arrowEntity.setPos(0, 0, 0);
			arrowEntity.setVelocity(VillageProjectileTargetingDebugOverlay.v_x, VillageProjectileTargetingDebugOverlay.v_y, VillageProjectileTargetingDebugOverlay.v_z);
			world.spawnEntity(arrowEntity);
		}
	}
}
