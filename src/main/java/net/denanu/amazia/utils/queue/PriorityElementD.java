package net.denanu.amazia.utils.queue;

import java.util.Comparator;

import net.minecraft.util.Pair;

public class PriorityElementD extends Pair<Double, Double> {

	public static Comparator<PriorityElementD> comparator = new PriorityComparator();

	public PriorityElementD() {
		super(0.0, 0.0);
	}

	public PriorityElementD(final Double left, final Double right) {
		super(left, right);
	}

	public static class PriorityComparator implements Comparator<PriorityElementD> {
		@Override
		public int compare(final PriorityElementD o1, final PriorityElementD o2) {
			return Double.compare(o1.getLeft(), o2.getLeft());
		}
	}
}
