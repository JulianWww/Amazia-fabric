package net.denanu.amazia.components.entity;

import net.minecraft.nbt.NbtCompound;

public class PartOfVillageComponent implements IEntityBooleanCompnent {
	private boolean value;

	@Override
	public void readFromNbt(NbtCompound tag) {
		tag.putBoolean("inVillage", this.value);
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		this.value = tag.getBoolean("isVillage");
	}

	@Override
	public boolean getValue() {
		return this.value;
	}

}
