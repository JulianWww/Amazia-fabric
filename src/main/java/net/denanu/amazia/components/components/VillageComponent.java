package net.denanu.amazia.components.components;

import java.util.HashSet;

import net.denanu.amazia.utils.nbt.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

public class VillageComponent implements IVillageComponent {
	private HashSet<BlockPos> villages;

	public VillageComponent() {
		this.villages = new HashSet<>();
	}

	@Override
	public void readFromNbt(final NbtCompound tag) {
		this.villages = NbtUtils.toBlockPosSet(tag.getList("villages", NbtElement.LIST_TYPE));
	}

	@Override
	public void writeToNbt(final NbtCompound tag) {
		tag.put("villages", NbtUtils.toNbt(this.villages));
	}

	@Override
	public HashSet<BlockPos> get() {
		return this.villages;
	}

	@Override
	public void add(final BlockPos pos) {
		if (!this.villages.contains(pos)) {
			this.villages.add(pos);
		}
	}

	@Override
	public void remove(final BlockPos pos) {
		this.villages.remove(pos);
	}

}
