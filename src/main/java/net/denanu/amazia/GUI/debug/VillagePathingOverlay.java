package net.denanu.amazia.GUI.debug;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.node.ClientPathingNode;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class VillagePathingOverlay {
	public static ArrayList<ClientPathingNode> nodes = new ArrayList<>();
	public static long id=-1;
	public static boolean render = false;
	public static BlockPos villageLoc = new BlockPos(0,0,0);

	public static Semaphore sem = new Semaphore(1);

	public static int redColor = 0xFF0000;

	public static void render(final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Camera cam, final GameRenderer renderer) {

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

		try {
			VillagePathingOverlay.sem.acquire();
			for (final ClientPathingNode node : VillagePathingOverlay.nodes) {
				if (!frustum.isVisible(new Box(node))) {
					continue;
				}
				for (final BlockPos other: node.ajacents) {
					if (!frustum.isVisible(new Box(other))) {
						continue;
					}
					VillagePathingOverlay.drawLine(node, other, VillagePathingOverlay.redColor, bufferBuilder, cam);
				}
			}
		} catch (final InterruptedException e) {
			Amazia.LOGGER.error(e.toString());
		}
		VillagePathingOverlay.sem.release();

		matrices.pop();

		tessellator.draw();
		RenderSystem.lineWidth(1.0f);
		RenderSystem.enableBlend();
		RenderSystem.enableTexture();
	}

	public static void addNode(final ClientPathingNode node) {
		try {
			VillagePathingOverlay.sem.acquire();
			VillagePathingOverlay.nodes.remove(node);
			VillagePathingOverlay.nodes.add(node);

		} catch (final InterruptedException e) {
			Amazia.LOGGER.error(e.toString());
		}

		VillagePathingOverlay.sem.release();
	}

	private static void drawLine(final BlockPos from, final BlockPos to, final int color, final BufferBuilder bufferBuilder, final Camera camera) {
		VillagePathingOverlay.drawLine(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), color, bufferBuilder, camera);
	}

	private static void drawLine(final float fx, final float fy, final float fz, final float tx, final float ty, final float tz, final int color, final BufferBuilder bufferBuilder, final Camera camera) {
		final double cameraX = camera.getPos().x;
		final double cameraY = camera.getPos().y - .005D;
		final double cameraZ = camera.getPos().z;

		final float yOffset = 0.07f;

		bufferBuilder.vertex(fx - cameraX + 0.5, fy - cameraY + yOffset, fz - cameraZ + 0.5).color(0f, 0f, 0f, 0f).next();
		bufferBuilder.vertex(fx - cameraX + 0.5, fy - cameraY + yOffset, fz - cameraZ + 0.5).color(0f, 1f, 0f, 1f).next();
		bufferBuilder.vertex(tx - cameraX + 0.5, ty - cameraY + yOffset, tz - cameraZ + 0.5).color(0f, 1f, 0f, 1f).next();
		bufferBuilder.vertex(tx - cameraX + 0.5, ty - cameraY + yOffset, tz - cameraZ + 0.5).color(0f, 0f, 0f, 0f).next();
		//bufferBuilder.nextElement();
	}

	public static void render(final WorldRenderContext wrc)
	{
		if (VillagePathingOverlay.render)
		{
			VillagePathingOverlay.render(wrc.matrixStack(), wrc.consumers(), wrc.camera(), wrc.gameRenderer());
		}
	}

	public static void setup() {
		final ArrayList<BlockPos> ajacents = new ArrayList<>();
		ajacents.add(new BlockPos(1, 0, 0));
		VillagePathingOverlay.nodes.add(new ClientPathingNode(ajacents, new BlockPos(0,0,0)));
	}
}
