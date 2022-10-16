package net.denanu.amazia.village;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.pathing.BasePathingNode;
import net.denanu.amazia.pathing.PathFinder;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.utils.CuboidWorldSampler;
import net.denanu.amazia.village.sceduling.FarmingSceduler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;

public class Village {
	private static int SIZE = 120;
	private static int THROTTLE = 20000;
	
	private BlockPos origin;
	private boolean valid;
	
	private FarmingSceduler farming;
	private PathingGraph pathingGraph;
	
	private CuboidWorldSampler sampler;
	@Nullable
	private VillageCoreBlockEntity coreBlock;
	
	public Village(VillageCoreBlockEntity core) {
		this.valid = true;
		this.coreBlock = core;
		this.origin = core.getPos();
		
		this.farming = new FarmingSceduler(this);
		this.pathingGraph = new PathingGraph(core.getWorld(), this);
		
		this.sampler = new CuboidWorldSampler(this.getOrigin(), SIZE*2, SIZE*2);
	}
	
    public void writeNbt(NbtCompound nbt, String name) {
		nbt.putBoolean("village.isValid", valid);
		farming.writeNbt(nbt, name + ".farming");
    }
    public void readNbt(NbtCompound nbt, String name) {
    	valid = nbt.getBoolean("village.isValid");
    	farming.readNbt(nbt, name + ".farming");
    }
	
	public boolean isValid() {
		return this.valid;
	}
	public void destroy() {
		this.valid = false;
	}
	
	public void tick(ServerWorld world) {
		this.scan(world);
		this.update();
	}
	private void update() {
		this.pathingGraph.update();
		//this.testPathfinding();
	}
	private void scan(ServerWorld world) {
		for (int idx=0; idx<THROTTLE; idx++) {
			BlockPos pos = this.sampler.getPos();
			this.discover(world, pos);
		}
	}
	private void discover(ServerWorld world, BlockPos blockPos) {
		this.farming.discover(world, blockPos);
	}


	public void setChanged() {
		if (coreBlock != null) {
			this.coreBlock._setChanged();
		}
	}
	
	public BlockPos getOrigin() {
		return this.origin;
	}
	public PathingGraph getPathingGraph() {
        return this.pathingGraph;
    }
	public FarmingSceduler getFarming() {
		return this.farming;
	}
	public static int getSize() {
		return Village.SIZE;
	}
	
	public boolean isInVillage(final BlockPos pos) {
        return Math.max(Math.abs(this.getOrigin().getX() - pos.getX()), Math.abs(this.getOrigin().getZ() - pos.getZ())) < SIZE;
    }
}
