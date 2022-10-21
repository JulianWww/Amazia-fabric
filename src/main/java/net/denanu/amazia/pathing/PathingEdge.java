package net.denanu.amazia.pathing;

import java.util.Comparator;

import net.minecraft.util.math.BlockPos;

public class PathingEdge {
	public static final Comparator <PathingEdge> comparator = getComparator();
	
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
	
	private static Comparator <PathingEdge> getComparator() {
		return new Comparator<PathingEdge>()
        {
            public int compare ( PathingEdge e1 , PathingEdge e2 )
            {
                return e1.getPath().getLength() - e2.getPath().getLength();
            }
            
            /*private static Pair<PathingNode, PathingNode> sortEndPoints(PathingEdge e) {
            	return isLarger(e.startNode.getBlockPos(), e.endNode.getBlockPos()) ? new Pair<>(e.startNode, e.endNode) : new Pair<>(e.endNode, e.startNode);
            }
            
            private static boolean isLarger(BlockPos pos1, BlockPos pos2) {
            	if (pos1.getX() == pos2.getX()) {
            		if (pos1.getY() == pos2.getY()) {
            			return pos1.getZ() > pos2.getZ();
            		}
            		return pos1.getY() > pos2.getY();
            	}
            	return pos1.getX() > pos2.getX();
            }*/
        };
	}

	public boolean connects(BlockPos pos) {
		return this.startNode.getBlockPos().equals(pos) || this.endNode.getBlockPos().equals(pos);
	}

}
