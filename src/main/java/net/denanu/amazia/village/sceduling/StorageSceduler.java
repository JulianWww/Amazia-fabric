package net.denanu.amazia.village.sceduling;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.denanu.amazia.entities.village.server.goal.storage.InteractWithContainerGoal;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaIds;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.denanu.blockhighlighting.Highlighter;
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
	private final HashMap<BlockPos, DoubleDownPathingData> chests;
	private List<BlockPos> blockSetupHolder;

	public StorageSceduler(final Village _village) {
		super(_village);
		this.chests = new HashMap<>();
		this.blockSetupHolder = new LinkedList<>();
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.put("chests", NbtUtils.toNbt(this.toBlockPosList()));
		return nbt;
	}
	@Override
	public void readNbt(final NbtCompound nbt) {
		this.blockSetupHolder = NbtUtils.toBlockPosList(nbt.getList("chests", NbtElement.LIST_TYPE));
	}

	private List<BlockPos> toBlockPosList() {
		return new LinkedList<>(this.chests.keySet());
	}

	@Override
	public void initialize() {
		this.fromBlockPosList(this.blockSetupHolder);
		this.blockSetupHolder.clear();
	}

	protected static boolean isAcessibleChest(final ServerWorld world, final Block block, final BlockPos pos) {
		if (block instanceof final ChestBlock chest) {
			return !ChestBlock.isChestBlocked(world, pos);
		}
		return false;
	}
	protected static boolean isBarrel(final Block block) {
		return block.equals(Blocks.BARREL);
	}

	protected static boolean isStorageBlock(final ServerWorld world, final BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return StorageSceduler.isBarrel(block) || StorageSceduler.isAcessibleChest(world, block, pos);
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		this.discoverChests(world, pos);
		this.discoverChests(world, pos.down());
	}

	private void discoverChests(final ServerWorld world, final BlockPos pos) {
		if (StorageSceduler.isStorageBlock(world, pos)) {
			this.addChest(world, pos);
		}
		else {
			this.removeChest(world, pos);
		}
	}

	private void addChest(final ServerWorld world, final BlockPos pos) {
		if (!this.chests.containsKey(pos)) {
			final Direction facing = this.getFacing(pos);
			if (facing != null) {
				this.chests.put(pos, new DoubleDownPathingData(pos, this.getVillage(), facing));
				VillageSceduler.markBlockAsFound(pos, world);
			}

			Highlighter.highlight(world, BlockHighlightingAmaziaIds.STORAGE, pos);
		}
	}
	private void removeChest(final ServerWorld world, final BlockPos pos) {
		if (this.chests.containsKey(pos)) {
			this.chests.get(pos).destroy(this.getVillage());
			this.chests.remove(pos);
			VillageSceduler.markBlockAsLost(pos, world);

			Highlighter.unhighlight(world, BlockHighlightingAmaziaIds.STORAGE, pos);
		}
	}

	// Pair of contains item type, can add
	public static Pair<Boolean, Boolean> canAddItem(final LootableContainerBlockEntity lootableContainerBlockEntity, final ItemStack itm) {
		boolean contains = false;
		boolean canAdd = false;

		for (int idx=0; idx<lootableContainerBlockEntity.size(); idx++) {
			final ItemStack stack = lootableContainerBlockEntity.getStack(idx);
			final boolean canMerge = InteractWithContainerGoal.canMergeItems(stack, itm);
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

	public static boolean containsItem(final LootableContainerBlockEntity lootableContainerBlockEntity, final Item itm) {
		for (int idx=0; idx<lootableContainerBlockEntity.size(); idx++) {
			if (lootableContainerBlockEntity.getStack(idx).isOf(itm)) {
				return true;
			}
		}
		return false;
	}

	public DoubleDownPathingData getDepositLocation(final ServerWorld world, final ItemStack item) {
		DoubleDownPathingData potential = null;
		for (final Entry<BlockPos, DoubleDownPathingData> pos : this.chests.entrySet()) {
			final Pair<Boolean, Boolean> data = StorageSceduler.canAddItem(pos.getValue().getStorageInventory(world), item);
			if (data.getLeft() && data.getRight()) {
				return pos.getValue();
			}
			if (data.getRight()) {
				potential = pos.getValue();
			}
		}
		return potential;
	}

	public DoubleDownPathingData getRequestLocation(final ServerWorld world, final Item item) {
		for (final Entry<BlockPos, DoubleDownPathingData> pos : this.chests.entrySet()) {
			if (StorageSceduler.containsItem(pos.getValue().getStorageInventory(world), item)) {
				return pos.getValue();
			}
		}
		return null;
	}

	public boolean itemInStorage(final ServerWorld world, final Item itm)  {
		return this.getRequestLocation(world, itm) != null;
	}

	@Override
	protected void addPathingOption(final BlockPos pos, final Direction facing) {
		this.chests.put(pos, new DoubleDownPathingData(pos, this.getVillage(), facing));
	}

	public Collection<DoubleDownPathingData> getAccessPoints() {
		return this.chests.values();
	}
}
