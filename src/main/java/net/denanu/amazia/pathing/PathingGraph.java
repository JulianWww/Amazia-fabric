package net.denanu.amazia.pathing;

import java.util.HashMap;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.interfaces.PathingUpdateInterface;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.village.Village;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingGraph {
	private ServerWorld world;
	private Village village;
	
	private int evaluationIdx;
	
	private PathingEventEmiter eventEmiter;
	
	public PathingLvl lvl0;
	public HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingCluster>>> clusters;
	private final static int throttle = 100;
	
	private boolean setupDone;
	
	private NodeQueue nodeQueue;
	
	public PathingGraph(ServerWorld _world, Village _village) {
		this.world = _world;
		this.village = _village;
		this.eventEmiter = new PathingEventEmiter();
		
		this.lvl0 = new PathingLvl();
		this.clusters = new HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingCluster>>>();
		
		this.nodeQueue = new NodeQueue();
		
		this.evaluationIdx = 0;
		this.setupDone = false;
	}
	
	public int getEvalIndex() {
		return this.evaluationIdx;
	}
	public void nextEval() {
		this.evaluationIdx++;
	}
	

	public void update() {
		this.world.getProfiler().push("Pathing Update");
		this.processNodeQueue();
		this.world.getProfiler().pop();
	}
	
	private PathingNode toUpdate() {
		return this.nodeQueue.poll(this);
	}
	private boolean canUpdate() {
		return !this.nodeQueue.isEmpty();
	}
	
	private void processNodeQueue() {
        int nodesProcessed = 0;
        while (this.canUpdate() && nodesProcessed < throttle) {
            final PathingNode node = this.toUpdate();
            if (node != null) {
            	nodesProcessed = nodesProcessed + node.update(this.world, this);
            	continue;
            }
            else {
            	break;
            }
        }
    }

	public void onBlockUpdate(ServerWorld _world, BlockPos pos) {
		this.removeNodes(pos);

		this.updateNodes(pos.east());
		this.updateNodes(pos.west());
		this.updateNodes(pos.north());
		this.updateNodes(pos.south());
	}
	
	private void updateNodes(BlockPos pos) {
		HashMap<Integer, BasePathingNode> map = this.lvl0.getRemove(pos.getX(), pos.getZ());
		
		if (map == null) { return; }
		
		this.updateNode(map, pos.getY() + 1);
		this.updateNode(map, pos.getY());
		this.updateNode(map, pos.getY() - 1);
		this.updateNode(map, pos.getY() - 2);
		return;
	}
	private void updateNode(final HashMap<Integer, BasePathingNode> map, final int y) {
		BasePathingNode node = map.get(y);
		if (node != null) {
			node.sceduleUpdate(this);
		}
	}
	
	private void removeNodes(BlockPos pos) {
		HashMap<Integer, BasePathingNode> map = this.lvl0.getRemove(pos.getX(), pos.getZ());
		
		if (map == null) return;
		
		this.removeNode(map, pos.getY() + 1);
		this.removeNode(map, pos.getY());
		this.removeNode(map, pos.getY() - 1);
		this.removeNodeRising(map, pos.getY() - 2);
		return;
	}
	private void removeNode(final HashMap<Integer, BasePathingNode> map, final int y) {
		BasePathingNode node = map.get(y);
		if (node != null) {
			node.destroy(this);
			map.remove(y);
		}
	}
	private void removeNodeRising(final HashMap<Integer, BasePathingNode> map, final int y) {
		BasePathingNode node = map.get(y);
		if (node != null && node.hasRisingEdge()) {
			node.destroy(this);
			map.remove(y);
		}
	}
	
	public ServerWorld getWorld() {
		return world;
	}

	public PathingEventEmiter getEventEmiter() {
		return eventEmiter;
	}

	public boolean isSetupDone() {
		return setupDone;
	}


	public void finalizeSetup() {
		Amazia.LOGGER.info("Pathing Setup Done");
		this.setupDone = true;
	}


	public boolean hasNode(BlockPos pos) {
		return this.getNode(pos) != null;
	}

	public void seedVillage(BlockPos bp) {
		byte clearanceHeight = 0;
        if (BasePathingNode.isPassable(this.getWorld(), bp) && BasePathingNode.isPassable(this.getWorld(), bp.up())) {
            clearanceHeight = 2;
            if (BasePathingNode.isPassable(this.getWorld(), bp.up(2))) {
                ++clearanceHeight;
            }
        }
        if (clearanceHeight >= 2) {
            final BasePathingNode baseNode = new BasePathingNode(bp, this, clearanceHeight, PathingCluster.get(this, bp, 0));
            baseNode.sceduleUpdate(this);
        }
        return;
	}
	
	public boolean isInRange(final BlockPos bp) {
        return this.village.isInVillage(bp);
    }


	public void queueNode(PathingNode node) {
		nodeQueue.put(node);
	}

	public BasePathingNode getNode(final BlockPos pos) {
		return this.lvl0.get(pos);
	}
	
	public BasePathingNode getNode(int x, int y, int z) {
		return this.getNode(new BlockPos(x, y, z));
	}

	public BasePathingNode getNodeYRange(int x, int i, int y, int z) {
		BasePathingNode node = null;
		
		for (BlockPos pos = new BlockPos(x, i, z); pos.getY()<=y; pos = pos.up()) {
			node = this.getNode(pos);
			if (node != null) {
				return node;
			}
		}
		return node;
	}
}
