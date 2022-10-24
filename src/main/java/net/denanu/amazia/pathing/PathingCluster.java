package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.HashSet;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.util.math.BlockPos;

public class PathingCluster {
	private HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> lvl1;
	private HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> lvl2;
	private HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> lvl3;
	private HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> lvl4;
	private HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> lvl5;
	
	public PathingCluster() {
		this.lvl1 = new HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>>();
		this.lvl2 = new HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>>();
		this.lvl3 = new HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>>();
		this.lvl4 = new HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>>();
		this.lvl5 = new HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>>();
	}
	
	public void addToCluster(BlockPos pos, int lvl, PathingNode node) {
		pos = PathingUtils.toCluster(pos, lvl);
		if (lvl == 1) { addToCluster(pos, node, lvl1); }
		if (lvl == 2) { addToCluster(pos, node, lvl2); }
		if (lvl == 3) { addToCluster(pos, node, lvl3); }
		if (lvl == 4) { addToCluster(pos, node, lvl4); }
		if (lvl == 5) { addToCluster(pos, node, lvl5); }
		return;
	}
	
	public HashSet<PathingNode> getClusterEdges(BlockPos pos, int lvl) {
		BlockPos cluster = PathingUtils.toCluster(pos, lvl);
		if (lvl == 1) {
			return get(cluster, lvl1); 
		}
		if (lvl == 2) { 
			return get(cluster, lvl2);
		}
		if (lvl == 3) { 
			return get(cluster, lvl3);
		}
		if (lvl == 4) { 
			return get(cluster, lvl4);
		}
		if (lvl == 5) { 
			return get(cluster, lvl5);
		}
		return null;
	}
	
	private static void addToCluster(BlockPos pos, PathingNode node, HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> map) {
		PathingCluster.getz(PathingCluster.getx(map, pos.getX()), pos.getZ()).add(node);
	}
	
	private static HashSet<PathingNode> get(BlockPos pos, HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> map) {
		return PathingCluster.getz(PathingCluster.getx(map, pos.getX()), pos.getZ());
	}
	
	private static HashMap<Integer, HashSet<PathingNode>> getx(HashMap<Integer, HashMap<Integer, HashSet<PathingNode>>> map, int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, HashSet<PathingNode>>());
		}
		return map.get(key);
	}
	
	private static HashSet<PathingNode> getz(HashMap<Integer, HashSet<PathingNode>> map, int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashSet<PathingNode>());
		}
		return map.get(key);
	}
}
