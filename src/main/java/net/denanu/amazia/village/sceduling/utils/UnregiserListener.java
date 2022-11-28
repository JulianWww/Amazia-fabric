package net.denanu.amazia.village.sceduling.utils;

import net.denanu.amazia.village.Village;
import net.minecraft.util.math.BlockPos;

public class UnregiserListener implements PathingListenerRegistryOperation {

	@Override
	public void put(final BlockAreaPathingData<?> listener, final Village village, final BlockPos pos) {
		listener.unregister(village, pos);
	}

}
