package net.denanu.amazia.pathing;

import net.minecraft.util.math.BlockPos;

public class PathingUtils {
	public static int getHeigestLevel(BlockPos pos) {
		if (isLvl5(pos)) return 5;
		if (isLvl4(pos)) return 4;
		if (isLvl3(pos)) return 3;
		if (isLvl2(pos)) return 2;
		if (isLvl1(pos)) return 1;
		return 0;
	}
	
	private static boolean isLvl1(BlockPos pos) {
		return isLvl(pos, 4);
	}
	private static boolean isLvl2(BlockPos pos) {
		return isLvl(pos, 8);
	}
	private static boolean isLvl3(BlockPos pos) {
		return isLvl(pos, 16);
	}
	private static boolean isLvl4(BlockPos pos) {
		return isLvl(pos, 32);
	}
	private static boolean isLvl5(BlockPos pos) {
		return isLvl(pos, 64);
	}
	
	private static boolean isLvl(BlockPos pos, int size) {
		return isLvlInDim(size, pos.getX()) || isLvlInDim(size, pos.getY()) || isLvlInDim(size, pos.getZ());
	}
	private static boolean isLvlInDim(int size, int x) {
		return x % size == 0 || x % size == 1;
	}
}
