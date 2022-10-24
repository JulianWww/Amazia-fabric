package net.denanu.amazia.utils.queue;

import java.util.Comparator;

import net.minecraft.util.Pair;

public class PriorityElement<E> extends Pair<Integer, E> {
	
	public static Comparator<PriorityElement<?>> comparator = new PriorityComparator();

	public PriorityElement(Integer left, E right) {
		super(left, right);
	}
	
	public static class PriorityComparator implements Comparator<PriorityElement<?>> {
		@Override
		public int compare(PriorityElement<?> o1, PriorityElement<?> o2) {
			return o1.getLeft() - o2.getLeft();
		}	
	}
	
}
