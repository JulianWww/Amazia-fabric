package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.HashSet;

import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.util.math.BlockPos;

public class PathingLvl {
	// positions are x, z, y
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, BasePathingNode>>> positionMap;
	
	PathingLvl() {
		this.positionMap = new HashMap<Integer, HashMap<Integer, HashMap<Integer, BasePathingNode>>>();
	}
	
	public BasePathingNode get(final BlockPos pos) {
		return gety(getz(getx(positionMap, pos.getX()), pos.getZ()), pos.getY());
	}
	
	public void add(final BlockPos pos, BasePathingNode node) {
		setz(setx(this.positionMap, pos.getX()), pos.getZ()).put(pos.getY(), node);
	}
	
	public HashMap<Integer, BasePathingNode> getRemove(final int x, final int z) {
		return getz(getx(this.positionMap, x), z);
	}
	
	
	
	
	static <E> HashMap<Integer, HashMap<Integer, E>> setx(HashMap<Integer, HashMap<Integer, HashMap<Integer, E>>> map, int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, HashMap<Integer, E>>());
		}
		return map.get(key);
	}
	static <E> HashMap<Integer, E> setz(HashMap<Integer, HashMap<Integer, E>> map, int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, E>());
		}
		return map.get(key);
	}
	
	static <E> HashMap<Integer, HashMap<Integer, E>> getx(HashMap<Integer, HashMap<Integer, HashMap<Integer, E>>> map, int key) {
		if (!map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
	static <E> HashMap<Integer, E> getz(HashMap<Integer, HashMap<Integer, E>> map, int key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
	static <E> E gety(HashMap<Integer, E> map, int key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
}
