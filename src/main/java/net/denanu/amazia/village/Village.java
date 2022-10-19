package net.denanu.amazia.village;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.utils.CuboidSampler;
import net.denanu.amazia.village.sceduling.FarmingSceduler;
import net.denanu.amazia.village.sceduling.MineingSceduler;
import net.denanu.amazia.village.sceduling.StorageSceduler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Village {
	private static int SIZE = 120;
	private static int SCAN_SIZE = 10;
	private static int THROTTLE = 1;
	
	private BlockPos origin;
	private boolean valid;
	
	private FarmingSceduler farming;
	private StorageSceduler storage;
	private MineingSceduler mineing;

	private PathingGraph pathingGraph;
	
	private CuboidSampler sampler;
	@Nullable
	private VillageCoreBlockEntity coreBlock;
	
	public Village(VillageCoreBlockEntity core) {
		this.coreBlock = core;
		this.farming = new FarmingSceduler(this);
		this.storage = new StorageSceduler(this);
		this.mineing = new MineingSceduler(this);
		this.valid = true;
	}
	
	public void setupVillage() {
		this.origin = this.coreBlock.getPos();
		
		this.pathingGraph = new PathingGraph((ServerWorld)this.coreBlock.getWorld(), this);
		
		this.storage.initialize();
		this.mineing.initialize();
		
		this.sampler = new CuboidSampler(this.getOrigin(), SCAN_SIZE, SCAN_SIZE, SCAN_SIZE);
		
		this.register(this.coreBlock.getWorld());
	}
	
	public void remove(World world) {
		if (!world.isClient()) {
			Amazia.getVillageManager().removeVillage(this);
		}
	}
	public void register(World world) {
		if (!world.isClient()) {
			Amazia.getVillageManager().addVillage(this);
		}
	}
	
    public void writeNbt(NbtCompound nbt, String name) {
		nbt.putBoolean("village.isValid", valid);
		farming.writeNbt(nbt, name + ".farming");
		storage.writeNbt(nbt, name + ".storage");
		mineing.writeNbt(nbt, name + ".mineing");
    }
    public void readNbt(NbtCompound nbt, String name) {
    	valid = nbt.getBoolean("village.isValid");
    	farming.readNbt(nbt, name + ".farming");
    	storage.readNbt(nbt, name + ".storage");
    	mineing.readNbt(nbt, name + ".mineing");
    }
	
	public boolean isValid() {
		return this.valid;
	}
	public void destroy() {
		this.valid = false;
	}
	
	public void tick(ServerWorld world) {
		//this.scan(world);
		this.update();
		return;
	}
	private void update() {
		this.pathingGraph.update();
		//this.testPathfinding();
	}
	private void scan(ServerWorld world) {
		PlayerEntity player = world.getClosestPlayer(this.origin.getX(), this.origin.getY(), this.origin.getZ(), 2*SIZE, false);
		if (player != null) {
			this.sampler.setPos(player.getPos());
			for (int idx=0; idx<THROTTLE; idx++) {
				BlockPos pos = this.sampler.getPos();
				this.discover(world, pos);
			}
		}
	}
	private void discover(ServerWorld world, BlockPos blockPos) {
		this.farming.discover(world, blockPos);
		this.storage.discover(world, blockPos);
		this.mineing.discover(world, blockPos);
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
	public StorageSceduler getStorage() {
		return storage;
	}
	public MineingSceduler getMineing() {
		return mineing;
	}

	public static int getSize() {
		return Village.SIZE;
	}
	
	public boolean isInVillage(final BlockPos pos) {
		if (this.getOrigin() == null) {
			return false;
		}
        return Math.max(Math.abs(this.getOrigin().getX() - pos.getX()), Math.abs(this.getOrigin().getZ() - pos.getZ())) < SIZE;
    }

	public void onPathingBlockUpdate(BlockPos pos) {
		if (this.getPathingGraph() != null && this.isInVillage(pos)) {
			ServerWorld world = this.getWorld();
			this.pathingGraph.onBlockUpdate(world, pos);
		}
	}

	public ServerWorld getWorld() {
		return (ServerWorld)this.pathingGraph.getWorld();
	}

	public void onVillageBlockUpdate(BlockPos pos) {
		if (this.isInVillage(pos)) {
			this.discover((ServerWorld)this.getWorld(), pos);
		}
	}
}
