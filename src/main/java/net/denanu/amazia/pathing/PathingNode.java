package net.denanu.amazia.pathing;

import java.util.Set;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingNode implements PathingUpdateInterface {
	private BlockPos pos;
	private int lvl;
	protected PathingNode parent, child;
	protected boolean queued;
	protected boolean destroyed;
	protected Set<PathingUpdateInterface> pathes;
	public Set<PathingEdge> edges;
	
	public PathingNode(BlockPos pos, PathingGraph graph) {
		this.lvl = 0;
		this.init(pos, graph);
		this.buildParents(pos, graph);
	}
	public PathingNode(BlockPos pos, PathingGraph graph, int lvl) {
		this.lvl = lvl;
		this.init(pos, graph);
	}
	private void init(BlockPos pos, PathingGraph graph) {
		this.pos = pos;
		this.parent = null;
		this.child = null;
	}
	
	private void buildParents(BlockPos pos, PathingGraph graph) {
		buildParents(pos, graph, 1, PathingUtils.getHeigestLevel(pos), this);
	}
	private static void buildParents(BlockPos pos, PathingGraph graph, int height, int maxHeight, PathingNode child) {
		if (height <= maxHeight) {
			child.parent = new PathingNode(pos, graph, height);
			child.parent.child = child;
			buildParents(pos, graph, height + 1, maxHeight, child.parent);
		}
	}
	
	public void destroy(PathingGraph graph) {
		this.destroyed = false;
		if (this.parent != null) {
			this.parent.destroy(graph);
		}
		for (PathingUpdateInterface path : this.pathes) {
			path.sceduleUpdate(graph);
		}
	}
	
	public void addPath(PathingUpdateInterface path) {
		this.pathes.add(path);
	}
	public void removePath(PathingUpdateInterface path) {
		this.pathes.remove(path);
	}
	
	public PathingEdge getConnection(final BlockPos pos) {
        for (final PathingEdge edge : this.edges) {
        	if (edge.connects(pos)) {
        		return edge;
        	}
        }
        return null;
    }
	
	public int getLvl() { 
		return this.lvl;
	}
	
	public BlockPos getBlockPos() {
		return this.pos;
	}
	
	public PathingNode getTopParent() {
		if (this.parent != null) {
			return this.parent.getTopParent();
		}
		return this;
	}
	
	public int getTopLvl() {
		return this.getTopParent().lvl;
	}
	
	public PathingNode getParentAtLvl(int l) {
		if (this.lvl == l) {
			return this;
		}
		if (this.parent != null) {
			return this.parent.getTopParent();
		}
		return null;
	}
	
	public void updateConnections(ServerWorld world, PathingGraph graph) {
		return; //TODO
	}
	public void BaseConnect(PathingNode other) {
		PathingNode[] p = {this, other};
		PathingEdge path = new PathingEdge(this, other, p, 1);
		this.edges.add(path);
		other.edges.add(path);
	}
	@Override
	public void update(ServerWorld world, PathingGraph graph) {
		if (!this.destroyed) {
			this.dequeue();
			this.updateConnections(world, graph);
		}
	}
	public void updateParent(ServerWorld world, PathingGraph graph) {
		if (this.parent != null) {
			this.parent.update(world, graph);
		}
	}
	@Override
	public void sceduleUpdate(PathingGraph graph) {
		if (this.canQueue()) { 
			graph.queueNode(this);
			this.queued = false;
		}
	}
	@Override
	public void dequeue() {
		this.queued = false;
	}
	@Override
	public boolean canQueue() {
		return !this.queued;
	}
}
