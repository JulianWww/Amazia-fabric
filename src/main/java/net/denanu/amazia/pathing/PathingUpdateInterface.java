package net.denanu.amazia.pathing;

import net.minecraft.server.world.ServerWorld;

public interface PathingUpdateInterface {
	public void update(ServerWorld world, PathingGraph graph);
	public void sceduleUpdate(PathingGraph graph);
	public void dequeue();
	public boolean canQueue();
}
