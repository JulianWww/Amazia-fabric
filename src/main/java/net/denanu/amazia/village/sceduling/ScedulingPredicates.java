package net.denanu.amazia.village.sceduling;

import net.denanu.amazia.block.custom.TeachersDeskBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class ScedulingPredicates {
	public static boolean isEnchantingTable(final BlockState state) {
		return state.isOf(Blocks.ENCHANTING_TABLE);
	}

	public static boolean isDesk(final BlockState state) {
		return state.getBlock() instanceof TeachersDeskBlock;
	}

	public static boolean isBookShelf(final BlockState state) {
		return state.isOf(Blocks.BOOKSHELF);
	}

	public static boolean isAnvil(final BlockState state) {
		return state.getBlock() instanceof AnvilBlock;
	}
}
