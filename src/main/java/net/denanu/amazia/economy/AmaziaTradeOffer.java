package net.denanu.amazia.economy;

import java.util.ArrayList;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

public class AmaziaTradeOffer {
	public ItemStack item;
	ItemStack seller;
	float value;
	boolean buy, dirty;
	public int offerId;

	public AmaziaTradeOffer(ItemStack item, float value, boolean buy) {
		this.item = item;
		this.value = value;
		this.buy = buy;
		this.dirty = true;
	}

	public AmaziaTradeOffer(NbtCompound nbt) {
		this.item = ItemStack.fromNbt(nbt.getCompound("id"));
		this.value = nbt.getFloat("value");
		this.buy = nbt.getBoolean("buy");
	}
	
	public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
  
        nbt.put("id",  this.item.writeNbt(new NbtCompound()));
        nbt.putFloat("value", this.value);
        nbt.putBoolean("buy", this.buy);
        return nbt;
    }
	
	public void setDirty() {
		this.dirty = true;
	}
	
	public String getKey() {
		return this.item.getTranslationKey();
	}
	
	public void setPrice(float value) {
		if (this.value != value) {
			this.dirty = true;
		}
		this.value = value;
	}
	public boolean isBuy() {
		return this.buy;
	}
	
	public int getQuantity() {
		return this.item.getCount();
	}

	public boolean isDisabled() {
		return false;
	}
	
	private int getComplexPrice(int v) {
		return this.buy ? Math.floorDiv(v, 9) : Math.floorDiv(v, 9) + 1;
	}
	
	private ItemStack buildSeller() {
		int v = (int) (this.value * this.item.getCount());
		if (v <= 64) {
			return new ItemStack(Items.EMERALD, Math.max(1, v));
		}
		else if (v <= 576) {
			return new ItemStack(Items.EMERALD_BLOCK, this.getComplexPrice(v));
		}
		return new ItemStack(Items.EMERALD_BLOCK, 64);
	}
	
	private ItemStack buildMoneyItem() {
		if (this.seller == null || this.dirty) {
			this.dirty = false;
			this.seller = this.buildSeller();
		}
		return seller;
	}

	public ItemStack copySellItem() {
		return this.getSellItem().copy();
	}
	public ItemStack getSellItem() {
		return (this.buy ? this.buildMoneyItem() : this.item).copy();
	}

	public int getMerchantExperience() {
		return (int) (JJUtils.rand.nextGaussian(5f, 1f));
	}

	public boolean matchesBuyItems(ItemStack firstBuyItem, ItemStack secondBuyItem) {
		return (firstBuyItem.isOf(this.getFistBuyItem().getItem()) && firstBuyItem.getCount() >= this.getFistBuyItem().getCount());
	}

	public boolean depleteBuyItems(ItemStack firstBuyStack, ItemStack secondBuyStack) {
		if (!this.matchesBuyItems(firstBuyStack, secondBuyStack)) {
            return false;
        }
        firstBuyStack.decrement(this.getFistBuyItem().getCount());
        return true;
	}

	public void use() {
	}

	public ItemStack getFistBuyItem() {
		return this.buy ? this.item : this.buildMoneyItem();
	}

	public ItemStack getSecondBuyItem() {
		return ItemStack.EMPTY;
	}

	private void changePrice(float delta) {
		this.value = this.value + delta;
		this.dirty = true;
	}
	
	public void updatePrice() {
		this.changePrice(Amazia.economy.getItem(this.getKey()).getPriceDelta(this.getQuantity(), this.isBuy()));
	}

	public AmaziaTradeOffer modify(ArrayList<OfferModifier> modifiers) {
		for (OfferModifier modifier : modifiers) {
			modifier.modify(this);
		}
		return this;
	}
}
