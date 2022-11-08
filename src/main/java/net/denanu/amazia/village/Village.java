package net.denanu.amazia.village;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.utils.CuboidSampler;
import net.denanu.amazia.village.sceduling.FarmingSceduler;
import net.denanu.amazia.village.sceduling.LumberSceduler;
import net.denanu.amazia.village.sceduling.MineingSceduler;
import net.denanu.amazia.village.sceduling.RancherSceduler;
import net.denanu.amazia.village.sceduling.StorageSceduler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class Village {
	private static int SIZE = 16;
	private static int SCAN_SIZE = 10;
	private static int THROTTLE = 1;
	
	private BlockPos origin;
	private boolean valid;
	
	private FarmingSceduler farming;
	private StorageSceduler storage;
	private MineingSceduler mineing;
	private LumberSceduler  lumber;
	private RancherSceduler ranching;

	private PathingGraph pathingGraph;
	
	private CuboidSampler sampler;
	@Nullable
	private VillageCoreBlockEntity coreBlock;
	
	private Box boundingBox;
	
	public Village(VillageCoreBlockEntity core) {
		this.coreBlock = core;
		this.farming 	= new FarmingSceduler(this);
		this.storage 	= new StorageSceduler(this);
		this.mineing 	= new MineingSceduler(this);
		this.lumber 	= new LumberSceduler (this);
		this.ranching	= new RancherSceduler(this);
		this.valid = true;
		
		this.boundingBox = new Box(
				core.getPos().getX() + SIZE,
				400,
				core.getPos().getZ() + SIZE,
				core.getPos().getX() - SIZE,
				-400,
				core.getPos().getZ() - SIZE
			);
	}
	
	public void setupVillage() {
		this.origin = this.coreBlock.getPos();
		
		this.pathingGraph = new PathingGraph((ServerWorld)this.coreBlock.getWorld(), this);
		
		this.farming.initialize();
		this.storage.initialize();
		this.mineing.initialize();
		this.lumber.initialize();
		this.ranching.initialize();
		
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
	
    public NbtCompound writeNbt() {
    	NbtCompound nbt = new NbtCompound();
		nbt.putBoolean("isValid", valid);
		nbt.put("farming",  farming. writeNbt());
		nbt.put("storage",  storage. writeNbt());
		nbt.put("mineing",  mineing. writeNbt());
		nbt.put("lumber",   lumber.  writeNbt());
		nbt.put("ranching", ranching.writeNbt());
		return nbt;
    }
    public void readNbt(NbtCompound nbt) {
    	valid = nbt.getBoolean("isValid");
    	farming. readNbt(nbt.getCompound("farming"));
    	storage. readNbt(nbt.getCompound("storage"));
    	mineing. readNbt(nbt.getCompound("mineing"));
    	lumber.  readNbt(nbt.getCompound("lumber"));
    	ranching.readNbt(nbt.getCompound("ranching"));
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
		this.farming. discover(world, blockPos);
		this.storage. discover(world, blockPos);
		this.mineing. discover(world, blockPos);
		this.lumber.  discover(world, blockPos);
		this.ranching.discover(world, blockPos);
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
	public LumberSceduler getLumber() {
		return lumber;
	}
	public RancherSceduler getRanching() {
		return this.ranching;
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
	
	public Box getBox() {
		return this.boundingBox;
	}

	public void onVillageBlockUpdate(BlockPos pos) {
		if (this.isInVillage(pos)) {
			this.discover((ServerWorld)this.getWorld(), pos);
		}
	}
}
