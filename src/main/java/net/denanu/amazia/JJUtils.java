package net.denanu.amazia;

import net.minecraft.nbt.NbtCompound;
import java.lang.Iterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;

public class JJUtils {
	public static final Random rand = new Random();
	
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
	
	public static void wrtieBlockPosNBT(NbtCompound tag, BlockPos pos, String name) {
		if (pos == null) {
			tag.putBoolean(name, false);
		}
		else {
			tag.putBoolean(name, true);
			tag.putInt(name + ".x", pos.getX());
			tag.putInt(name + ".y", pos.getY());
			tag.putInt(name + ".z", pos.getZ());
		}
	}
	public static BlockPos readBlockPosNBT(NbtCompound tag, String name) {
		if (tag.getBoolean(name)) {
			return new BlockPos(
					tag.getInt(name + ".x"),
					tag.getInt(name + ".y"),
					tag.getInt(name + ".z")
				);
		}
		else {
			return null;
		}
	}
	
	public static boolean equal(BlockPos a, BlockPos b) {
		return (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ());
	}
	
	public static <E extends Object> E getRandomSetElement(Set<E> set) {
		if (set.size() == 0) { return null; }
		int item = rand.nextInt(set.size());
		int i = 0;
		for(E obj : set)
		{
		    if (i == item)
		        return obj;
		    i++;
		}
		return null;
	}
	public static <E extends Object> E getRandomListElement(List<E> list) {
		if (list.size() == 0) {return null;}
		return list.get(rand.nextInt(list.size()));
	}
	public static <E extends Object> E getRandomArrayElement(E[] arr) {
		if (arr.length == 0) { return null; }
		return arr[rand.nextInt(arr.length)];
	}
}
