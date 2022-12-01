package net.denanu.amazia.utils.random;

import java.util.NavigableMap;
import java.util.TreeMap;

import net.denanu.amazia.JJUtils;

public class WeightedRandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private double total = 0;

    public WeightedRandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = JJUtils.rand.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
