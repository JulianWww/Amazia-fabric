package net.denanu.amazia.utils;

import java.util.Iterator;

public class ReverseArrayIterator<E> implements Iterator<E> {
	private final E[] arr;
	private int idx;
	
	public ReverseArrayIterator(final E[] arr) {
		this.arr = arr;
		this.idx = arr.length -1;
	}
	

	@Override
	public boolean hasNext() {
		return this.idx >= 0;
	}

	@Override
	public E next() {
		return this.arr[this.idx--];
	}
}
