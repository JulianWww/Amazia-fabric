package net.denanu.amazia.utils.random;

import java.util.NavigableMap;
import java.util.TreeMap;

import net.denanu.amazia.JJUtils;

public class WeightedRandomCollection<E> {
	private final NavigableMap<Double, E> map = new TreeMap<>();
	private double total = 0;

	public WeightedRandomCollection<E> add(final double weight, final E result) {
		if (weight <= 0) {
			return this;
		}
		this.total += weight;
		this.map.put(this.total, result);
		return this;
	}

	public E next() {
		final double value = JJUtils.rand.nextDouble() * this.total;
		return this.map.higherEntry(value).getValue();
	}
}
