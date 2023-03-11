package net.denanu.amazia.pathing.node;

import java.util.HashSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.pathing.PathingCell;
import net.denanu.amazia.pathing.PathingCluster;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class BasePathingNode extends PathingNode {
	private final byte clearanceHeight;
	public HashSet<BasePathingNode> ajacentNodes;
	public BasePathingNode movedFromNode;

	public BasePathingNode(final BlockPos pos, final PathingGraph graph, final byte clearance, final PathingCluster cluster) {
		super(new PathingCell(pos), graph, cluster);
		this.clearanceHeight = clearance;
		graph.lvl0.add(pos, this);
		graph.getEventEmiter().sendCreate(pos);
		this.ajacentNodes = new HashSet<>();
	}

	public boolean hasRisingEdge(final ServerWorld world) {
		for (final BasePathingNode node: this.ajacentNodes) {
			if (node.getBlockPos().getY() > this.getBlockPos().getY()) {
				return true;
			}
		}
		return this.differantClearance(world);
	}

	public byte getClearanceHeight() {
		return this.clearanceHeight;
	}

	@Override
	public int updateConnections(final ServerWorld world, final PathingGraph graph) {
		this._updateConnections(world, graph);
		return 1;
	}

	public boolean _updateConnections(final ServerWorld world, final PathingGraph graph) {
		final boolean a = this.checkConnectionSide(world, graph, this.getBlockPos().east());
		final boolean b = this.checkConnectionSide(world, graph, this.getBlockPos().west());
		final boolean c = this.checkConnectionSide(world, graph, this.getBlockPos().north());
		final boolean d = this.checkConnectionSide(world, graph, this.getBlockPos().south());
		return a || b || c || d;
	}

	private boolean checkConnectionSide(final ServerWorld world, final PathingGraph graph, final BlockPos pos) {
		return this.checkConnection(world, graph, pos);
	}

	private boolean checkConnection(final ServerWorld world, final PathingGraph graph, BlockPos pos) {
		if (!graph.isInRange(pos)) {
			return false;
		}
		if (!BasePathingNode.canWalkOn(world, pos)) {
			pos = pos.down();
			if (!BasePathingNode.canWalkOn(world, pos)) {
				pos = pos.down();
				if (!BasePathingNode.canWalkOn(world, pos)) {
					return false;
				}
			}
		}

		pos = pos.up();
		if (Math.abs(BasePathingNode.getTop(world, this.getBlockPos()) - BasePathingNode.getTop(world, pos)) > 1) {
			return false;
		}

		final PathingEdge connected = this.getConnection(pos);
		if (connected == null) {
			boolean newNode = false;
			BasePathingNode node = this.getExistingNeighbor(graph, pos);
			if (node == null) {
				node = this.checkWalkableNeighbor(world, pos, graph);
				if (node != null) {
					newNode = true;
				}
			}

			if (node != null && this.canWalkTo(node)) {
				this.addAjacentNode(node);
				this.addBaseConnection(node, graph);
				if (newNode) {
					node.sceduleUpdate(graph);
				}
				return true;
			}
		}
		return false;
	}

	private static double getTop(final ServerWorld world, final BlockPos pos) {
		final double voxelh = world.getBlockState(pos).getOutlineShape(world, pos, ShapeContext.absent()).getMax(Axis.Y);
		if (voxelh == Double.NEGATIVE_INFINITY) {
			return pos.getY();
		}
		return voxelh + pos.getY();
	}

	private void addAjacentNode(final BasePathingNode node) {
		if (node != null) {
			this.ajacentNodes.add(node);
			node.ajacentNodes.add(this);
		}
	}

	private static byte getClerance(final ServerWorld world, final BlockPos pos) {
		if (BasePathingNode.canWalkOn(world, pos.down()) && BasePathingNode.isPassable(world, pos) && BasePathingNode.isPassable(world, pos.up())) {
			byte clearance = 2;
			if (BasePathingNode.isPassable(world, pos.up(2))) {
				clearance++;
			}
			return clearance;
		}
		return 0;
	}

	public boolean differantClearance(final ServerWorld world) {
		final byte newClear = BasePathingNode.getClerance(world, this.getBlockPos());
		if (newClear != this.clearanceHeight) {
			return true;
		}
		return false;
	}

	private BasePathingNode checkWalkableNeighbor(final ServerWorld world, final BlockPos pos, final PathingGraph graph) {
		final byte clearance = BasePathingNode.getClerance(world, pos);
		if (clearance != 0) {
			return new BasePathingNode(pos, graph, clearance, PathingCluster.get(graph, pos, 0));
		}
		return null;
	}

	public static boolean canWalkOn(final ServerWorld world, final BlockPos pos) {
		final BlockState blockState = world.getBlockState(pos);
		return blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FenceBlock) && !(blockState.getBlock() instanceof WallBlock) && !blockState.isOf(AmaziaBlocks.TREE_FARM_MARKER);
	}
	public static boolean isPassable(final ServerWorld world, final BlockPos bp) {
		final BlockState blockState = world.getBlockState(bp);
		return !blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FluidBlock) || BasePathingNode.isPortal(blockState);
	}
	public static boolean isPortal(final BlockState state) {
		return state.getBlock() instanceof DoorBlock && !state.isOf(Blocks.IRON_DOOR) || state.getBlock() instanceof FenceGateBlock;
	}
	public boolean canWalkTo(final BasePathingNode node) {
		final boolean a = node.getBlockPos().getY() == this.getBlockPos().getY() - 1 && node.getClearanceHeight() >= 3;
		final boolean b = node.getBlockPos().getY() == this.getBlockPos().getY();
		final boolean c = node.getBlockPos().getY() == this.getBlockPos().getY() + 1 && this.getClearanceHeight() >= 3;
		if (!b) {
			Amazia.LOGGER.warn("from: " + this.toString() + ", to: " + node.toString());
			Amazia.LOGGER.warn(Boolean.toString(a) + ", " + Boolean.toString(b) + ", " + Boolean.toString(c));
		}

		return node.getBlockPos().getY() == this.getBlockPos().getY() - 1 && node.getClearanceHeight() >= 3 || node.getBlockPos().getY() == this.getBlockPos().getY() || node.getBlockPos().getY() == this.getBlockPos().getY() + 1 && this.getClearanceHeight() >= 3;
	}

	private BasePathingNode getExistingNeighbor(final PathingGraph graph, final BlockPos pos) {
		return graph.getNode(pos); // maybe use ajacent nodes for this probably wont work more thinking required
	}

	@Override
	public void destroy(final PathingGraph graph) {
		for (final BasePathingNode node : this.ajacentNodes) {
			node.ajacentNodes.remove(this);
		}
		graph.getEventEmiter().sendDestroy(this.getBlockPos());
		super.destroy(graph);
	}

	@Override
	public PathingNode getByDirection(final Direction dir) {
		for (final PathingNode node : this.ajacentNodes) {
			if (BasePathingNode.testWithDirection(this, node, dir)) {
				return node;
			}
		}
		return null;
	}

	private static boolean testWithDirection(final PathingNode orig, final PathingNode ajacent, final Direction dir) {
		return switch(dir.getDirection()) {
		case POSITIVE -> orig.getAxisPosition(dir.getAxis()) < ajacent.getAxisPosition(dir.getAxis());
		case NEGATIVE -> orig.getAxisPosition(dir.getAxis()) > ajacent.getAxisPosition(dir.getAxis());
		};
	}
}
