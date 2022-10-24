package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.LinkedList;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.interfaces.PathingUpdateInterface;

public class NodeQueue {
	private HashMap<Integer, LinkedList<PathingUpdateInterface>> queue;
	private int idx;
	
	public NodeQueue() {
		this.queue = new HashMap<Integer, LinkedList<PathingUpdateInterface>>();
		this.queue.put(0, new LinkedList<PathingUpdateInterface>());
		this.queue.put(1, new LinkedList<PathingUpdateInterface>());
		this.queue.put(2, new LinkedList<PathingUpdateInterface>());
		this.queue.put(3, new LinkedList<PathingUpdateInterface>());
		this.queue.put(4, new LinkedList<PathingUpdateInterface>());
		this.queue.put(5, new LinkedList<PathingUpdateInterface>());
		
		this.idx = 6;
	}
	
	private void setIdx(int i) {
		if (this.idx > i) {
			this.idx = i;
		}
	}
	
	private void releaseIdx(PathingGraph graph) {
		for (; this.idx<6 && this.queue.get(this.idx).isEmpty(); this.idx++) {}
		if (idx == 6) {
			if (graph != null) {
				graph.finalizeSetup();
			}
		}
	}
	
	public void put(PathingUpdateInterface node) {
		this.queue.get(node.getLvl()).add(node);
		this.setIdx(node.getLvl());
	}
	
	public PathingUpdateInterface poll(PathingGraph graph) {
		LinkedList<PathingUpdateInterface> data = this.queue.get(this.idx);
		if (data.isEmpty()) {
			this.releaseIdx(graph);
		}
		return data.poll();
	}

	public boolean isEmpty() {
		return this.idx == 6;
	}
}
