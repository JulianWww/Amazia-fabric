package net.denanu.amazia.pathing.node;

import java.util.HashSet;

import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.pathing.PathingCell;
import net.denanu.amazia.pathing.PathingCluster;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BasePathingNode extends PathingNode {
	private final byte clearanceHeight;
	public HashSet<BasePathingNode> ajacentNodes;
	public BasePathingNode movedFromNode;

	public BasePathingNode(BlockPos pos, PathingGraph graph, final byte clearance, PathingCluster cluster) {
		super(new PathingCell(pos), graph, cluster);
		this.clearanceHeight = clearance;
		graph.lvl0.add(pos, this);
		graph.getEventEmiter().sendCreate(pos);
		this.ajacentNodes = new HashSet<BasePathingNode>();
	}
	
	public boolean hasRisingEdge() {
		for (BasePathingNode node: this.ajacentNodes) {
			if (node.getBlockPos().getY() > this.getBlockPos().getY()) {
				return true;
			}
		}
		return false;
	}
	
	public byte getClearanceHeight() {
        return this.clearanceHeight;
    }
	
	@Override
	public int updateConnections(ServerWorld world, PathingGraph graph) {
		this._updateConnections(world, graph);
		return 1;
	}
	
	public boolean _updateConnections(ServerWorld world, PathingGraph graph) {
		boolean a = this.checkConnectionSide(world, graph, this.getBlockPos().east());
		boolean b = this.checkConnectionSide(world, graph, this.getBlockPos().west());
		boolean c = this.checkConnectionSide(world, graph, this.getBlockPos().north());
		boolean d = this.checkConnectionSide(world, graph, this.getBlockPos().south());
		return a || b || c || d;
	}
	
	private boolean checkConnectionSide(final ServerWorld world, final PathingGraph graph, final BlockPos pos) {
		return (
				this.checkConnection(world, graph, pos)
			);
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
        final PathingEdge connected = this.getConnection(pos);
        if (connected == null) {
            boolean newNode = false;
            BasePathingNode node = this.getExistingNeighbor(graph, pos);
            if (node == null) {
                node = this.checkWalkableNeighbor(world, pos, graph);
                if (node != null) {
                	this.addAjacentNode(node);
                    newNode = true;
                }
            }
            
            if (node != null && this.canWalkTo(node)) {
                this.addBaseConnection(node, graph); 
                if (newNode) {
                    node.sceduleUpdate(graph);
                }
                return true;
            }
        }
        return false;
    }
	
	private void addAjacentNode(BasePathingNode node) {
		this.ajacentNodes.add(node);
		node.ajacentNodes.add(this);
	}

	private BasePathingNode checkWalkableNeighbor(final ServerWorld world, BlockPos pos, PathingGraph graph) {
		if (canWalkOn(world, pos.down()) && isPassable(world, pos) && isPassable(world, pos.up())) {
			byte clearance = 2;
			if (isPassable(world, pos.up(2))) {
				clearance++;
			}
			BasePathingNode out = new BasePathingNode(pos, graph, clearance, PathingCluster.get(graph, pos, 0));
			return out;
		}
		return null;
    }

	public static boolean canWalkOn(final ServerWorld world, BlockPos pos) {
		final BlockState blockState = world.getBlockState(pos);
        return blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FenceBlock) && !(blockState.getBlock() instanceof WallBlock) && !blockState.isOf(AmaziaBlocks.TREE_FARM_MARKER);
	}
	public static boolean isPassable(final ServerWorld world, final BlockPos bp) {
		final BlockState blockState = world.getBlockState(bp);
        return !blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FluidBlock) || isPortal(world, bp);
	}
	public static boolean isPortal(final ServerWorld world, final BlockPos bp) {
		return false;
	}
	public boolean canWalkTo(final BasePathingNode node) {
        return (node.getBlockPos().getY() == this.getBlockPos().getY() - 1 && node.getClearanceHeight() >= 3) || node.getBlockPos().getY() == this.getBlockPos().getY() || (node.getBlockPos().getY() == this.getBlockPos().getY() + 1 && this.getClearanceHeight() >= 3);
    }

	private BasePathingNode getExistingNeighbor(PathingGraph graph, BlockPos pos) {
		return graph.getNode(pos); // maybe use ajacent nodes for this probably wont work more thinking required
	}
	
	@Override
	public void destroy(PathingGraph graph) {
		for (BasePathingNode node : this.ajacentNodes) {
			node.ajacentNodes.remove(this);
		}
		graph.getEventEmiter().sendDestroy(getBlockPos());
		super.destroy(graph);
	}
}
