package net.denanu.amazia.economy.offerModifiers;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.minecraft.nbt.NbtCompound;

public interface ModifierEconomy {
	public void modifiy(AmaziaTradeOffer offer);
		// used to update prices on purchase
	public void update(int quanity, boolean isBuy);
		// run as part of the price return methods for exponential return
	public void update();
	public float getCurrentPrice();
	public void reset();
	public void setCurrentPrice(float value);
	
	public NbtCompound toNbt();
	public void fromNbt(NbtCompound nbt);
}
