package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.minecraft.nbt.NbtCompound;

public abstract class SimpleModifierEconomy implements ModifierEconomy {
	protected float currentPrice;
	private float volatility;
	private float basePrice;
	private float returnRate;
	
	public SimpleModifierEconomy(float price, float volatility, float returnRate) {
		this.volatility = volatility;
		this.currentPrice = price;
		this.basePrice = price;
		this.returnRate = returnRate;
	}

	@Override
	public void modifiy(AmaziaTradeOffer offer) {
		offer.modifiedValue = offer.modifiedValue + this.currentPrice;
	}
	
	@Override
	public void reset() {
		this.currentPrice = this.basePrice;
	}
	
	@Override
	public void update() {
		this.currentPrice = this.currentPrice - (this.currentPrice - this.basePrice) * this.returnRate;
	}

	@Override
	public void update(int quanity, boolean isBuy) {
		this.currentPrice = this.currentPrice + this.getPriceDelta(quanity, isBuy);
		this.constrain();
	}
	
	protected float getPriceDelta(int quantity, boolean buy) {
		return buy ? -this.volatility * quantity : this.volatility * quantity;
	}

	protected abstract void constrain();
	
	@Override
	public float getCurrentPrice() {
		return this.currentPrice;
	}
	
	@Override
	public void setCurrentPrice(float value) {
		this.currentPrice = value;
	}
	
	@Override
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putFloat("value", this.currentPrice);
		return nbt; 
	}
	
	@Override
	public void fromNbt(NbtCompound nbt) {
		this.currentPrice = nbt.contains("value") ? nbt.getFloat("value") : this.basePrice;
	}

}
