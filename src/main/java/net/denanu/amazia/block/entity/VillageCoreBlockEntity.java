package net.denanu.amazia.block.entity;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VillageCoreBlockEntity extends BlockEntity {
	private Village village;
	private boolean initialized = false;

	public VillageCoreBlockEntity(BlockPos pos, BlockState state) {
		super(AmaziaBlockEntities.VILLAGE_CORE, pos, state);
	}
	
	@Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        village.writeNbt(nbt, "village");
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.initialize();
        //village.readNbt(nbt, "village");
    }
    
    public Village getVillage() {
    	return this.village;
    }
    
    public void initialize() {
    	if (!this.initialized) {
			this.village = new Village(this);
			this.village.getPathingGraph().seedVillage(pos.up());
    		this.initialized = true;
    	}
    }
	
	public static void tick(World world, BlockPos pos, BlockState state, VillageCoreBlockEntity e) {
		Amazia.LOGGER.info("hi from Villag Core");
		if (!world.isClient()) {
			e.initialize();
			e.village.tick((ServerWorld)world);
		}
	}

	public void _setChanged() {
		this.markDirty();
	}
}
