package net.denanu.amazia.pathing;

import java.util.Comparator;

import net.minecraft.util.math.BlockPos;

public class PathingEdge {
	protected PathingNode startNode;
	protected PathingNode endNode;
	protected EdgePath path;
	
	public PathingEdge(PathingNode from, PathingNode end, PathingNode[] path, int length) {
		this.startNode = from;
		this.endNode = end;
		this.path = new EdgePath(path, length);
	}
	
	public EdgePath getPath() {
		return this.path;
	}

	public boolean connects(BlockPos pos) {
		return this.startNode.getBlockPos().equals(pos) || this.endNode.getBlockPos().equals(pos);
	}
	
	@Override
	public int hashCode() {
		int result = this.startNode.getBlockPos().getX();
		result = result << 4 + this.startNode.getBlockPos().getY();
		result = result << 4 + this.startNode.getBlockPos().getZ();
		
		result = result << 4 + this.endNode.getBlockPos().getX();
		result = result << 4 + this.endNode.getBlockPos().getY();
		result = result << 4 + this.endNode.getBlockPos().getZ();
		return result;
	}
	
	private static boolean equalsInAxis(BlockPos x1, BlockPos x2, BlockPos y1, BlockPos y2) {
		return (x1 == y1 && x2 == y2) || (x1 == y2 && x2 == y1);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PathingEdge edge) {
			return (equalsInAxis(this.startNode.getBlockPos(), this.endNode.getBlockPos(), edge.startNode.getBlockPos(), edge.endNode.getBlockPos()));
		}
		return false;
	}

}
