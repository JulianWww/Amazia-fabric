package net.denanu.amazia.pathing.interfaces;

import net.denanu.amazia.pathing.PathingGraph;
import net.minecraft.server.world.ServerWorld;

public interface PathingUpdateInterface {
	public int update(ServerWorld world, PathingGraph graph);
	public void sceduleUpdate(PathingGraph graph);
	public void dequeue();
	public boolean canQueue();
	public int getLvl();
}
