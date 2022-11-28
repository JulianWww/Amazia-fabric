package net.denanu.amazia.pathing;

import java.util.ArrayList;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;

public class PathingCluster {
	private final ArrayList<PathingNode> parents;
	@Debug
	private final BlockPos pos;
	@Debug
	private final int lvl;

	public PathingCluster(final PathingGraph pathingGraph, final BlockPos pos, final int lvl) {
		this.parents = new ArrayList<PathingNode>();
		this.register(pathingGraph, pos, lvl);
		this.pos = pos;
		this.lvl = lvl;

	}

	private void register(final PathingGraph graph, final BlockPos pos, final int lvl) {
		PathingLvl.setz(PathingLvl.setx(graph.clusters, lvl), pos.getX()).put(pos.getZ(), this);
	}

	public static PathingCluster get(final PathingGraph graph, BlockPos pos, final int lvl) {
		if (lvl == 5) {
			pos = new BlockPos(0,0,0);
		}
		else {
			pos = PathingUtils.toCluster(pos, lvl+1);
		}
		PathingCluster cluster = PathingLvl.gety(PathingLvl.getz(PathingLvl.getx(graph.clusters, lvl), pos.getX()), pos.getZ());
		if (cluster == null) {
			cluster = new PathingCluster(graph, pos, lvl);
		}
		return cluster;
	}

	public void add(final PathingNode node) {
		this.parents.add(node);
	}
	public void remove(final PathingNode node) {
		this.parents.remove(node);
	}
	public void update(final PathingGraph graph) {
		for (final PathingNode node : this.parents) {
			PathingCluster.debugUpdate(graph, node);
			node.sceduleUpdate(graph);
		}
	}

	@Debug
	public static void debugUpdate(final PathingGraph graph, final PathingNode node) {
		//graph.getWorld().setBlockState(new BlockPos(node.getBlockPos().getX(), -64, node.getBlockPos().getZ()),
		//		Blocks.REDSTONE_BLOCK.getDefaultState());
	}
}
