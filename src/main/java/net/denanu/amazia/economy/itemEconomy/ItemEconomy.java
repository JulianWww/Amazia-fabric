package net.denanu.amazia.economy.itemEconomy;

import java.util.ArrayList;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class ItemEconomy extends BaseItemEconomy {
	public static final int MAX_VALUE = 576;
	
	private final float baseValue;
	private final float volatility;
	private final float returnRate;
	private final RandomnessFactory<Integer> stackSizeFactory;
	
	private float currentPrice;
	
	public ItemEconomy(Item itm, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory) {
		super(itm);
		this.baseValue = baseValue;
		this.volatility = volatility;
		this.returnRate = returnRate;
		this.stackSizeFactory = stackSizeFactory;
		
		this.reset();
	}
	
	protected final ItemStack getStack() {
		return new ItemStack(this.itm, this.stackSizeFactory.next());
	}
	
	public float getCurrentPrice() {
		return this.currentPrice;
	}
	
	public void update() {
		this.currentPrice = this.currentPrice - (this.currentPrice - this.baseValue) * this.returnRate;
	}
	
	public void updatePrice(float quantity, boolean buy) {
		this.currentPrice = this.currentPrice + this.getPriceDelta(quantity, buy);
		if (this.currentPrice < 0) { this.currentPrice = 0; }
		if (this.currentPrice > MAX_VALUE) { this.currentPrice = MAX_VALUE; }
	}
	
	public float getPriceDelta(float quantity, boolean buy) {
		return buy ? -this.volatility * quantity : this.volatility * quantity;
	}
	
	
	public void reset() {
		this.currentPrice = this.baseValue;
	}

	public void setCurrentValue(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public NbtElement toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putFloat("value", this.currentPrice);
		return nbt;
	}

	public void fromNbt(NbtCompound nbt) {
		this.currentPrice = nbt.getFloat("value");
	}
}
