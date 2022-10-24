package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.HashSet;

import net.denanu.amazia.pathing.node.PathingNode;
import net.minecraft.util.math.BlockPos;

public class PathingLvl<E extends PathingNode> {
	private HashMap<BlockPos, E> positionMap;
	
	PathingLvl() {
		this.positionMap = new HashMap<BlockPos, E>();
	}
	
	public E get(final BlockPos pos) {
		return this.positionMap.get(pos);
	}
	
	public void add(final BlockPos pos, E node) {
		this.positionMap.put(pos, node);
	}
}
