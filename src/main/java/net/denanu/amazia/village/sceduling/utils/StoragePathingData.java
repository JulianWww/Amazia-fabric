package net.denanu.amazia.village.sceduling.utils;

import net.denanu.amazia.mixin.ChestInventoryAccessMixin;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.mixin.BarrelInventoryAccessMixin;
import net.denanu.amazia.village.Village;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class StoragePathingData extends BlockAreaPathingData {
	private Direction preferedDirection;

	public StoragePathingData(BlockPos pos, Village village, Direction _preferedDirection) {
		super(pos, village);
		this.preferedDirection = _preferedDirection;
	}

	public Direction getPreferedDirection() {
		return preferedDirection;
	}

	public void setPreferedDirection(Direction preferedDirection) {
		this.preferedDirection = preferedDirection;
	}

	@Override
	protected void getAccessPoints(PathingListenerRegistryOperation operation, BlockPos origin, Village v) {
		operation.put(this, v, origin.up());
		this.registerSide(operation, origin.east(), v);
		this.registerSide(operation, origin.west(), v);
		this.registerSide(operation, origin.north(), v);
		this.registerSide(operation, origin.south(), v);
	}
	
	private void registerSide(PathingListenerRegistryOperation operation, BlockPos pos, Village v) {
		operation.put(this, v, pos);
		operation.put(this, v, pos.down());
		operation.put(this, v, pos.down(2));
	}
	
	private BlockPos getPosibilityOnSide(BlockPos pos) {
		if (this.getPathingOptions().contains(pos)) {
			return pos;
		}
		if (this.getPathingOptions().contains(pos.down())) {
			return pos;
		}
		if (this.getPathingOptions().contains(pos.down(2))) {
			return pos;
		}
		return null;
	}
	private BlockPos getTopAccess() {
		if (this.getPathingOptions().contains(this.getPos().up())) { return this.getPos().up(); }
		return null;
	}

	
	public BlockPos getAccessPoint() {
		BlockPos out = this.getPosibilityOnSide(this.getPos().offset(this.preferedDirection));  if (out != null) { return out; }
		out = this.getPosibilityOnSide(this.getPos().north()); 									if (out != null) { return out; }
		out = this.getPosibilityOnSide(this.getPos().south());									if (out != null) { return out; }
		out = this.getPosibilityOnSide(this.getPos().east());									if (out != null) { return out; }
		out = this.getPosibilityOnSide(this.getPos().west());									if (out != null) { return out; }
		return this.getTopAccess();
	}
	
	public LootableContainerBlockEntity getStorageInventory(ServerWorld world) {
		BlockEntity entity = world.getBlockEntity(this.getPos());
		if (entity instanceof LootableContainerBlockEntity chest) {
			return chest;
		}
		Amazia.LOGGER.warn("Amazai Storage discovery missed a block");
		return null;
	}
}