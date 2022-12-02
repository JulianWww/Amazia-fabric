package net.denanu.amazia.block.entity;

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

	public VillageCoreBlockEntity(final BlockPos pos, final BlockState state) {
		super(AmaziaBlockEntities.VILLAGE_CORE, pos, state);
		this.createVillage();
	}

	@Override
	protected void writeNbt(final NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("village", this.village.writeNbt());
	}
	@Override
	public void readNbt(final NbtCompound nbt) {
		super.readNbt(nbt);
		this.createVillage();
		this.village.readNbt(nbt.getCompound("village"));
	}

	public Village getVillage() {
		return this.village;
	}

	private void createVillage() {
		this.village = new Village(this);
	}

	private void initialize() {
		if (!this.initialized) {
			this.village.setupVillage();
			this.village.getPathingGraph().seedVillage(this.pos.up());
			this.initialized = true;
		}
	}

	public static void tick(final World world, final BlockPos pos, final BlockState state, final VillageCoreBlockEntity e) {
		if (!world.isClient()) {
			e.initialize();
			e.village.tick((ServerWorld)world);
		}
	}

	public void _setChanged() {
		this.markDirty();
	}

	@Override
	public void markRemoved() {
		super.markRemoved();
		this.village.remove(this.getWorld());
	}

	@Override
	public void cancelRemoval() {
		super.cancelRemoval();
		this.village.register(this.getWorld());
	}
}
