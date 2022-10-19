package net.denanu.amazia.village.sceduling.utils;

import java.util.HashSet;
import java.util.Set;

import net.denanu.amazia.pathing.PathingEventListener;
import net.denanu.amazia.village.Village;
import net.minecraft.util.math.BlockPos;

public abstract class BlockAreaPathingData implements PathingEventListener {
	private BlockPos pos;
	private Set<BlockPos> validPathingNodes;

	BlockAreaPathingData(BlockPos pos, Village _village) {
		this.pos = pos;
		this.validPathingNodes = new HashSet<BlockPos>();
		this.getAccessPoints(new RegisterListener(), pos, _village);
	}
	
	protected abstract void getAccessPoints(PathingListenerRegistryOperation operation, BlockPos origin, Village v);
	
	public void destroy(Village village) {
		this.getAccessPoints(new UnregiserListener(), pos, village);
	}
	
	protected void register(Village v, BlockPos pos) {
		v.getPathingGraph().getEventEmiter().registerListener(this, pos);
		if (v.getPathingGraph().hasNode(pos)) {
			this.validPathingNodes.add(pos);
		}
	}
	protected void unregister(Village v, BlockPos pos) {
		v.getPathingGraph().getEventEmiter().removeListener(this, pos);
	}

	@Override
	public void onCreate(BlockPos pos) {
		this.validPathingNodes.add(pos);
	}

	@Override
	public void onDestroy(BlockPos pos) {
		this.validPathingNodes.remove(pos);
	}
	
	public BlockPos getPos() {
		return this.pos;
	}

	public Set<BlockPos> getPathingOptions() {
		return this.validPathingNodes;
	}
}
