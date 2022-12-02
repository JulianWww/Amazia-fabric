package net.denanu.amazia.pathing.node;

import java.util.HashSet;

import net.denanu.amazia.pathing.PathingCell;
import net.denanu.amazia.pathing.PathingCluster;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.pathing.PathingUtils;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.denanu.amazia.pathing.interfaces.PathingPathInterface;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;

public class PathingNode implements PathingPathInterface {
	private PathingCell pos;
	private int lvl;
	protected PathingNode parent, child;
	protected boolean queued;
	protected boolean destroyed;
	public HashSet<PathingEdge> edges, baseEdges;
	public int distance = 0;
	public int lastEvaluation = 0;

	public PathingCluster cluster;

	public PathingEdge from = null;

	public PathingNode(final PathingCell pos, final PathingGraph graph, final PathingCluster cluster) {
		this.lvl = 0;
		this.cluster = cluster;
		this.init(pos, graph);
		PathingNode.buildParents(pos, graph, 1, PathingUtils.getHeigestLevel(pos), this);
		return;
	}
	public PathingNode(final PathingCell pos, final PathingGraph graph, final int lvl, final PathingCluster cluster, final PathingCluster childCluster) {
		this.lvl = lvl;
		this.init(pos, graph);
		this.cluster = cluster;
		childCluster.add(this);
		//this.sceduleUpdate(graph);
	}
	private void init(final PathingCell pos, final PathingGraph graph) {
		this.pos = pos;
		this.parent = null;
		this.child = null;
		this.edges = new HashSet<PathingEdge>();
		this.baseEdges = new HashSet<PathingEdge>();
		this.destroyed = false;
	}

	private static void buildParents(final PathingCell pos, final PathingGraph graph, final int height, final int maxHeight, final PathingNode child) {
		if (height <= maxHeight) {
			//graph.getWorld().spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0);
			child.parent = new PathingNode(pos, graph, height, PathingCluster.get(graph, pos, height), child.cluster);
			child.parent.child = child;
			PathingNode.buildParents(pos, graph, height + 1, maxHeight, child.parent);
		}
	}

	public void breakConnections(final PathingGraph graph) {
		for (final PathingEdge edge : this.edges) {
			edge.to(this).edges.remove(edge);
		}

		for (final PathingEdge edge: this.baseEdges) {
			edge.to(this).baseEdges.remove(edge);
		}
		this.edges.clear();
		this.baseEdges.clear();
	}

	public void breakAbstractConnections() {
		for (final PathingEdge edge : this.edges) {
			if (!this.baseEdges.contains(edge)) {
				edge.to(this).edges.remove(edge);
			}
		}
		this.edges.clear();
	}

