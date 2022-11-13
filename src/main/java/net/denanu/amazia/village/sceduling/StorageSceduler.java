package net.denanu.amazia.village.sceduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.goal.storage.InteractWithContainerGoal;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class StorageSceduler extends FacingPathingVillageSceduler {
	private HashMap<BlockPos, DoubleDownPathingData> chests;
	private List<BlockPos> blockSetupHolder;

	public StorageSceduler(Village _village) {
		super(_village);
		this.chests = new HashMap<BlockPos, DoubleDownPathingData>();
		this.blockSetupHolder = new LinkedList<BlockPos>();
	}

	@Override
	public NbtCompound writeNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.put("chests", NbtUtils.toNbt(this.toBlockPosList()));
		return nbt;
	}
	@Override
	public void readNbt(NbtCompound nbt) {
		this.blockSetupHolder = NbtUtils.toBlockPosList(nbt.getList("chests", NbtElement.LIST_TYPE));
		return;
	}

	private List<BlockPos> toBlockPosList() {
		List<BlockPos> out = new LinkedList<BlockPos>();
		out.addAll(this.chests.keySet());
		return out;
	}

	@Override
	public void initialize() {
		this.fromBlockPosList(this.blockSetupHolder);
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
		return StorageSceduler.isBarrel(block) || StorageSceduler.isAcessibleChest(world, block, pos);
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		this.discoverChests(world, pos);
		this.discoverChests(world, pos.down());
	}

	private void discoverChests(ServerWorld world, BlockPos pos) {
		if (StorageSceduler.isStorageBlock(world, pos)) {
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
				this.chests.put(pos, new DoubleDownPathingData(pos, this.getVillage(), facing));
				VillageSceduler.markBlockAsFound(pos, world);
			}
		}
	}
	private void removeChest(ServerWorld world, BlockPos pos) {
		if (this.chests.containsKey(pos)) {
			this.chests.get(pos).destroy(this.getVillage());
			this.chests.remove(pos);
			VillageSceduler.markBlockAsLost(pos, world);
		}
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

	public DoubleDownPathingData getDepositLocation(ServerWorld world, ItemStack item) {
		Amazia.LOGGER.info("Scanned storage depositing");
		DoubleDownPathingData potential = null;
		for (Entry<BlockPos, DoubleDownPathingData> pos : this.chests.entrySet()) {
			Pair<Boolean, Boolean> data = StorageSceduler.canAddItem(pos.getValue().getStorageInventory(world), item);
			if (data.getLeft() && data.getRight()) {
				return pos.getValue();
			}
			if (data.getRight()) {
				potential = pos.getValue();
			}
		}
		return potential;
	}

	public DoubleDownPathingData getRequestLocation(ServerWorld world, Item item) {
		for (Entry<BlockPos, DoubleDownPathingData> pos : this.chests.entrySet()) {
			if (StorageSceduler.containsItem(pos.getValue().getStorageInventory(world), item)) {
				return pos.getValue();
			}
		}
		return null;
	}

	public boolean itemInStorage(ServerWorld world, Item itm)  {
		return this.getRequestLocation(world, itm) != null;
	}

	@Override
	protected void addPathingOption(BlockPos pos, Direction facing) {
		this.chests.put(pos, new DoubleDownPathingData(pos, this.getVillage(), facing));
	}
}
