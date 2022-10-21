package net.denanu.amazia.pathing;

import net.minecraft.server.world.ServerWorld;

public class EdgePath implements PathingUpdateInterface {
	private PathingNode[] path;
	private boolean queued;
	private int length;
	
	public EdgePath(PathingNode[] path, int length) {
		this.path = path;
		this.length = length;
	}
	
	public void register() {
		for (PathingNode node : path) {
			node.addPath(this);
		}
	}
	
	public void destroy() {
		for (PathingNode node : path) {
			node.removePath(this);
		}
	}

	public PathingNode[] getPath() {
		return path;
	}

	public int getLength() {
		return length;
	}

	@Override
	public void update(ServerWorld world, PathingGraph graph) {
		this.dequeue();
		return;
	}

	@Override
	public void sceduleUpdate(PathingGraph graph) {
		if (!this.queued) {
			graph.queuePath(this);
			this.queued = true;
		}
	}

	@Override
	public void dequeue() {
		this.queued = false;
	}

	@Override
	public boolean canQueue() {
		return this.queued;
	}
	
}
