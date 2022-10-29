package net.denanu.amazia.utils;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {
	private final E[] arr;
	private int idx;
	
	public ArrayIterator(final E[] arr) {
		this.arr = arr;
		this.idx = 0;
	}
	

	@Override
	public boolean hasNext() {
		return this.idx < this.arr.length;
	}

	@Override
	public E next() {
		return this.arr[this.idx++];
	}

}
