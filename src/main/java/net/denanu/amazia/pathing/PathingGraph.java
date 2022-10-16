package net.denanu.amazia.pathing;

import java.util.Deque;
import java.util.LinkedList;

import net.denanu.amazia.village.Village;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PathingGraph {
    private final World world;
    protected final Village village;
    private int nodesVerified;
    private final PathingCellMap baseCellMap;
    private Deque<PathingNode> nodeProcessQueue;
    private boolean initialQueueComplete;
    private final int throttle = 10;
    //private List<EntityPlayerMP> listeners;
    
    public PathingGraph(final World worldIn, final Village v) {
        this.nodesVerified = 0;
        this.nodeProcessQueue = new LinkedList<PathingNode>();
        this.initialQueueComplete = false;
        //this.listeners = new ArrayList<EntityPlayerMP>();
        this.world = worldIn;
        this.village = v;
        this.baseCellMap = new PathingCellMap(Village.getSize());
    }
    
    public int nodeCount() {
        return this.baseCellMap.nodeCount();
    }
    
    public boolean isProcessing() {
        return !this.nodeProcessQueue.isEmpty() || this.baseCellMap.nodeCount() <= 0;
    }
    
    /*public void addListener(final EntityPlayerMP player) {
        this.listeners.add(player);
        this.baseCellMap.notifyListenerInitial(this.world, player);
    }
    
    public void removeListener(final EntityPlayerMP player) {
        this.listeners.remove(player);
    }*/
    
    public void seedVillage(final BlockPos bp) {
        byte clearanceHeight = 0;
        if (BasePathingNode.isPassable(this.getWorld(), bp) && BasePathingNode.isPassable(this.getWorld(), bp.up())) {
            clearanceHeight = 2;
            if (BasePathingNode.isPassable(this.getWorld(), bp.up(2))) {
                ++clearanceHeight;
            }
        }
        if (clearanceHeight >= 2) {
            final BasePathingNode baseNode = new BasePathingNode(bp, clearanceHeight);
            this.baseCellMap.putNode(baseNode, this.getWorld());
            this.nodeProcessQueue.addLast(baseNode);
        }
    }
    
    public void update() {
        this.processNodeQueue();
    }
    
    private void processNodeQueue() {
        int nodesProcessed = 0;
        while (!this.nodeProcessQueue.isEmpty() && nodesProcessed < this.throttle) {
            final PathingNode node = this.nodeProcessQueue.pollFirst();
            if (node != null) {
                if (!node.isDestroyed()) {
                    node.process(this.getWorld(), this.baseCellMap, this);
                    //if (!this.listeners.isEmpty()) {
                    //    node.notifyListeners(this.world, this.listeners);
                    //}
                }
            }
            ++nodesProcessed;
        }
        if (this.nodeProcessQueue.isEmpty() && this.baseCellMap.nodeCount() > 1000) {
            this.initialQueueComplete = true;
        }
    }
    
    public boolean isInitialQueueComplete() {
        return this.initialQueueComplete;
    }
    
    public boolean isInRange(final BlockPos bp) {
        return this.village.isInVillage(bp);
    }
    
    public void addFirstNode(final PathingNode node) {
        if (node.isDestroyed()) {
            return;
        }
        if (!node.isQueued()) {
            node.queue();
            this.nodeProcessQueue.addFirst(node);
        }
    }
    
    public void addLastNode(final PathingNode node) {
        if (node.isDestroyed()) {
            return;
        }
        if (!node.isQueued()) {
            node.queue();
            for (final PathingNode child : node.children) {
                assert child.parent == node;
            }
            this.nodeProcessQueue.addLast(node);
        }
    }
    
    private void verifyNode(final PathingNode node) {
        for (int i = 0; i < 4 - node.getCell().level; ++i) {
            System.out.print("    ");
        }
        System.out.print("->" + node.getCell());
        ++this.nodesVerified;
        if (node.getCell().level == 1 && node.children.size() > 4) {
            System.err.println("Node with > 4 children " + node);
        }
        if (node.getCell().level > 0 && node.children.size() < 1) {
            System.err.println("Level " + node.getCell().level + " with no children");
        }
        System.out.print("      Connections: ");
        for (final PathingNode connect : node.connections) {
            System.out.print(connect.cell + "  ");
        }
        System.out.print("\n");
        for (final PathingNode child : node.children) {
            if (child.parent != node) {
                System.err.println("child/parent mismatch");
            }
            for (final PathingNode childConnect : child.connections) {
                if (childConnect.parent != node && !node.isConnected(childConnect.parent)) {
                    System.err.println("Node " + node + " not connected to neighbor child " + child + " parent " + childConnect.parent);
                }
            }
            this.verifyNode(child);
        }
    }
    
    public void onBlockUpdate(final World world, final BlockPos bp) {
        for (BasePathingNode baseNode = this.baseCellMap.getNodeYRange(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ()); baseNode != null; baseNode = this.baseCellMap.getNodeYRange(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ())) {
            this.baseCellMap.removeNode(baseNode, this);
            //baseNode.notifyListeners(world, this.listeners);
        }
        this.baseCellMap.updateNodes(bp.getX() + 1, bp.getY() - 2, bp.getY() + 1, bp.getZ(), this);
        this.baseCellMap.updateNodes(bp.getX() - 1, bp.getY() - 2, bp.getY() + 1, bp.getZ(), this);
        this.baseCellMap.updateNodes(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ() + 1, this);
        this.baseCellMap.updateNodes(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ() - 1, this);
        if (this.isInitialQueueComplete()) {
            this.processNodeQueue();
        }
    }
    
    public void onChunkUnloaded(final Chunk chunk) {
    }
    
    public void onChunkLoaded(final Chunk chunk) {
    }
    
    public boolean isInGraph(final BlockPos bp) {
        return this.getBaseNode(bp.getX(), bp.getY(), bp.getZ()) != null;
    }
    
    public BasePathingNode getBaseNode(final int x, final int y, final int z) {
        return this.baseCellMap.getNode(x, y, z);
    }
    
    public BasePathingNode getNodeYRange(final int x, final int y1, final int y2, final int z) {
        return this.baseCellMap.getNodeYRange(x, y1, y2, z);
    }
    
    public BasePathingNode getNearbyBaseNode(final Vec3d pos, final double widthX, final double height, final double widthZ) {
        BasePathingNode node = this.getBaseNode((int)pos.x, (int)pos.y, (int)pos.z);
        if (node == null) {
            final double halfX = widthX / 2.0;
            final double halfZ = widthZ / 2.0;
            final BlockPos corner1 = new BlockPos(pos.x - halfX, pos.y - 1.0, pos.z - halfZ);
            final BlockPos corner2 = new BlockPos(pos.x + halfX, pos.y + height, pos.z + halfZ);
            for (final BlockPos blockPos : BlockPos.iterate(corner1, corner2)) {
                node = this.getBaseNode(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                if (node != null) {
                    break;
                }
            }
        }
        return node;
    }
    
    public void debugEdgeNodes(final World world) {
        this.baseCellMap.debugEdgeNodes(world);
    }
    
    public BasePathingNode getEdgeNode(final BlockPos origin, final Double minDist) {
        return this.baseCellMap.getEdgeNode(origin, minDist);
    }
    
    public PathingCellMap getCellMap() {
    	return this.baseCellMap;
    }

	public World getWorld() {
		return world;
	}
}
