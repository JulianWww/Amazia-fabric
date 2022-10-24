package net.denanu.amazia.pathing.edge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.denanu.amazia.pathing.interfaces.PathingPathInterface;
import net.denanu.amazia.pathing.interfaces.PathingUpdateInterface;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.utils.ArrayIterator;
import net.denanu.amazia.utils.ReverseArrayIterator;
import net.minecraft.util.math.BlockPos;

public class PathingEdge implements PathingPathInterface {
	protected final PathingNode startNode;
	protected final PathingNode endNode;
	protected EdgePath path;
	
	private HashSet<PathingUpdateInterface> pathes;
	
	public PathingEdge(PathingNode from, PathingNode end, PathingPathInterface[] path, int length) {
		this.startNode = from;
		this.endNode = end;
		this.path = new EdgePath(path, length, from.getLvl());
	}
	
	public EdgePath getPath() {
		return this.path;
	}

	public boolean connects(BlockPos pos) {
		return this.startNode.getBlockPos().equals(pos) || this.endNode.getBlockPos().equals(pos);
	}
	
	public PathingNode to(PathingNode from) {
		if (this.startNode.lvllessEquals(from)) {
			return this.endNode;
		}
		return this.startNode;
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

	public static PathingEdge build(PathingNode tail, PathingNode head, int distance) {
		List<PathingEdge> nodes = new LinkedList<PathingEdge>();
		PathingNode iter = tail;
		while (iter != head) {
			nodes.add(0, iter.from);
			iter = iter.from.to(iter);
		}
		return new PathingEdge(head.getParent(), tail.getParent(), nodes.toArray(new PathingEdge[nodes.size()]), distance);
	}

	@Override
	public void addPath(PathingUpdateInterface path) {
		this.pathes.add(path);
	}

	@Override
	public void removePath(PathingUpdateInterface path) {
		this.pathes.remove(path);
	}
	
	private Iterator<PathingPathInterface> getPathIterator(PathingNode endNode) {
		if (endNode.lvllessEquals(this.startNode)) {
			return new ArrayIterator<PathingPathInterface>(this.path.getPath());
		}
		return new ReverseArrayIterator<PathingPathInterface>(this.path.getPath());
	}
	
	public List<PathingNode> toPath(PathingNode endNode) {
		List<PathingNode> output = new LinkedList<PathingNode>();
		Iterator<PathingPathInterface> iter = this.getPathIterator(endNode);
		PathingPathInterface subpath;
		PathingEdge edge;
		boolean skiped = false;
		
		while (iter.hasNext()) {
			subpath = iter.next();
			if (subpath instanceof PathingNode node) {
				if (skiped) {
					output.add(node);
				}
				else {
					skiped = true;
				}
				endNode = node;
			} 
			else {
				edge = ((PathingEdge)subpath);
				output.addAll(edge.toPath(endNode));
				endNode = edge.to(endNode);
			}
		}
		return output;
	}
}
