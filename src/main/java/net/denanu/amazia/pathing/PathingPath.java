package net.denanu.amazia.pathing;

import java.util.List;

import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;

public class PathingPath extends Path {

	public PathingPath(List<PathNode> nodes, BlockPos target, boolean reachesTarget) {
		super(nodes, target, reachesTarget);
	}
}
