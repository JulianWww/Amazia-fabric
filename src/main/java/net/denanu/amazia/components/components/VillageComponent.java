package net.denanu.amazia.components.components;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.utils.nbt.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

public class VillageComponent implements IVillageComponent {
	private List<BlockPos> villages;

	public VillageComponent() {
		this.villages = new ArrayList<BlockPos>();
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.villages = NbtUtils.toBlockPosList(tag.getList("villages", NbtElement.LIST_TYPE));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.put("villages", NbtUtils.toNbt(this.villages));
	}

	@Override
	public List<BlockPos> get() {
		return this.villages;
	}

	@Override
	public void add(BlockPos pos) {
		if (!this.villages.contains(pos)) {
			this.villages.add(pos);
		}
	}

}
