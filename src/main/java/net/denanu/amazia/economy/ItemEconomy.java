package net.denanu.amazia.economy;

import net.minecraft.item.ItemStack;

public class ItemEconomy {
	private final ItemStack itemStack;
	private final int baseValue;
	private final int volatility;
	private final int returnRate;
	
	private float currentPrice;
	
	public ItemEconomy(ItemStack stack, int baseValue, int volatility, int returnRate) {
		this.itemStack = stack;
		this.baseValue = baseValue;
		this.volatility = volatility;
		this.returnRate = returnRate;
		
		this.reset();
	}
	
	public final ItemStack getStack() {
		return this.itemStack;
	}
	
	public float getCurrentPrice() {
		return Math.max(1, this.currentPrice);
	}
	
	public void update() {
		this.currentPrice = this.currentPrice - (this.currentPrice - this.baseValue) * this.returnRate;
	}
	
	public void buy() {
		this.currentPrice = this.currentPrice + this.volatility;
	}
	
	public void sell() {
		this.currentPrice = this.currentPrice - this.volatility;
	}
	
	
	public void reset() {
		this.currentPrice = this.baseValue;
	}

	public String getName() {
		return this.itemStack.toString();
	}
}
