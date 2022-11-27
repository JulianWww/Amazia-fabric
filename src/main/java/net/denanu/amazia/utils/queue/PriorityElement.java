package net.denanu.amazia.utils.queue;

import java.util.Comparator;

import net.minecraft.util.Pair;

public class PriorityElement<E> extends Pair<Integer, E> {

	public static Comparator<PriorityElement<?>> comparator = new PriorityComparator();

	public PriorityElement(final Integer left, final E right) {
		super(left, right);
	}

	public static class PriorityComparator implements Comparator<PriorityElement<?>> {
		@Override
		public int compare(final PriorityElement<?> o1, final PriorityElement<?> o2) {
			return o1.getLeft() - o2.getLeft();
		}
	}
}
