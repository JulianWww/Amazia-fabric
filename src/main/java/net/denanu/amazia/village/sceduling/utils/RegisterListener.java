package net.denanu.amazia.village.sceduling.utils;

import net.denanu.amazia.village.Village;
import net.minecraft.util.math.BlockPos;

public class RegisterListener implements PathingListenerRegistryOperation {
	@Override
	public void put(BlockAreaPathingData listener, Village village, BlockPos pos) {
		listener.register(village, pos);
	}

}
