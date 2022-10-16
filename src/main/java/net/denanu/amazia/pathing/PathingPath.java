package net.denanu.amazia.pathing;

import java.util.List;

import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;

public class PathingPath extends Path {

	public PathingPath(List<PathNode> nodes, BlockPos target, boolean reachesTarget) {
		super(nodes, target, reachesTarget);
	}
	
	@Override
	public String toString() {
		String out = "[";
		for (int idx=0; idx < this.getLength(); idx++) {
			out = out + "[" + this.getNode(idx).x + ", " + this.getNode(idx).z + "],\n"; 
		}
		return out.substring(0, out.length() - 2)+ "]";
	}
}
