package net.denanu.amazia.components.entity;

import net.minecraft.nbt.NbtCompound;

public class BooleanCompnent implements IBooleanCompnent {
	private boolean value = false;

	public BooleanCompnent(boolean value) {
		this.value = value;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.value = tag.getBoolean("isVillage");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("inVillage", this.value);
	}

	@Override
	public boolean getValue() {
		return this.value;
	}

	@Override
	public void setValue(boolean value) {
		this.value = value;
	}

}
