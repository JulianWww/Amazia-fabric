package net.denanu.amazia.village;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.pathing.PathingGraph;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class Village {
	private static int SIZE = 120;
	private static int SIZE_OFFSET = SIZE/2;
	private static int MIN_Y = -64;
	private static int MAX_Y = 128;
	private BlockPos origin;
	
	private int scanPos;
	private boolean valid;
	private FarmingSceduler farming;
	@Nullable
	private VillageCoreBlockEntity coreBlock;
	private PathingGraph pathingGraph;
	
	public Village(VillageCoreBlockEntity core) {
		this.valid = true;
		this.coreBlock = core;
		this.origin = core.getPos();
		
		this.farming = new FarmingSceduler(this);
		this.pathingGraph = new PathingGraph(core.getWorld(), this);
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
	}
	private void scan(ServerWorld world) {
		BlockPos pos = this.getColumn();
		if (pos != null) {
			for (int idx=MIN_Y; idx<MAX_Y; idx++) {
				this.discover(world, new BlockPos(pos.getX(), idx, pos.getZ()));
			}
		}
	}
	private void discover(ServerWorld world, BlockPos blockPos) {
		this.farming.discover(world, blockPos);
	}
	private BlockPos getColumn() {
		int dx = this.scanPos % Village.SIZE;
		int dz = (this.scanPos - dx) / Village.SIZE;
		if (dz > Village.SIZE) {
			this.scanPos = 0;
			return this.getColumn();
		}
		else {
			this.scanPos++;
			BlockPos pos = new BlockPos(dx-Village.SIZE_OFFSET, 0, dz-Village.SIZE_OFFSET);
			return this.toGlobalPos(pos);
		}
	}

	private BlockPos toGlobalPos(BlockPos pos) {
		return pos.add(this.getOrigin());
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
	
	public boolean isInVillage(final BlockPos pos) {
        return Math.max(Math.abs(this.getOrigin().getX() - pos.getX()), Math.abs(this.getOrigin().getZ() - pos.getZ())) < SIZE;
    }
}
