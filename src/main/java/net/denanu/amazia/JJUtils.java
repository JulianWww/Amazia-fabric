package net.denanu.amazia;

import net.minecraft.nbt.NbtCompound;
import java.lang.Iterable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class JJUtils {
	public static <E extends Iterable<BlockPos>> void writeNBT(NbtCompound tag, E list, String name) {
		List<Integer> x = new ArrayList<Integer>();
		List<Integer> y = new ArrayList<Integer>();
		List<Integer> z = new ArrayList<Integer>();
		for (BlockPos pos: list) {
			x.add(pos.getX());
			y.add(pos.getY());
			z.add(pos.getZ());
		}
		tag.putIntArray(name + ".x", x);
		tag.putIntArray(name + ".y", y);
		tag.putIntArray(name + ".z", z);
	}
	public static List<BlockPos> readNBT(NbtCompound tag, String name) {
		int[] x = tag.getIntArray(name + ".x");
		int[] y = tag.getIntArray(name + ".y");
		int[] z = tag.getIntArray(name + ".z");
		List<BlockPos> list = new ArrayList<BlockPos>();
		for (int idx=0; idx < x.length; idx++) {
			BlockPos pos =  new BlockPos(x[idx], y[idx], z[idx]);
			if (pos != null) {
				list.add(pos);
			}
		}
		return list;
	}
}
