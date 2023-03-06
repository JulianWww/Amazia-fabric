package net.denanu.amazia.pathing;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;

public class PathingPath extends Path {
	public PathingPath(final List<PathingNode> path, final BlockPos target) {
		super(PathingPath.buildPath(path), target, true);
	}

	private static List<PathNode> buildPath(List<PathingNode> path) {
		path = PathSmother.smoothPath(path);
		final ArrayList<PathNode> out = new ArrayList<>();
		out.ensureCapacity(path.size());
		for (final ListIterator<PathingNode> iter = path.listIterator(path.size()); iter.hasPrevious();) {
			out.add(iter.previous().getBlockPos().minecraftPathingNode);
		}
		return out;
	}
}
