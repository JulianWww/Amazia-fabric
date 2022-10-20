package net.denanu.amazia.village.sceduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.storage.InteractWithContainerGoal;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class StorageSceduler extends VillageSceduler {
	private HashMap<BlockPos, StoragePathingData> chests;
	private List<BlockPos> blockSetupHolder;

	public StorageSceduler(Village _village) {
		super(_village);
		this.chests = new HashMap<BlockPos, StoragePathingData>();
		this.blockSetupHolder = new LinkedList<BlockPos>();
	}
	
	public void writeNbt(NbtCompound nbt, String name) {
		JJUtils.writeNBT(nbt, this.toBlockPosList(), name + ".chests");
    }
    public void readNbt(NbtCompound nbt, String name) {
    	this.blockSetupHolder = JJUtils.readNBT(nbt, name + ".chests");
    }
    
    private List<BlockPos> toBlockPosList() {
    	List<BlockPos> out = new LinkedList<BlockPos>();
    	for (BlockPos pos : this.chests.keySet()) {
    		out.add(pos);
    	}
    	return out;
    }
    
    protected Direction getFacing(BlockPos pos) {
    	BlockState state = this.getVillage().getPathingGraph().getWorld().getBlockState(pos);
    	if (state.getBlock() instanceof ChestBlock) {
    		return ChestBlock.getFacing(state);
    	}
    	else if (state.getBlock() instanceof BarrelBlock) {
    		return state.get(BarrelBlock.FACING);
    	}
    	return null;
    }
    
    private void fromBlockPosList(List<BlockPos> positions) {
    	for (BlockPos pos: positions) {
    		Direction facing = this.getFacing(pos);
    		if (facing != null) {
    			this.chests.put(pos, new StoragePathingData(pos, this.getVillage(), facing));
    		}
    	}
    }
    
    public void initialize() {
    	this.fromBlockPosList(blockSetupHolder);
    	this.blockSetupHolder.clear();
    	return;
    }
	
	protected static boolean isAcessibleChest(ServerWorld world, Block block, BlockPos pos) {
		if (block instanceof ChestBlock chest) {
			return !ChestBlock.isChestBlocked(world, pos);
		}
		return false;
	}
	protected static boolean isBarrel(Block block) {
		return block.equals(Blocks.BARREL);
	}
	
	protected static boolean isStorageBlock(ServerWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return (
				isBarrel(block) || isAcessibleChest(world, block, pos)
			);
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		this.discoverChests(world, pos);
		this.discoverChests(world, pos.down());
	}
	
	private void discoverChests(ServerWorld world, BlockPos pos) {
		if (isStorageBlock(world, pos)) {
			this.addChest(world, pos);
		}
		else {
			this.removeChest(world, pos);
		}
	}

	private void addChest(ServerWorld world, BlockPos pos) {
		if (!this.chests.containsKey(pos)) {
			Direction facing = this.getFacing(pos);
    		if (facing != null) {
    			this.chests.put(pos, new StoragePathingData(pos, this.getVillage(), facing));
    			markBlockAsFound(pos, world);
    		}
		}
	}
	private void removeChest(ServerWorld world, BlockPos pos) {
		if (this.chests.containsKey(pos)) {
			this.chests.get(pos).destroy(this.getVillage());
			this.chests.remove(pos);
			markBlockAsLost(pos, world);
		}
	}

	@Override
	public BlockPos getRandomPos(ServerWorld world, AmaziaVillagerEntity entity) {
		return null;
	}
	
	// Pair of contains item type, can add
	public static Pair<Boolean, Boolean> canAddItem(LootableContainerBlockEntity lootableContainerBlockEntity, ItemStack itm) {
		boolean contains = false;
		boolean canAdd = false;
		
		for (int idx=0; idx<lootableContainerBlockEntity.size(); idx++) {
			ItemStack stack = lootableContainerBlockEntity.getStack(idx);
			boolean canMerge = InteractWithContainerGoal.canMergeItems(stack, itm);
			if (stack.isEmpty() || canMerge) {
				canAdd = true;
			}
			if (canMerge) {
				contains = true;
			}
			if (contains && canAdd) { break; }
		}
		return new Pair<>(contains, canAdd);
	}
	
	public static boolean containsItem(LootableContainerBlockEntity lootableContainerBlockEntity, Item itm) {
		for (int idx=0; idx<lootableContainerBlockEntity.size(); idx++) {
			if (lootableContainerBlockEntity.getStack(idx).isOf(itm)) { 
				return true; 
			}
		}
		return false;
	}
	
	public StoragePathingData getDepositLocation(ServerWorld world, ItemStack item) {
		StoragePathingData potential = null;
		for (Entry<BlockPos, StoragePathingData> pos : this.chests.entrySet()) {
			Pair<Boolean, Boolean> data = canAddItem(pos.getValue().getStorageInventory(world), item);
			if (data.getLeft() && data.getRight()) { return pos.getValue(); }
			if (data.getRight()) {
				potential = pos.getValue();
			}
		}
		return potential;
	}

	public StoragePathingData getRequestLocation(ServerWorld world, Item item) {
		for (Entry<BlockPos, StoragePathingData> pos : this.chests.entrySet()) {
			if (containsItem(pos.getValue().getStorageInventory(world), item)) 
			return pos.getValue();
		}
		return null;
	}
}
