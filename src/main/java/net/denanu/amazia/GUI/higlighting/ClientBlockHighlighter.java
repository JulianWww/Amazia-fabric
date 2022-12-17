package net.denanu.amazia.GUI.higlighting;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import fi.dy.masa.malilib.util.Color4f;
import net.denanu.clientblockhighlighting.config.Config;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class ClientBlockHighlighter {
	private static Semaphore mutex = new Semaphore(1);
	private static final Identifier FORCEFIELD = new Identifier("textures/misc/forcefield.png");

	protected static final int RANGE = 10000*1000;

	private static HashSet<BlockPos> poses = new HashSet<>();

	public static void highlight(final BlockPos pos) {
		try {
			ClientBlockHighlighter.mutex.acquire();
			ClientBlockHighlighter.poses.add(pos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ClientBlockHighlighter.mutex.release();
	}

	public static void unHighlight(final BlockPos pos) {
		try {
			ClientBlockHighlighter.mutex.acquire();
			ClientBlockHighlighter.poses.remove(pos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ClientBlockHighlighter.mutex.release();
	}

	public static void highlight(final BlockPos pos, final boolean active) {
		if (active) {
			ClientBlockHighlighter.highlight(pos);
		}
		else {
			ClientBlockHighlighter.unHighlight(pos);
		}
	}

	public static <VillageBoundingBox> void render(final MatrixStack matrixStack, @Nullable final VertexConsumerProvider consumers, final Camera camera, final GameRenderer gameRenderer, final ClientWorld world) {
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		//Mod.LOGGER.info(Float.toString(a));
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

		//final Vec3d camPos = camera.getPos();

		for (BlockPos pos : ClientBlockHighlighter.poses) {

			ClientBlockHighlighter.drawLineBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos), Config.Colors.OUTLINE_COLOR.getColor());
		}

		tessellator.draw();

		for (BlockPos pos : ClientBlockHighlighter.poses) {
			ClientBlockHighlighter.drawAreaBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos), Config.Colors.FILL_COLOR.getColor());
		}
		RenderSystem.enableTexture();
		//RenderSystem.lineWidth(1.0f);
	}

	public static void drawLineBox(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel, final Color4f color) {
		ClientBlockHighlighter.draw(pos, buf, camera, voxel, color, ClientBlockHighlighter::renderDrawLineBox);
	}

	public static void drawAreaBox(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel, final Color4f color) {
		ClientBlockHighlighter.draw(pos, buf, camera, voxel, color, ClientBlockHighlighter::drawBox);
	}

	private static void draw(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel, final Color4f color, final Renderer renderer) {
		final double cameraX = camera.getPos().x;
		final double cameraY = camera.getPos().y;
		final double cameraZ = camera.getPos().z;

		double offset = 0.01;

		double xpos = pos.getX() - cameraX;
		double ypos = pos.getY() - cameraY;
		double zpos = pos.getZ() - cameraZ;

		double x, y, z, topx, topy, topz;

		if (voxel.isEmpty()) {
			x = xpos;
			y = ypos;
			z = zpos;
			topx = x+1;
			topy = y+1;
			topz = z+1;
		}
		else  {
			x = xpos - offset + voxel.getMin(Direction.Axis.X);
			y = ypos - offset + voxel.getMin(Direction.Axis.Y);
			z = zpos - offset + voxel.getMin(Direction.Axis.Z);

			topx = xpos + offset + voxel.getMax(Direction.Axis.X);
			topy = ypos + offset + voxel.getMax(Direction.Axis.Y);
			topz = zpos + offset + voxel.getMax(Direction.Axis.Z);
		}

		renderer.render(x, y, z, topx, topy, topz, buf, color);
	}

	private static void renderDrawLineBox(final double x, final double y, final double z, final double topx, final double topy, final double topz, final BufferBuilder buf, final Color4f color) {
		ClientBlockHighlighter.drawYalignedSquare	(x, 	y, 		z, 		topx, 	topz, 		buf,	color.intValue);
		ClientBlockHighlighter.drawYalignedSquare	(x, 	topy, 	z, 		topx, 	topz, 		buf,	color.intValue);

		ClientBlockHighlighter.drawLine			(x, 	y, 		z, 		x, 		topy, z, 	buf,	color.intValue);
		ClientBlockHighlighter.drawLine			(topx, 	y, 		z, 		topx, 	topy, z, 	buf, 	color.intValue);
		ClientBlockHighlighter.drawLine			(x, 	y, 		topz, 	x, 		topy, topz, buf,	color.intValue);
		ClientBlockHighlighter.drawLine			(topx, 	y, 		topz, 	topx, 	topy, topz,	buf,	color.intValue);
	}

	private static void drawBox(final double x, final double y, final double z, final double topx, final double topy, final double topz, final BufferBuilder buf, final Color4f color) {
		DebugRenderer.drawBox(x, y, z, topx, topy, topz, color.r, color.g, color.b, color.a);
	}

	public static void drawYalignedSquare(final double x, final double y, final double z, final double topx, final double topz, final BufferBuilder buf, final int color) {
		buf.vertex(x, 		y, z).		color(0).next();
		buf.vertex(x, 		y, z).		color(color).next();
		buf.vertex(x, 		y, topz).	color(color).next();
		buf.vertex(topx, 	y, topz).	color(color).next();
		buf.vertex(topx, 	y, z).		color(color).next();
		buf.vertex(x, 		y, z).		color(color).next();
		buf.vertex(x, 		y, z).		color(0).next();
	}



	public static void drawLine(final double fx, final double fy, final double fz, final double tx, final double ty, final double tz, final BufferBuilder buf, final int color) {
		buf.vertex(fx, fy, fz).color(0).next();
		buf.vertex(fx, fy, fz).color(color).next();
		buf.vertex(tx, ty, tz).color(color).next();
		buf.vertex(tx, ty, tz).color(0).next();
	}

	public static void render(final WorldRenderContext wrc)
	{
		if (!ClientBlockHighlighter.poses.isEmpty() && Config.Generic.SHOULD_RENDER.getBooleanValue())
		{
			ClientBlockHighlighter.render(wrc.matrixStack(), wrc.consumers(), wrc.camera(), wrc.gameRenderer(), wrc.world());
		}
	}

	interface Renderer {
		void render(final double x, final double y, final double z, final double topx, final double topy, final double topz, final BufferBuilder buf, final Color4f color);
	}
}
