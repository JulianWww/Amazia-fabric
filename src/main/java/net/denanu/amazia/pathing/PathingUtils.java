package net.denanu.amazia.pathing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.utils.queue.PriorityElement;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public class PathingUtils {
	public static int getHeigestLevel(BlockPos pos) {
		if (isLvl5(pos)) return 5;
		if (isLvl4(pos)) return 4;
		if (isLvl3(pos)) return 3;
		if (isLvl2(pos)) return 2;
		if (isLvl1(pos)) return 1;
		return 0;
	}
	
	private static boolean isLvl1(BlockPos pos) {
		return isLvl(pos, 4);
	}
	private static boolean isLvl2(BlockPos pos) {
		return isLvl(pos, 8);
	}
	private static boolean isLvl3(BlockPos pos) {
		return isLvl(pos, 16);
	}
	private static boolean isLvl4(BlockPos pos) {
		return isLvl(pos, 32);
	}
	private static boolean isLvl5(BlockPos pos) {
		return isLvl(pos, 64);
	}
	
	private static boolean isLvl(BlockPos pos, int size) {
		return isLvlInDim(size, pos.getX()) || isLvlInDim(size, pos.getZ()); //|| isLvlInDim(size, pos.getY())
	}
	private static boolean isLvlInDim(int size, int x) {
		return x % size == 0 || x % size == 1 || x % size == 1-size;
	}
	
	private static BlockPos toClusterPos(BlockPos pos, int size) {
		return new BlockPos(
				toClusterPos(size, pos.getX()),
				size,
				toClusterPos(size, pos.getZ())
			);
	}
	
	private static int toClusterPos(int size, int val) {
		if (val > 0) {
			val = val - 1;
			return val - val % size + 1;
		}
		return val - val % size;
	}
	
	public static BlockPos toCluster(BlockPos pos, int lvl) {
		if (lvl == 0) { return null; }
		if (lvl == 1) { return toClusterPos(pos, 4); }
		if (lvl == 2) { return toClusterPos(pos, 8); }
		if (lvl == 3) { return toClusterPos(pos, 16); }
		if (lvl == 4) { return toClusterPos(pos, 32); }
		return toClusterPos(pos, 64);
	}
	
	
	public static Pair<Integer, HashSet<PathingEdge>> getAbstractEdges(PathingNode start, HashSet<PathingNode> endNodes) {
		PriorityQueue<PriorityElement<PathingNode>> nodeQueue = new PriorityQueue<PriorityElement<PathingNode>>(PriorityElement.comparator);
		HashSet<PathingEdge> outs = new HashSet<PathingEdge>();
		HashSet<PathingNode> visited = new HashSet<PathingNode>();
		
		nodeQueue.add(new PriorityElement<PathingNode>(0, start));
		visited.add(start);
		
		int visitedCount = 0;
		
		PriorityElement<PathingNode> current;
		PathingNode next;
		while (!nodeQueue.isEmpty()) {
			current = nodeQueue.poll();
			visitedCount += 1;
			for (PathingEdge edge : current.getRight().edges) {
				next = edge.to(current.getRight());
				if (!visited.contains(next)) {
					visited.add(next);
					
					next.from = edge;
					int nextDist = current.getLeft() + edge.getPath().getLength();
					
					// build nodes if possible
					if (next.getParent() != null) { // && endNodes.contains(next.getParent())
						outs.add(PathingEdge.build(next, start, nextDist));
					}
					
					nodeQueue.add(new PriorityElement<PathingNode>(nextDist, next));
				}
			}
		}
		return new Pair<>(visitedCount, outs);
	}
}
