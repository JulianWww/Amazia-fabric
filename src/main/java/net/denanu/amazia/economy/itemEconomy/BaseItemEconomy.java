package net.denanu.amazia.economy.itemEconomy;

import java.util.ArrayList;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.economy.offerModifiers.finalizers.OfferFinalModifer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public abstract class BaseItemEconomy {
	protected final Item itm;
	protected final ArrayList<OfferModifier> modifiers;
	private int count;
	private final ArrayList<OfferFinalModifer> finalizers;

	public BaseItemEconomy(final Item itm) {
		this.itm = itm;
		this.modifiers = new ArrayList<>();
		this.finalizers = new ArrayList<>();
		this.count = 1;
	}

	public abstract void fromNbt(NbtCompound compound);

	public abstract NbtElement toNbt();

	public abstract void reset();

	public abstract void update();

	public abstract float getPriceDelta(float quantity, boolean buy);

	public abstract float getCurrentPrice();

	public abstract void setCurrentValue(float float1);

	public abstract void updatePrice(float quantity, boolean buy);

	protected abstract ItemStack getStack();

	public String getName() { return this.itm.getTranslationKey(); }

	public BaseItemEconomy modify(final OfferModifier mod) {
		this.modifiers.add(mod);
		return this;
	}

	public BaseItemEconomy finalize(final OfferFinalModifer mod) {
		this.finalizers.add(mod);
		return this;
	}

	public AmaziaTradeOffer build(final IAmaziaMerchant merchant) {
		this.finalizers.trimToSize();
		this.modifiers.trimToSize();
		return new AmaziaTradeOffer(this.getStack(), this.getCurrentPrice(), JJUtils.rand.nextBoolean()).modify(this.modifiers).setFinalizers(this.finalizers).build();
	}

	public boolean hasNbt() {
		return true;
	}

	public BaseItemEconomy count(final int i) {
		this.count = i;
		return this;
	}

	public int getCount() {
		return this.count;
	}

	public BaseItemEconomy addToVillage() {
		Economy.addVillagerItem(this.getName());
		return this;
	}
}
