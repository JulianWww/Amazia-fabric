package net.denanu.amazia.pathing;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.pathing.node.PathingNode;
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
		if (idx > 0 && idx < path.size() - 2) {
			return PathSmother.isUnnecesaryNode(node, path, idx);
		}
		return true;
	}

	private static boolean isUnnecesaryNode(final PathingNode node, final List<PathingNode> path, final int idx) {

		final PathingNode predecessor = path.get(idx - 1);
		final PathingNode successor   = path.get(idx + 1);

		if (predecessor.getY() == node.getY() && node.getY() == successor.getY()) {
			final boolean predXMatch = predecessor.getX() == node.getX();

			if (predecessor.getX() == successor.getX() || predecessor.getZ() == successor.getZ()) {
				return true;
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
			if (corner != null && corner.getY() == node.getY()) {
				return false;
			}
		}

		return true;
	}
}
