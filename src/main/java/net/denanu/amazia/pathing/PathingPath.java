package net.denanu.amazia.pathing;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;

public class PathingPath extends Path {
	public PathingPath(final List<PathingNode> path, final BlockPos target, final BlockPos origin) {
		super(PathingPath.buildPath(path, origin), target, true);
	}

	private static List<PathNode> buildPath(List<PathingNode> path, final BlockPos origin) {
		path = PathSmother.smoothPath(path);
		final ArrayList<PathNode> out = new ArrayList<>();
		out.ensureCapacity(path.size()+1);
		out.add(new PathingCell(origin).minecraftPathingNode);
		final ListIterator<PathingNode> iter = path.listIterator(path.size());
		for (; iter.hasPrevious();) {
			out.add(iter.previous().getBlockPos().minecraftPathingNode);
		}
		return out;
	}
}
