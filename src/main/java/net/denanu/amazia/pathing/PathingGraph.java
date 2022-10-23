package net.denanu.amazia.pathing;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Set;

import net.denanu.amazia.village.Village;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingGraph {
	private ServerWorld world;
	private Village village;
	
	private PathingEventEmiter eventEmiter;
	
	public PathingLvl<BasePathingNode> lvl0;
	private final int throttle = 100;
	
	private Deque<PathingUpdateInterface> pathQueue, nodeQueue;
	
	public PathingGraph(ServerWorld _world, Village _village) {
		this.world = _world;
		this.village = _village;
		this.eventEmiter = new PathingEventEmiter();
		
		this.lvl0 = new PathingLvl<BasePathingNode>();
		
		this.pathQueue = new ArrayDeque<PathingUpdateInterface>();
		this.nodeQueue = new ArrayDeque<PathingUpdateInterface>();
	}
	

	public void update() {
		this.processNodeQueue();;
	}
	
	private PathingUpdateInterface toUpdate() {
		if (this.nodeQueue.isEmpty()) {
			return this.pathQueue.poll();
		}
		return this.nodeQueue.poll();
	}
	private boolean canUpdate() {
		return !this.pathQueue.isEmpty() || !this.nodeQueue.isEmpty();
	}
	
	private void processNodeQueue() {
        int nodesProcessed = 0;
        while (this.canUpdate() && nodesProcessed < this.throttle) {
            final PathingUpdateInterface node = this.toUpdate();
            if (node != null) {
                node.update(this.world, this);
            }
            ++nodesProcessed;
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


	public BasePathingNode getNode(final BlockPos pos) {
		return this.lvl0.get(pos);
	}


	public void queuePath(EdgePath edgePath) {
		pathQueue.addLast(edgePath);
	}


	public void queueNode(PathingNode node) {
		nodeQueue.addLast(node);
	}
}