	public void destroy(final PathingGraph graph) {
		this.destroyed = true;
		this.breakConnections(graph);
		if (this.parent != null) {
			this.parent.destroy(graph);
		}
		this.cluster.update(graph);
		if (this.child != null) {
			this.child.cluster.remove(this);
		}
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

	public PathingNode gotoLvl(final int lvl) {
		if (lvl < this.lvl) {
			return this.child.gotoLvl(lvl);
		}
		if (lvl > this.lvl) {
			return this.parent.gotoLvl(lvl);
		}
		return this;
	}

	public PathingNode getParentAtLvl(final int l) {
		if (this.lvl == l) {
			return this;
		}
		if (this.parent != null) {
			return this.parent.getTopParent();
		}
		return null;
	}

	public int updateConnections(final ServerWorld world, final PathingGraph graph) {
		return this.BuildAbstractConnections(graph) + 1;
	}

	protected void addBaseConnection(final PathingNode node, final PathingGraph graph) {
		if (this.inSameCluster(node) && !this.inSameSubCluster(node) || this.lvl == 5) {
			this.BaseConnect(node, graph);
			return;
		}
		if (this.parent != null && node.parent != null) {
			//this.sceduleUpdateParent(graph);
			//node.sceduleUpdateParent(graph);
			this.parent.addBaseConnection(node.parent, graph);
		}
	}
	protected void BaseConnect(final PathingNode other, final PathingGraph graph) {
		final PathingPathInterface[] p = {this, other};
		final PathingEdge path = new PathingEdge(this, other, p, 1);
		final boolean a = this.edges.add(path);
		final boolean b = other.edges.add(path);
		if (a || b) {
			this.cluster.update(graph);
		}

		this.baseEdges.add(path);
		other.baseEdges.add(path);
	}
	public int BuildAbstractConnections(final PathingGraph graph) {
		this.breakAbstractConnections();

		final Pair<Integer, HashSet<PathingEdge>> data = PathingUtils.getAbstractEdges(this.child, graph);

		for (final PathingEdge edge : data.getRight()) {
			this.edges.add(edge);
			edge.to(this).edges.add(edge);
		}

		for (final PathingEdge edge : this.baseEdges) {
			this.edges.add(edge);
			edge.to(this).edges.add(edge);
		}

		this.cluster.update(graph);
		return data.getLeft();
	}

	public int update(final ServerWorld world, final PathingGraph graph) {
		//this.debugUpdate(world);
		if (!this.destroyed) {
			this.dequeue();
			final int out = this.updateConnections(world, graph);
			return out;
		}
		return 1;
	}

	public void sceduleUpdateParent(final PathingGraph graph) {
		if (this.parent != null) {
			this.parent.sceduleUpdate(graph);
		}
	}

	public void sceduleUpdate(final PathingGraph graph) {
		if (this.canQueue()) {
			graph.queueNode(this);
			this.queued = true;
		}
	}

	public void dequeue() {
		this.queued = false;
	}

	public boolean canQueue() {
		return !this.queued && !this.destroyed;
	}

	@Override
	public String toString() {
		return "[" + this.lvl + "," + this.pos + "]\n";
	}

	@Debug
	public void debugUpdate(final ServerWorld world) {
		Block block = Blocks.GREEN_CONCRETE;
		final int lvl = this.getTopLvl();
		if (lvl == 1) { block = Blocks.BLUE_CONCRETE;}
		if (lvl == 2) { block = Blocks.YELLOW_CONCRETE;}
		if (lvl == 3) { block = Blocks.PURPLE_CONCRETE;}
		if (lvl == 4) { block = Blocks.ORANGE_CONCRETE;}
		if (lvl == 5) { block = Blocks.RED_CONCRETE;}
		world.setBlockState(this.getDebugPos(), block.getDefaultState());
	}

	@Debug
	private BlockPos getDebugPos() {
		return this.getBlockPos().withY(-64);
	}
	@Override
	public int hashCode() {
		int result = this.getBlockPos().getX() % 1024;
		result = result << 8 + this.getBlockPos().getY() % 1024;
		result = result << 8 + this.getBlockPos().getZ() % 1024;
		return result;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof final PathingNode other) {
			return this.getBlockPos() == other.getBlockPos() &&
					this.lvl == other.lvl;
		}
		return false;
	}
	public boolean lvllessEquals(final PathingNode other) {
		return this.getBlockPos() == other.getBlockPos();
	}

	public PathingNode getChild() {
		return this.child;
	}

	public int manhattenDistance(final PathingNode other) {
		final int d = Math.abs(this.getBlockPos().getX() - other.getBlockPos().getX());
		final int e = Math.abs(this.getBlockPos().getY() - other.getBlockPos().getY());
		final int f = Math.abs(this.getBlockPos().getZ() - other.getBlockPos().getZ());
		return d + e + f;
	}

	public int getSquaredDistance(final PathingNode other) {
		final int d = Math.abs(this.getBlockPos().getX() - other.getBlockPos().getX());
		final int e = Math.abs(this.getBlockPos().getY() - other.getBlockPos().getY());
		final int f = Math.abs(this.getBlockPos().getZ() - other.getBlockPos().getZ());
		return d * d + e * e + f * f;
	}

	public boolean inSameSuperCluster(final PathingNode node) {
		final int lvl = node.getTopLvl();
		return PathingUtils.toCluster(this.getBlockPos(), lvl).equals(PathingUtils.toCluster(node.getBlockPos(), lvl));
	}
	public boolean inSameCluster(final PathingNode node) {
		return PathingUtils.toCluster(this.getBlockPos(), node.lvl+1).equals(PathingUtils.toCluster(node.getBlockPos(), node.lvl+1));
	}
	public boolean inSameSubCluster(final PathingNode node) {
		return PathingUtils.toCluster(this.getBlockPos(), node.lvl).equals(PathingUtils.toCluster(node.getBlockPos(), node.lvl));
	}
	public boolean connectsto(final PathingNode next) {
		for (final PathingEdge edge : this.edges) {
			if (edge.to(this).lvllessEquals(next)) {
				return true;
			};
		}
		return false;
	}

	@Debug
	public void debugPlace(final ServerWorld world) {
		world.setBlockState(new BlockPos(this.pos.getX(), -64, this.pos.getZ()), Blocks.STONE.getDefaultState());
	}
}
