package net.denanu.amazia.pathing;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingNode {
	public PathNode minecraftPathingNode;
	public Set<PathingNode> edges;
	private BlockPos pos;
	private byte clearance;
	private boolean queued;
	
	public int distance;
	public PathingNode movedFromNode;
	
	public PathingNode(BlockPos pos, byte clearanceHeight) {
		this.pos = pos;
		this.clearance = clearanceHeight;
		
		this.edges = new HashSet<PathingNode>();
		this.queued = false;
		
		this.minecraftPathingNode = new PathNode(pos.getX(), pos.getY(), pos.getZ());
	}
	
	
	public void update(ServerWorld world, PathingGraph graph) {
		this.dequeue();
		this.checkConnection(world, graph, pos.east());
		this.checkConnection(world, graph, pos.west());
		this.checkConnection(world, graph, pos.north());
		this.checkConnection(world, graph, pos.south());
		if (this.edges.size() < 4) {
			this.debug(graph);
		}
	}
	
	private boolean checkConnection(final ServerWorld world, final PathingGraph graph, BlockPos pos) {
        if (!graph.isInRange(pos)) {
            return false;
        }
        if (!canWalkOn(world, pos)) {
        	pos = pos.down();
        	if (!canWalkOn(world, pos)) {
        		pos = pos.down();
        		if (!canWalkOn(world, pos)) {
        			pos = pos.down();
        		}
        	}
        }
        pos = pos.up();
        final PathingNode connected = this.getConnection(pos);
        if (connected == null) {
            boolean newNode = false;
            PathingNode node = this.getExistingNeighbor(graph, pos);
            if (node == null) {
                node = this.checkWalkableNeighbor(world, pos, graph);
                if (node != null) {
                    newNode = true;
                }
            }
            
            if (node != null && this.canWalkTo(node)) {
            	this.edges.add(node);
            	node.edges.add(this);
                if (newNode) {
                    graph.queueNode(node);
                }
                return true;
            }
        }
        return false;
    }
	
	private PathingNode checkWalkableNeighbor(final ServerWorld world, BlockPos pos, PathingGraph graph) {
		if (canWalkOn(world, pos.down()) && isPassable(world, pos) && isPassable(world, pos.up())) {
			byte clearance = 2;
			if (isPassable(world, pos.up(2))) {
				clearance++;
			}
			PathingNode out = new PathingNode(pos, clearance);
			graph.map.set(pos, out);
			return out;
		}
		return null;
    }
	
	
	private PathingNode getExistingNeighbor(PathingGraph graph, BlockPos pos2) {
		return graph.getNode(pos2);
	}


	private PathingNode getConnection(BlockPos pos2) {
		for (PathingNode node : this.edges) {
			if (node.getBlockPos() == pos2) {
				return node;
			}
		}
		return null;
	}


	public static boolean canWalkOn(final ServerWorld world, BlockPos pos) {
		final BlockState blockState = world.getBlockState(pos);
        return blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FenceBlock) && !(blockState.getBlock() instanceof WallBlock);
	}
	public static boolean isPassable(final ServerWorld world, final BlockPos bp) {
		final BlockState blockState = world.getBlockState(bp);
        return !blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FluidBlock) || isPortal(world, bp);
	}
	public static boolean isPortal(final ServerWorld world, final BlockPos bp) {
		return false;
	}
	public boolean canWalkTo(final PathingNode node) {
        return (node.getBlockPos().getY() == this.getBlockPos().getY() - 1 && node.getClearanceHeight() >= 3) || node.getBlockPos().getY() == this.getBlockPos().getY() || (node.getBlockPos().getY() == this.getBlockPos().getY() + 1 && this.getClearanceHeight() >= 3);
    }
	
	private byte getClearanceHeight() {
		return this.clearance;
	}
	BlockPos getBlockPos() {
		return this.pos;
	}
	
	public boolean canQueue() {
		return !this.queued;
	}
	public void queue() {
		this.queued = true;
	}
	private void dequeue() {
		this.queued = false;
	}


	public int getSquaredDistance(PathingNode other) {
		int d = Math.abs(this.getBlockPos().getX() - other.getBlockPos().getX());
		int e = Math.abs(this.getBlockPos().getY() - other.getBlockPos().getY());
		int f = Math.abs(this.getBlockPos().getZ() - other.getBlockPos().getZ());
		return d * d + e * e + f * f;
	}


	public void destroy(PathingGraph pathingGraph) {
		for (PathingNode edge: this.edges) {
			edge.edges.remove(this);
		}
		this.edges.clear();
	}


	public void debug(PathingGraph graph) {
		graph.getWorld().setBlockState(new BlockPos(this.pos.getX(), -64, this.pos.getZ()), Blocks.DIAMOND_BLOCK.getDefaultState());
	}
}
