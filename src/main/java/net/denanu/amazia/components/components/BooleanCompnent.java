package net.denanu.amazia.components.components;

import net.minecraft.nbt.NbtCompound;

public class BooleanCompnent implements IBooleanCompnent {
	private boolean value;

	public BooleanCompnent(boolean value) {
		this.value = value;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.value = tag.getBoolean("value");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("value", this.value);
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
