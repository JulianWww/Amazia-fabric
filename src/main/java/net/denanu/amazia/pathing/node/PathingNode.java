package net.denanu.amazia.pathing.node;

import java.util.HashSet;
import java.util.Set;

import net.denanu.amazia.pathing.PathingCell;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.pathing.PathingUtils;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.denanu.amazia.pathing.interfaces.PathingPathInterface;
import net.denanu.amazia.pathing.interfaces.PathingUpdateInterface;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public class PathingNode implements PathingUpdateInterface, PathingPathInterface {
	private PathingCell pos;
	private int lvl;
	protected PathingNode parent, child;
	protected boolean queued;
	protected boolean destroyed;
	protected Set<PathingUpdateInterface> pathes;
	public HashSet<PathingEdge> edges, baseEdges;
	public int distance = 0;
	
	public PathingEdge from = null;
	
	public PathingNode(PathingCell pos, PathingGraph graph) {
		this.lvl = 0;
		this.init(pos, graph);
		buildParents(pos, graph, 1, PathingUtils.getHeigestLevel(pos), this);
		return;
	}
	public PathingNode(PathingCell pos, PathingGraph graph, int lvl) {
		this.lvl = lvl;
		this.init(pos, graph);
		this.addToCluster(graph);
		//this.sceduleUpdate(graph);
	}
	private void init(PathingCell pos, PathingGraph graph) {
		this.pos = pos;
		this.parent = null;
		this.child = null;
		this.edges = new HashSet<PathingEdge>();
		this.baseEdges = new HashSet<PathingEdge>();
		this.pathes = new HashSet<PathingUpdateInterface>();
	}

	private static void buildParents(PathingCell pos, PathingGraph graph, int height, int maxHeight, PathingNode child) {
		if (height <= maxHeight) {
			//graph.getWorld().spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0);
			child.parent = new PathingNode(pos, graph, height);
			child.parent.child = child;
			buildParents(pos, graph, height + 1, maxHeight, child.parent);
		}
	}
	
	
	public void addToCluster(PathingGraph graph) {
		graph.clusters.addToCluster(this.pos, this.lvl, this);
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
	
	public PathingCell getBlockPos() {
		return this.pos;
	}
	
	public PathingNode getTopParent() {
		if (this.parent != null) {
			return this.parent.getTopParent();
		}
		return this;
	}
	
	public PathingNode getParent() {
		return this.parent;
	}
	
	public int getTopLvl() {
		return this.getTopParent().lvl;
	}
	
	public PathingNode gotoLvl(int lvl) {
		if (lvl < this.lvl) {
			return this.child.gotoLvl(lvl);
		}
		if (lvl > this.lvl) {
			return this.parent.gotoLvl(lvl);
		}
		return this;
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
	
	public int updateConnections(ServerWorld world, PathingGraph graph) {
		return this.BuildAbstractConnections(graph) + 1;
	}
	
	protected void addBaseConnection(PathingNode node, PathingGraph graph) {
		if (this.inSameCluster(node) || this.lvl == 5) {
			this.BaseConnect(node);
		}
		if (this.parent != null && node.parent != null) {
			this.sceduleUpdateParent(graph);
            node.sceduleUpdateParent(graph);
			this.parent.addBaseConnection(node.parent, graph);
		}
	}
	protected void BaseConnect(PathingNode other) {
		PathingPathInterface[] p = {this, other};
		PathingEdge path = new PathingEdge(this, other, p, 1);
		this.edges.add(path);
		other.edges.add(path);
		
		this.baseEdges.add(path);
		other.baseEdges.add(path);
	}
	public int BuildAbstractConnections(PathingGraph graph) {
		Pair<Integer, HashSet<PathingEdge>> data = PathingUtils.getAbstractEdges(this.child, 
				graph.clusters.getClusterEdges(this.pos, this.lvl)
			);
		
		for (PathingEdge edge: this.baseEdges) {
			data.getRight().add(edge);
		}
		if (!this.edges.equals(data.getRight())) {
			//for (PathingEdge edge : this.edges) {
				//edge.to(this).edges.remove(edge);
				//edge.to(this).sceduleUpdateParent(graph);
			//} 
			this.sceduleUpdateParent(graph);
			this.edges = data.getRight();
			
			for (PathingEdge edge : this.edges) {
				edge.to(this).sceduleUpdateParent(graph);
				edge.to(this).edges.add(edge);
			}
		}
		return data.getLeft();
	}
	
	@Override
	public int update(ServerWorld world, PathingGraph graph) {
		this.debugUpdate(world);
		if (!this.destroyed) {
			this.dequeue();
			int out = this.updateConnections(world, graph);
        	return out;
		}
		return 1;
	}
	
	public void sceduleUpdateParent(PathingGraph graph) {
		if (this.parent != null) {
			this.parent.sceduleUpdate(graph);
		}
	}
	@Override
	public void sceduleUpdate(PathingGraph graph) {
		if (this.canQueue()) { 
			graph.queueNode(this);
			this.queued = true;
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
	
	@Override
	public String toString() {
		return "[" + this.lvl + "," + this.pos + "]\n";
	}
	
	public void debugUpdate(ServerWorld world) {
		Block block = Blocks.GREEN_CONCRETE;
		int lvl = this.getTopLvl();
		if (lvl == 1) { block = Blocks.BLUE_CONCRETE;}
		if (lvl == 2) { block = Blocks.YELLOW_CONCRETE;}
		if (lvl == 3) { block = Blocks.PURPLE_CONCRETE;}
		if (lvl == 4) { block = Blocks.ORANGE_CONCRETE;}
		if (lvl == 5) { block = Blocks.RED_CONCRETE;}
		world.setBlockState(this.getDebugPos(), block.getDefaultState());
	}
	
	private BlockPos getDebugPos() {
		return new BlockPos(this.getBlockPos().getX(), -64, this.getBlockPos().getZ());
	}
	
	@Override
	public int hashCode() {
		int result = this.getBlockPos().getX() % 1024;
		result = result << 8 + (this.getBlockPos().getY() % 1024);
		result = result << 8 + (this.getBlockPos().getZ() % 1024);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PathingNode other) {
			return (
					this.getBlockPos() == other.getBlockPos() &&
					this.lvl == other.lvl
				);
		}
		return false;
	}
	public boolean lvllessEquals(PathingNode other) {
		return this.getBlockPos() == other.getBlockPos();
	}
	
	public PathingNode getChild() {
		return this.child;
	}
	
	public int manhattenDistance(PathingNode other) {
        int d = Math.abs(this.getBlockPos().getX() - other.getBlockPos().getX());
        int e = Math.abs(this.getBlockPos().getY() - other.getBlockPos().getY());
        int f = Math.abs(this.getBlockPos().getZ() - other.getBlockPos().getZ());
        return d + e + f;
    }
	
	public int getSquaredDistance(PathingNode other) {
		int d = Math.abs(this.getBlockPos().getX() - other.getBlockPos().getX());
		int e = Math.abs(this.getBlockPos().getY() - other.getBlockPos().getY());
		int f = Math.abs(this.getBlockPos().getZ() - other.getBlockPos().getZ());
		return d * d + e * e + f * f;
	}
	
	public boolean inSameSuperCluster(PathingNode node) {
		int lvl = node.getTopLvl();
		return PathingUtils.toCluster(this.getBlockPos(), lvl).equals(PathingUtils.toCluster(node.getBlockPos(), lvl));
	}
	public boolean inSameCluster(PathingNode node) {
		return PathingUtils.toCluster(this.getBlockPos(), node.lvl+1).equals(PathingUtils.toCluster(node.getBlockPos(), node.lvl+1));
	}
}
