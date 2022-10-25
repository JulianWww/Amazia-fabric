package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.LinkedList;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.node.PathingNode;

public class NodeQueue {
	private HashMap<Integer, LinkedList<PathingNode>> queue;
	private int idx;
	
	public NodeQueue() {
		this.queue = new HashMap<Integer, LinkedList<PathingNode>>();
		this.queue.put(0, new LinkedList<PathingNode>());
		this.queue.put(1, new LinkedList<PathingNode>());
		this.queue.put(2, new LinkedList<PathingNode>());
		this.queue.put(3, new LinkedList<PathingNode>());
		this.queue.put(4, new LinkedList<PathingNode>());
		this.queue.put(5, new LinkedList<PathingNode>());
		
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
	
	public void put(PathingNode node) {
		this.queue.get(node.getLvl()).add(node);
		this.setIdx(node.getLvl());
	}
	
	public PathingNode poll(PathingGraph graph) {
		LinkedList<PathingNode> data = this.queue.get(this.idx);
		if (data.isEmpty()) {
			this.releaseIdx(graph);
		}
		return data.poll();
	}

	public boolean isEmpty() {
		return this.idx == 6;
	}
}
