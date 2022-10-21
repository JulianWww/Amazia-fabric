package net.denanu.amazia.pathing;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BasePathingNode extends PathingNode {
	private final byte clearanceHeight;

	public BasePathingNode(BlockPos pos, PathingGraph graph, final byte clearance) {
		super(pos, graph);
		this.clearanceHeight = clearance;
	}
	
	public byte getClearanceHeight() {
        return this.clearanceHeight;
    }
	
	@Override
	public void updateConnections(ServerWorld world, PathingGraph graph) {
		this.checkConnectionSide(world, graph, this.getBlockPos().east());
		this.checkConnectionSide(world, graph, this.getBlockPos().west());
		this.checkConnectionSide(world, graph, this.getBlockPos().north());
		this.checkConnectionSide(world, graph, this.getBlockPos().south());
	}
	
	private boolean checkConnectionSide(final ServerWorld world, final PathingGraph graph, final BlockPos pos) {
		return (
				this.checkConnection(world, graph, pos.down()) ||
				this.checkConnection(world, graph, pos) ||
				this.checkConnection(world, graph, pos.up())
			);
	}
	
	private boolean checkConnection(final ServerWorld world, final PathingGraph graph, final BlockPos pos) {
        if (!graph.isInRange(pos)) {
            return false;
        }
        final PathingEdge connected = this.getConnection(pos);
        if (connected == null) {
            boolean newNode = false;
            BasePathingNode node = this.getExistingNeighbor(graph, pos);
            if (node == null) {
                node = BasePathingNode.checkWalkableNeighbor(world, pos, graph);
                if (node != null) {
                    newNode = true;
                }
            }
            if (node != null && this.canWalkTo(node)) {
                this.addBaseConnection(node, graph);
                if (newNode) {
                    this.sceduleUpdate(graph);
                    return true;
                }
            }
        }
        return false;
    }
	
	private void addBaseConnection(BasePathingNode node, PathingGraph graph) {
		int connectionLvl = Math.min(this.getTopLvl(), node.getTopLvl());
		this.getParentAtLvl(connectionLvl).BaseConnect(node.getParentAtLvl(connectionLvl));
	}

	private static BasePathingNode checkWalkableNeighbor(final ServerWorld world, BlockPos pos, PathingGraph graph) {
		if (canWalkOn(world, pos.down()) && isPassable(world, pos) && isPassable(world, pos.up())) {
			byte clearance = 2;
			if (isPassable(world, pos.up(2))) {
				clearance++;
			}
			return new BasePathingNode(pos, graph, clearance);
		}
		return null;
    }

	private static boolean canWalkOn(final ServerWorld world, BlockPos pos) {
		final BlockState blockState = world.getBlockState(pos);
        return blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FenceBlock) && !(blockState.getBlock() instanceof WallBlock);
	}
	static boolean isPassable(final ServerWorld world, final BlockPos bp) {
		final BlockState blockState = world.getBlockState(bp);
        return !blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FluidBlock) || isPortal(world, bp);
	}
	private static boolean isPortal(final ServerWorld world, final BlockPos bp) {
		return false;
	}
	private boolean canWalkTo(final BasePathingNode node) {
        return (node.getBlockPos().getY() == this.getBlockPos().getY() - 1 && node.getClearanceHeight() >= 3) || node.getBlockPos().getY() == this.getBlockPos().getY() || (node.getBlockPos().getY() == this.getBlockPos().getY() + 1 && this.getClearanceHeight() >= 3);
    }

	private BasePathingNode getExistingNeighbor(PathingGraph graph, BlockPos pos) {
		return graph.getNode(pos);
	}
}
