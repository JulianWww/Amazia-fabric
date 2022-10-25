package net.denanu.amazia.pathing;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;

public class CellMap {
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingNode>>> nodes;
	
	public CellMap() {
		this.nodes = new HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingNode>>>();
	}
	
	public void set(BlockPos pos, PathingNode node) {
		setz(setx(this.nodes, pos.getX()), pos.getZ()).put(pos.getY(), node);
	}
	
	public PathingNode get(BlockPos pos) {
		return this.get(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public PathingNode get(int x, int y, int z) {
		return gety(this.getRemove(x, z), y);
	}
	
	public HashMap<Integer, PathingNode> getRemove(int x, int z){
		return getz(getx(this.nodes, x), z);
	}
	
	private static HashMap<Integer, HashMap<Integer, PathingNode>> setx(HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingNode>>> map, int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, HashMap<Integer, PathingNode>>());
		}
		return map.get(key);
	}
	private static HashMap<Integer, PathingNode> setz(HashMap<Integer, HashMap<Integer, PathingNode>> map, int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, PathingNode>());
		}
		return map.get(key);
	}
	
	private static HashMap<Integer, HashMap<Integer, PathingNode>> getx(HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingNode>>> map, int key) {
		if (!map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
	private static HashMap<Integer, PathingNode> getz(HashMap<Integer, HashMap<Integer, PathingNode>> map, int key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
	private static PathingNode gety(HashMap<Integer, PathingNode> map, int key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
}
