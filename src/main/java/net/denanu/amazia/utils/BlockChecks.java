package net.denanu.amazia.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockChecks {
	public static boolean isHoable(Block block) {
		return (block.equals(Blocks.DIRT) || block.equals(Blocks.GRASS_BLOCK) || block.equals(Blocks.DIRT_PATH));
	}
}
