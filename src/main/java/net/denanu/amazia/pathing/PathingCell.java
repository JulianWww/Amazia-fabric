package net.denanu.amazia.pathing;

import net.denanu.amazia.pathing.edge.PathingEdge;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class PathingCell extends BlockPos {
	public PathingEdge from;
	public int lastEvaluation;
	public PathNode minecraftPathingNode;
	
	public PathingCell(int i, int j, int k) {
		super(i, j, k);
		this.setup();
		this.lastEvaluation = -1;
	}
	public PathingCell(double d, double e, double f) {
		super(d, e, f);
		this.setup();
	}
	public PathingCell(Vec3d pos) {
		this(pos.x, pos.y, pos.z);
		this.setup();
	}
	public PathingCell(Position pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
		this.setup();
	}
	public PathingCell(Vec3i pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
		this.setup();
	} 
	
	public void updateConnectivity(PathingEdge from, int eval) {
		if (this.lastEvaluation != eval) {
			this.from = from;
			this.lastEvaluation = eval;
		}
	}
	
	private void setup() {
		this.minecraftPathingNode = new PathNode(this.getX(), this.getY(), this.getZ());
	}
}
