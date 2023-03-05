package net.denanu.amazia.pathing;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PathSmother {
	public static List<PathingNode> smoothPath(final List<PathingNode> path) {
		final ArrayList<PathingNode> out = new ArrayList<>(path.size());

		int idx = 0;
		for (final PathingNode node : path) {
			if(PathSmother.isNodeNeeded(node, path, idx)) {
				out.add(node);
			}

			idx++;
		}
		return out;
	}

	private static boolean isNodeNeeded(final PathingNode node, final List<PathingNode> path, final int idx) {
		PathSmother.putNode(node.getBlockPos());
		if (idx > 0 && idx < path.size() - 1) {
			return PathSmother.isUnnecesaryNode(node, path, idx);
		}
		return true;
	}

	private static boolean isUnnecesaryNode(final PathingNode node, final List<PathingNode> path, final int idx) {

		final PathingNode predecessor = path.get(idx - 1);
		final PathingNode successor 	= path.get(idx + 1);

		if (predecessor.getY() == node.getY() && node.getY() == successor.getY()) {
			final boolean predXMatch = predecessor.getX() == node.getX();
			final boolean succXMatch = successor.getX()   == node.getX();
			if (predXMatch && succXMatch) {
				return false;
			}

			final boolean predZMatch = predecessor.getZ() == node.getZ();
			final boolean succZMatch = successor.getZ()   == node.getZ();

			if (predZMatch && succZMatch) {
				return false;
			}

			Direction dir;

			if (predXMatch) {
				if (node.getZ() > successor.getZ()) {
					dir = Direction.NORTH;
				}
				else {
					dir = Direction.SOUTH;
				}
			} else if (node.getX() > successor.getX()) {
				dir = Direction.WEST;
			}
			else {
				dir = Direction.EAST;
			}

			final PathingNode corner = predecessor.getByDirection(dir);
			if (corner != null) {
				PathSmother.putTestNode(corner.getBlockPos());
			}
			if (corner != null & corner.getY() == node.getY()) {
				return false;
			}
		}

		return true;
	}

	@Debug
	private static void putNode(final BlockPos pos) {
		if (MinecraftClient.getInstance().world != null) {
			MinecraftClient.getInstance().getServer().getOverworld().setBlockState(pos.down(), Blocks.EMERALD_BLOCK.getDefaultState());
		}
	}

	@Debug
	private static void putTestNode(final BlockPos pos) {
		if (MinecraftClient.getInstance().world != null) {
			MinecraftClient.getInstance().getServer().getOverworld().setBlockState(pos.down(), Blocks.DIAMOND_BLOCK.getDefaultState());
		}
	}
}
