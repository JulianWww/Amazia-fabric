package net.denanu.amazia.pathing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;

public class PathingPath extends Path {

	public PathingPath(List<PathNode> nodes, BlockPos target, boolean reachesTarget) {
		super(nodes, target, reachesTarget);
	}
	
	public PathingPath(List<PathingNode> path, BlockPos target) {
		super(buildPath(path), target, true);
	}

	private static List<PathNode> buildPath(List<PathingNode> path) {
		ArrayList<PathNode> out = new ArrayList<PathNode>();
		out.ensureCapacity(path.size());
		for (ListIterator<PathingNode> iter = path.listIterator(path.size()); iter.hasPrevious();) {
			out.add(iter.previous().getBlockPos().minecraftPathingNode);
		}
		return out;
	}
}
