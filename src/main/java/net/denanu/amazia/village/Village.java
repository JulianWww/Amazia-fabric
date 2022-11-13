package net.denanu.amazia.village;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.village.sceduling.BlacksmithingSceduler;
import net.denanu.amazia.village.sceduling.EnchantingSceduler;
import net.denanu.amazia.village.sceduling.FarmingSceduler;
import net.denanu.amazia.village.sceduling.LumberSceduler;
import net.denanu.amazia.village.sceduling.MineingSceduler;
import net.denanu.amazia.village.sceduling.RancherSceduler;
import net.denanu.amazia.village.sceduling.StorageSceduler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class Village {
	private static int SIZE = 16;
	private BlockPos origin;
	private boolean valid;
	
	private FarmingSceduler farming;
	private StorageSceduler storage;
	private MineingSceduler mineing;
	private LumberSceduler  lumber;
	private RancherSceduler ranching;
	private EnchantingSceduler enchanting;
	private BlacksmithingSceduler blacksmithing;

	private PathingGraph pathingGraph;
	
	@Nullable
	private VillageCoreBlockEntity coreBlock;
	
	private Box boundingBox;
	
	public Village(VillageCoreBlockEntity core) {
		this.coreBlock = core;
		this.farming 		= new FarmingSceduler  		(this);
		this.storage 		= new StorageSceduler   	(this);
		this.mineing 		= new MineingSceduler   	(this);
		this.lumber 		= new LumberSceduler    	(this);
		this.ranching		= new RancherSceduler   	(this);
		this.enchanting 	= new EnchantingSceduler	(this);
		this.blacksmithing 	= new BlacksmithingSceduler	(this);
		this.valid = true;
		
		this.boundingBox = new Box(
				core.getPos().getX() + Village.SIZE,
				400,
				core.getPos().getZ() + Village.SIZE,
				core.getPos().getX() - Village.SIZE,
				-400,
				core.getPos().getZ() - Village.SIZE
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
		this.enchanting.initialize();
		this.blacksmithing.initialize();
		
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
		nbt.putBoolean("isValid", 	this.valid);
		nbt.put("farming",   		this.farming.   writeNbt());
		nbt.put("storage",   		this.storage.   writeNbt());
		nbt.put("mineing",			this.mineing.   writeNbt());
		nbt.put("lumber",     		this.lumber.    writeNbt());
		nbt.put("ranching",  		this.ranching.  writeNbt());
		nbt.put("enchanting",		this.enchanting.writeNbt());
		nbt.put("blacksmithing", 	this.blacksmithing.writeNbt());
		return nbt;
    }
    public void readNbt(NbtCompound nbt) {
    	this.valid = nbt.getBoolean("isValid");
    	this.farming.   	readNbt(nbt.getCompound("farming"));
    	this.storage.   	readNbt(nbt.getCompound("storage"));
    	this.mineing.   	readNbt(nbt.getCompound("mineing"));
    	this.lumber.    	readNbt(nbt.getCompound("lumber"));
    	this.ranching. 		readNbt(nbt.getCompound("ranching"));
    	this.enchanting.	readNbt(nbt.getCompound("enchanting"));
    	this.blacksmithing.	readNbt(nbt.getCompound("blacksmithing"));
    }
	
	public boolean isValid() {
		return this.valid;
	}
	public void destroy() {
		this.valid = false;
	}
	
	public void tick(ServerWorld world) {
		this.update();
		return;
	}
	private void update() {
		this.pathingGraph.update();
	}
	private void discover(ServerWorld world, BlockPos blockPos) {
		this.farming.   	discover(world, blockPos);
		this.storage.   	discover(world, blockPos);
		this.mineing.   	discover(world, blockPos);
		this.lumber.    	discover(world, blockPos);
		this.ranching.  	discover(world, blockPos);
		this.enchanting.	discover(world, blockPos);
		this.blacksmithing.	discover(world, blockPos);
	}


	public void setChanged() {
		if (this.coreBlock != null) {
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
		return this.storage;
	}
	public MineingSceduler getMineing() {
		return this.mineing;
	}
	public LumberSceduler getLumber() {
		return this.lumber;
	}
	public RancherSceduler getRanching() {
		return this.ranching;
	}
	public EnchantingSceduler getEnchanting() {
		return this.enchanting;
	}
	public BlacksmithingSceduler getBlacksmithing() {
		return this.blacksmithing;
	}

	public static int getSize() {
		return Village.SIZE;
	}
	
	public boolean isInVillage(final BlockPos pos) {
		if (this.getOrigin() == null) {
			return false;
		}
        return Math.max(Math.abs(this.getOrigin().getX() - pos.getX()), Math.abs(this.getOrigin().getZ() - pos.getZ())) < Village.SIZE;
    }

	public void onPathingBlockUpdate(BlockPos pos) {
		if (this.getPathingGraph() != null && this.isInVillage(pos)) {
			ServerWorld world = this.getWorld();
			this.pathingGraph.onBlockUpdate(world, pos);
		}
	}

	public ServerWorld getWorld() {
		return this.pathingGraph.getWorld();
	}
	
	public Box getBox() {
		return this.boundingBox;
	}

	public void onVillageBlockUpdate(BlockPos pos) {
		if (this.isInVillage(pos)) {
			this.discover(this.getWorld(), pos);
		}
	}
}
