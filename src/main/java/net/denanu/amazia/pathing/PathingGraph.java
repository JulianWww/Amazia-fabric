package net.denanu.amazia.pathing;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.edge.EdgePath;
import net.denanu.amazia.pathing.interfaces.PathingUpdateInterface;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.village.Village;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingGraph {
	private ServerWorld world;
	private Village village;
	
	private PathingEventEmiter eventEmiter;
	
	public PathingLvl<BasePathingNode> lvl0;
	public PathingCluster clusters;
	private final int throttle = 100;
	
	private boolean setupDone;
	
	private NodeQueue pathQueue, nodeQueue;
	
	public PathingGraph(ServerWorld _world, Village _village) {
		this.world = _world;
		this.village = _village;
		this.eventEmiter = new PathingEventEmiter();
		
		this.lvl0 = new PathingLvl<BasePathingNode>();
		this.clusters = new PathingCluster();
		
		this.pathQueue = new NodeQueue();
		this.nodeQueue = new NodeQueue();
		
		this.setupDone = false;
	}
	

	public void update() {
		this.world.getProfiler().push("Pathing Update");
		this.processNodeQueue();
		this.world.getProfiler().pop();
	}
	
	private PathingUpdateInterface toUpdate() {
		if (this.nodeQueue.isEmpty()) {
			return this.pathQueue.poll(null);
		}
		return this.nodeQueue.poll(this);
	}
	private boolean canUpdate() {
		return !this.pathQueue.isEmpty() || !this.nodeQueue.isEmpty();
	}
	
	private void processNodeQueue() {
        int nodesProcessed = 0;
        while (this.canUpdate() && nodesProcessed < this.throttle) {
            final PathingUpdateInterface node = this.toUpdate();
            if (node != null) {
            	nodesProcessed = nodesProcessed + node.update(this.world, this);
            }
            else {
            	break;
            }
        }
    }

	public void onBlockUpdate(ServerWorld _world, BlockPos pos) {
		
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
		return false;
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
            final BasePathingNode baseNode = new BasePathingNode(bp, this, clearanceHeight);
            baseNode.sceduleUpdate(this);
        }
        return;
	}
	
	public boolean isInRange(final BlockPos bp) {
        return this.village.isInVillage(bp);
    }

	public void queuePath(EdgePath edgePath) {
		pathQueue.put(edgePath);
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
		
		for (BlockPos pos = new BlockPos(x, i, y); pos.getY()<=y; pos = pos.up()) {
			node = this.getNode(pos);
			if (node != null) {
				return node;
			}
		}
		return node;
	}
}
