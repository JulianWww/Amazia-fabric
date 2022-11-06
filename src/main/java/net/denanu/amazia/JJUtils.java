package net.denanu.amazia;

import net.minecraft.nbt.NbtCompound;
import java.lang.Iterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public class JJUtils {
	public static final Random rand = new Random();
	
	public static boolean equal(BlockPos a, BlockPos b) {
		return (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ());
	}
	
	@Nullable
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
	@Nullable
	public static <E extends Object> E getRandomListElement(List<E> list) {
		if (list.size() == 0) {return null;}
		return list.get(rand.nextInt(list.size()));
	}
	@Nullable
	public static <E extends Object> E getRandomArrayElement(E[] arr) {
		if (arr.length == 0) { return null; }
		return arr[rand.nextInt(arr.length)];
	}
}
