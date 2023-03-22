package net.denanu.amazia.pathing.interfaces;

import java.util.Collection;

import net.minecraft.util.math.BlockPos;

public interface PathingEventListener {

	void onCreate(BlockPos pos);

	void onDestroy(BlockPos pos);

	Collection<BlockPos> getPathingOptions();

}