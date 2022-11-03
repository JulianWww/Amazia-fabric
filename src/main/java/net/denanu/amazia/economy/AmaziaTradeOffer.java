package net.denanu.amazia.economy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.economy.offerModifiers.finalizers.OfferFinalModifer;
import net.denanu.amazia.exceptions.EconomyMissingModifierEconomyException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

public class AmaziaTradeOffer {
	public ItemStack item;
	ItemStack seller;
	public float value;
	public float modifiedValue;
	boolean buy, dirty, valid;
	public int offerId;
	private String key;
	
	public List<String> priceModifiers;
	public List<OfferFinalModifer> finalizers;

	public AmaziaTradeOffer(ItemStack item, float value, boolean buy) {
		this(item, value, buy, true);
	}
	public AmaziaTradeOffer(ItemStack item, float value, boolean buy, boolean valid) {
		this.item = item;
		this.value = value;
		this.buy = buy;
		this.dirty = true;
		
		this.modifiedValue = this.value;
		this.valid = valid;
		this.key = item.getTranslationKey();
		
		this.priceModifiers = new LinkedList<String>();
		this.finalizers = new ArrayList<OfferFinalModifer>();
	}

	public AmaziaTradeOffer(NbtCompound nbt) {
		this.item = ItemStack.fromNbt(nbt.getCompound("id"));
		this.value = nbt.getFloat("value");
		this.modifiedValue = value;
		this.buy = nbt.getBoolean("buy");
		this.key = nbt.getString("economyKey");
		
		this.fromModifierNbt ((NbtList) nbt.get("modifiers"));
		this.fromNbtFinalzers((NbtList) nbt.get("finalizers"));
	}
	
	public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
  
        nbt.put("id",  this.item.writeNbt(new NbtCompound()));
        nbt.putFloat("value", this.value);
        nbt.putBoolean("buy", this.buy);
        nbt.put("modifiers", this.toModifierNbt());
        nbt.put("finalizers", this.toFinalizerNbt());
        nbt.putString("economyKey", key);
        return nbt;
    }
	
	private NbtElement toModifierNbt() {
		NbtList arr = new NbtList();
		for (String mod : this.priceModifiers) {
			arr.add(NbtString.of(mod));
		}
		return arr;
	}
	
	private void fromModifierNbt(NbtList list) {
		this.priceModifiers = new LinkedList<String>();
		for (NbtElement val : list) {
			this.priceModifiers.add(((NbtString)val).asString());
		}
		return;
	}
	
	private NbtList toFinalizerNbt() {
		NbtList nbt = new NbtList();
		for (OfferFinalModifer mod : this.finalizers) {
			nbt.add(NbtString.of(mod.ident.toString()));
		}
		return nbt;
	}
	
	private void fromNbtFinalzers(NbtList list) {
		this.finalizers = new ArrayList<OfferFinalModifer>(list != null ? list.size() : 0);
		for (NbtElement val : list) {
			this.finalizers.add(Economy.getFinalizer(
					new Identifier(((NbtString)val).asString())
				));
		}
	}

	public void setDirty() {
		this.dirty = true;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public void setPrice(float value) {
		if (this.value != value) {
			this.dirty = true;
		}
		this.value = value;
		this.modifyPrice();
	}
	public boolean isBuy() {
		return this.buy;
	}
	
	public int getQuantity() {
		return this.item.getCount();
	}

	public boolean isDisabled() {
		return !this.valid;
	}
	
	private int getComplexPrice(int v) {
		return this.buy ? Math.floorDiv(v, 9) : Math.floorDiv(v, 9) + 1;
	}
	
	private ItemStack buildSeller() {
		int v = (int) (this.modifiedValue * this.item.getCount());
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
		this.modifyPrice();
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
	
	public AmaziaTradeOffer build() {
		this.modifyPrice();
		return this;
	}
	
	private void modifyPrice() {
		this.modifiedValue = this.value;
		for (String mod : this.priceModifiers) {
			ModifierEconomy modifier = Economy.getModifierEconomy(mod);
			if (modifier != null) {
				modifier.modifiy(this);
			}
			else {
				throw new EconomyMissingModifierEconomyException("Missing trade modifer price modifer of key: " + mod + "\nadd with AmaziaValueModifer::register");
			}
		}
		return;
	}

	public void updateModifiers(LivingEntity merchant) {
		for (String mod : this.priceModifiers) {
			ModifierEconomy modifier = Economy.getModifierEconomy(mod);
			if (modifier != null) {
				modifier.update(this.getQuantity(), this.isBuy());;
			}
		}
	}
	
	public void finalze(LivingEntity merchant) {
		this.valid = true;
		for (OfferFinalModifer mod : this.finalizers) {
			mod.modify(this, merchant);
		}
	}

	public void disable() {
		this.valid = false;
	}

	public AmaziaTradeOffer setFinalizers(ArrayList<OfferFinalModifer> finals) {
		this.finalizers = finals;
		return this;
	}
	public void setIsBuy(boolean b) {
		this.buy = b;		
	}
}
