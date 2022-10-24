package net.denanu.amazia.pathing.edge;

import java.util.Iterator;

import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.pathing.interfaces.PathingPathInterface;
import net.denanu.amazia.pathing.interfaces.PathingUpdateInterface;
import net.minecraft.server.world.ServerWorld;

public class EdgePath implements PathingUpdateInterface {
	private PathingPathInterface[] path;
	private boolean queued;
	private int length, lvl;
	
	public EdgePath(PathingPathInterface[] path, int length, int lvl) {
		this.path = path;
		this.length = length;
		this.lvl = lvl;
		this.register();
	}
	
	public void register() {
		for (PathingPathInterface node : path) {
			node.addPath(this);
		}
	}
	
	public void destroy() {
		for (PathingPathInterface node : path) {
			node.removePath(this);
		}
	}

	public PathingPathInterface[] getPath() {
		return path;
	}

	public int getLength() {
		return length;
	}

	@Override
	public int update(ServerWorld world, PathingGraph graph) {
		this.dequeue();
		return 1;
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

	@Override
	public int getLvl() {
		return this.lvl;
	}	
}
