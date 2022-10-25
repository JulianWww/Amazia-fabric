package net.denanu.amazia.pathing;

import java.util.ArrayList;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.util.math.BlockPos;

public class PathingCluster {
	private ArrayList<PathingNode> parents;
	
	public PathingCluster(PathingGraph pathingGraph, BlockPos pos, int lvl) {
		this.parents = new ArrayList<PathingNode>();
		this.register(pathingGraph, pos, lvl);
		
	}
	
	private void register(PathingGraph graph, BlockPos pos, int lvl) {
		PathingLvl.setz(PathingLvl.setx(graph.clusters, lvl), pos.getX()).put(pos.getZ(), this);
	}
	
	public static PathingCluster get(PathingGraph graph, BlockPos pos, int lvl) {
		if (lvl == 5) {
			pos = new BlockPos(0,0,0);
		}
		else {
			pos = PathingUtils.toCluster(pos, lvl+1);
		}
		PathingCluster cluster = PathingLvl.gety(PathingLvl.getz(PathingLvl.getx(graph.clusters, lvl), pos.getX()), pos.getY());
		if (cluster == null) {
			cluster = new PathingCluster(graph, pos, lvl);
		}
		return cluster;
	}
	
	public void add(PathingNode node) {
		this.parents.add(node);
	}
	public void remove(PathingNode node) {
		this.parents.remove(node);
	}
	public void update(PathingGraph graph) {
		for (PathingNode node : this.parents) {
			node.sceduleUpdate(graph);
		}
	}
}
