package net.denanu.amazia.pathing;

import java.util.HashMap;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.village.Village;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingGraph {
	private final ServerWorld world;
	private final Village village;

	private int evaluationIdx;

	private final PathingEventEmiter eventEmiter;

	public PathingLvl lvl0;
	public HashMap<Integer, HashMap<Integer, HashMap<Integer, PathingCluster>>> clusters;
	private final static int throttle = 100;

	private boolean setupDone;

	private final NodeQueue nodeQueue;

	public PathingGraph(final ServerWorld _world, final Village _village) {
		this.world = _world;
		this.village = _village;
		this.eventEmiter = new PathingEventEmiter();

		this.lvl0 = new PathingLvl();
		this.clusters = new HashMap<>();

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
		while (this.canUpdate() && nodesProcessed < PathingGraph.throttle) {
			final PathingNode node = this.toUpdate();
			if (node != null) {
				nodesProcessed = nodesProcessed + node.update(this.world, this);
				continue;
			}
			break;
		}
	}

	public void onBlockUpdate(final ServerWorld _world, final BlockPos pos) {
		this.removeNodes(pos);

		this.updateNodes(pos.east());
		this.updateNodes(pos.west());
		this.updateNodes(pos.north());
		this.updateNodes(pos.south());
	}

	private void updateNodes(final BlockPos pos) {
		final HashMap<Integer, BasePathingNode> map = this.lvl0.getRemove(pos.getX(), pos.getZ());

		if (map == null) { return; }

		this.updateNode(map, pos.getY() + 1);
		this.updateNode(map, pos.getY());
		this.updateNode(map, pos.getY() - 1);
		this.updateNode(map, pos.getY() - 2);
	}
	private void updateNode(final HashMap<Integer, BasePathingNode> map, final int y) {
		final BasePathingNode node = map.get(y);
		if (node != null) {
			node.sceduleUpdate(this);
		}
	}

	private void removeNodes(final BlockPos pos) {
		final HashMap<Integer, BasePathingNode> map = this.lvl0.getRemove(pos.getX(), pos.getZ());

		if (map == null) {
			return;
		}

		this.removeNode(map, pos.getY() + 1);
		this.removeNode(map, pos.getY());
		this.removeNode(map, pos.getY() - 1);
		this.removeNodeRising(map, pos.getY() - 2);
	}
	private void removeNode(final HashMap<Integer, BasePathingNode> map, final int y) {
		final BasePathingNode node = map.get(y);
		if (node != null) {
			node.destroy(this);
			map.remove(y);
		}
	}
	private void removeNodeRising(final HashMap<Integer, BasePathingNode> map, final int y) {
		final BasePathingNode node = map.get(y);
		if (node != null && node.hasRisingEdge()) {
			node.destroy(this);
			map.remove(y);
		}
	}

	public ServerWorld getWorld() {
		return this.world;
	}

	public PathingEventEmiter getEventEmiter() {
		return this.eventEmiter;
	}

	public boolean isSetupDone() {
		return this.setupDone;
	}


	public void finalizeSetup() {
		Amazia.LOGGER.info("Pathing Setup Done");
		this.setupDone = true;
	}


	public boolean hasNode(final BlockPos pos) {
		return this.getNode(pos) != null;
	}

	public void seedVillage(final BlockPos bp) {
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
	}

	public boolean isInRange(final BlockPos bp) {
		return this.village.isInVillage(bp);
	}


	public void queueNode(final PathingNode node) {
		this.nodeQueue.put(node);
	}

	public BasePathingNode getNode(final BlockPos pos) {
		return this.lvl0.get(pos);
	}

	public BasePathingNode getNode(final int x, final int y, final int z) {
		return this.getNode(new BlockPos(x, y, z));
	}

	public BasePathingNode getNodeYRange(final int x, final int i, final int y, final int z) {
		BasePathingNode node = null;

		for (BlockPos pos = new BlockPos(x, i, z); pos.getY()<=y; pos = pos.up()) {
			node = this.getNode(pos);
			if (node != null) {
				return node;
			}
		}
		return node;
	}

	public BlockPos getRandomNode() {
		return this.lvl0.getRandom();
	}

	public BlockPos getRandomVillageEnterNode() {
		return this.lvl0.getRandomVillageEnterNode();
	}

	public BlockPos getAccessPoint(final BlockPos pos) {
		for (short idx=0; idx < 4; idx++) {
			final BasePathingNode node = this.getNode(switch(idx) {
			case 0 -> pos.north();
			case 1 -> pos.south();
			case 2 -> pos.east();
			default -> pos.west();
			});

			if (node != null) {
				return node.getBlockPos();
			}
		}
		return null;
	}
}
