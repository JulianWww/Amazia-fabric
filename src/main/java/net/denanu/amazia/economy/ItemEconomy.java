package net.denanu.amazia.economy;

import java.util.ArrayList;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class ItemEconomy {
	public static final int MAX_VALUE = 576;
	
	private final Item itemStack;
	private final float baseValue;
	private final float volatility;
	private final float returnRate;
	private final RandomnessFactory<Integer> stackSizeFactory;
	private final ArrayList<OfferModifier> modifiers;
	
	private float currentPrice;
	
	public ItemEconomy(Item stack, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory, ArrayList<OfferModifier> modifiers) {
		this.itemStack = stack;
		this.baseValue = baseValue;
		this.volatility = volatility;
		this.returnRate = returnRate;
		this.stackSizeFactory = stackSizeFactory;
		
		this.modifiers = modifiers;
		
		this.reset();
	}
	
	private final ItemStack getStack() {
		return new ItemStack(this.itemStack, this.stackSizeFactory.next());
	}
	
	public float getCurrentPrice() {
		return this.currentPrice;
	}
	
	public void update() {
		this.currentPrice = this.currentPrice - (this.currentPrice - this.baseValue) * this.returnRate;
	}
	
	public void updatePrice(int quantity, boolean buy) {
		this.currentPrice = this.currentPrice + this.getPriceDelta(quantity, buy);
		if (this.currentPrice < 0) { this.currentPrice = 0; }
		if (this.currentPrice > MAX_VALUE) { this.currentPrice = MAX_VALUE; }
	}
	
	public float getPriceDelta(int quantity, boolean buy) {
		return buy ? -this.volatility * quantity : this.volatility * quantity;
	}
	
	
	public void reset() {
		this.currentPrice = this.baseValue;
	}

	public String getName() {
		return this.itemStack.getTranslationKey();
	}
	
	public AmaziaTradeOffer build(IAmaziaMerchant merchant) {
		return new AmaziaTradeOffer(this.getStack(), this.getCurrentPrice(), JJUtils.rand.nextBoolean()).modify(this.modifiers);
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
