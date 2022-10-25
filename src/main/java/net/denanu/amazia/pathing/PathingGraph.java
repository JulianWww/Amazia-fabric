package net.denanu.amazia.pathing;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import net.denanu.amazia.village.Village;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingGraph {
	private ServerWorld world;
	private Village village;
	
	private Deque<PathingNode> queue;
	private PathingEventEmiter emiter;
	CellMap map;
	private int throttle = 1000;

	public PathingGraph(ServerWorld world, Village village) {
		this.world = world;
		this.village = village;
		
		this.queue = new LinkedList<PathingNode>();
		this.emiter = new PathingEventEmiter();
		this.map = new CellMap();
	}

	public PathingEventEmiter getEventEmiter() {
		return this.emiter;
	}

	public void update() {
		this.world.getProfiler().push("Pathing Update");
		this.processNodeQueue();
		this.world.getProfiler().pop();
	}
	
	private void processNodeQueue() {
        int nodesProcessed = 0;
        while (this.canUpdate() && nodesProcessed < this.throttle ) {
            final PathingNode node = this.queue.poll();
            if (node != null) {
            	node.update(this.world, this);
            	nodesProcessed++;
            }
            else {
            	break;
            }
        }
    }

	private boolean canUpdate() {
		return !this.queue.isEmpty();
	}

	public void onBlockUpdate(ServerWorld world2, BlockPos pos) {
		this.removeNodes(pos);
		
		this.updateNodes(pos.east());
		this.updateNodes(pos.west());
		this.updateNodes(pos.north());
		this.updateNodes(pos.south());
	}
	
	private void updateNodes(BlockPos pos) {
		HashMap<Integer, PathingNode> map = this.map.getRemove(pos.getX(), pos.getZ());
		
		if (map == null) { return; }
		
		this.updateNode(map, pos.getY() + 1);
		this.updateNode(map, pos.getY());
		this.updateNode(map, pos.getY() - 1);
		this.updateNode(map, pos.getY() - 2);
	}
	private void updateNode(final HashMap<Integer, PathingNode> map, final int y) {
		PathingNode node = map.get(y);
		if (node != null) {
			this.queueNode(node);
		}
	}
	
	private void removeNodes(BlockPos pos) {
		HashMap<Integer, PathingNode> map = this.map.getRemove(pos.getX(), pos.getZ());
		
		if (map == null) return;
		
		this.removeNode(map, pos.getY() + 1);
		this.removeNode(map, pos.getY());
		this.removeNode(map, pos.getY() - 1);
		this.removeNode(map, pos.getY() - 2);
		return;
	}
	private void removeNode(final HashMap<Integer, PathingNode> map, final int y) {
		PathingNode node = map.get(y);
		if (node != null) {
			node.destroy(this);
			map.remove(y);
		}
	}

	public ServerWorld getWorld() {
		return this.world;
	}

	public boolean hasNode(BlockPos pos) {
		return this.getNode(pos) != null;
	}

	public void seedVillage(BlockPos bp) {
		byte clearanceHeight = 0;
        if (PathingNode.isPassable(this.getWorld(), bp) && PathingNode.isPassable(this.getWorld(), bp.up())) {
            clearanceHeight = 2;
            if (PathingNode.isPassable(this.getWorld(), bp.up(2))) {
                ++clearanceHeight;
            }
        }
        if (clearanceHeight >= 2) {
            final PathingNode baseNode = new PathingNode(bp, clearanceHeight);
            this.map.set(bp, baseNode);
            this.queueNode(baseNode);
        }
	}
	
	public void queueNode(PathingNode node) {
		if (node.canQueue()) {
			node.queue();
			this.queue.addLast(node);
		}
	}

	public boolean isInRange(BlockPos pos) {
		return this.village.isInVillage(pos);
	}

	public PathingNode getNode(BlockPos pos2) {
		return this.map.get(pos2);
	}

	public PathingNode getNode(int x, int y, int z) {
		return this.map.get(x, y, z);
	}

}
