package net.denanu.amazia.pathing;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasePathingNode extends PathingNode {
    private final byte clearanceHeight;
    private long updateTick;
    
    public BasePathingNode(final BlockPos bp, final byte ch) {
        super(new PathingCell(bp, (byte)0));
        this.updateTick = 0L;
        this.clearanceHeight = ch;
        this.updateTick = System.currentTimeMillis();
    }
    
    public byte getClearanceHeight() {
        return this.clearanceHeight;
    }
    
    public long getUpdateTick() {
        return this.updateTick;
    }
    
    @Override
    public int updateConnections(final World world, final PathingCellMap cellMap, final PathingGraph graph) {
        this.updateTick = System.currentTimeMillis();
        this.checkConnection(world, cellMap, graph, 1, 0);
        this.checkConnection(world, cellMap, graph, -1, 0);
        this.checkConnection(world, cellMap, graph, 0, 1);
        this.checkConnection(world, cellMap, graph, 0, -1);
        if (this.parent == null) {
            (this.parent = new PathingNode(this.getCell().up())).addChild(this);
            graph.addLastNode(this.parent);
        }
        return 0;
    }
    
    private boolean checkConnection(final World world, final PathingCellMap cellMap, final PathingGraph graph, final int x, final int z) {
        if (!graph.isInRange(this.getBlockPos().add(x, 0, z))) {
            return false;
        }
        final PathingNode connected = this.getConnection(x, z);
        if (connected == null) {
            boolean newNode = false;
            BasePathingNode node = this.getExistingNeighbor(cellMap, x, z);
            if (node == null) {
                node = this.checkWalkableNeighbor(world, x, z);
                if (node != null) {
                    newNode = true;
                }
            }
            if (node != null && this.canWalkTo(node)) {
                PathingNode.connectNodes(this, node, graph);
                if (newNode) {
                    graph.addFirstNode(node);
                    cellMap.putNode(node, world);
                    return true;
                }
            }
        }
        else {
            this.checkParentLink(connected);
        }
        return false;
    }
    
    /*@Override
    protected void notifyListeners(final World world, final List<EntityPlayerMP> listeners) {
        final SimpleNetworkWrapper network;
        final PacketPathingNode packetPathingNode;
        listeners.forEach(p -> {
            network = TekVillager.NETWORK;
            new PacketPathingNode(new PathingNodeClient(this));
            network.sendTo((IMessage)packetPathingNode, p);
        });
    }*/
    
    private BasePathingNode checkWalkableNeighbor(final World world, final int x, final int z) {
        BlockPos bp = this.getBlockPos().add(x, 0, z);
        if (!canWalkOn(world, bp)) {
            bp = bp.down();
            if (!canWalkOn(world, bp)) {
                bp = bp.down();
                if (!canWalkOn(world, bp)) {
                    bp = null;
                }
            }
        }
        if (bp != null) {
            bp = bp.up();
            byte clearance = 0;
            if (isPassable(world, bp) && isPassable(world, bp.up(1))) {
                clearance = 2;
                if (isPassable(world, bp.up(2))) {
                    ++clearance;
                }
            }
            if (clearance >= 2) {
                return new BasePathingNode(bp, clearance);
            }
        }
        return null;
    }
    
    public static boolean isPassable(final World world, final BlockPos bp) {
        final BlockState blockState = world.getBlockState(bp);
        return !blockState.getMaterial().blocksMovement() && (blockState.getBlock().canPathfindThrough(blockState, world, bp, NavigationType.LAND) || isPortal(world, bp));
    }
    
    private static boolean isPortal(final World world, final BlockPos bp) {
        return false;//VillageStructure.isWoodDoor(world, bp) || VillageStructure.isGate(world, bp);
    }
    
    public static boolean canWalkOn(final World world, final BlockPos bp) {
        if (!isPassable(world, bp)) {
            final BlockState blockState = world.getBlockState(bp);
            return blockState.getMaterial().blocksMovement() && !(blockState.getBlock() instanceof FenceBlock) && !(blockState.getBlock() instanceof WallBlock);
        }
        return false;
    }
    
    private boolean canWalkTo(final BasePathingNode node) {
        return (node.getCell().y == this.getCell().y - 1 && node.getClearanceHeight() >= 3) || node.getCell().y == this.getCell().y || (node.getCell().y == this.getCell().y + 1 && this.getClearanceHeight() >= 3);
    }
    
    private BasePathingNode getExistingNeighbor(final PathingCellMap cellMap, final int x, final int z) {
        return cellMap.getNodeYRange(this.getCell().x + x, this.getCell().y - 1, this.getCell().y + 1, this.getCell().z + z);
    }
}
