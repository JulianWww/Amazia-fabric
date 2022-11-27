package net.denanu.amazia.village.sceduling.utils;

import net.denanu.amazia.village.Village;
import net.minecraft.util.math.BlockPos;

public interface PathingListenerRegistryOperation {
	public void put(BlockAreaPathingData<?> listener, Village village, BlockPos pos);
}
