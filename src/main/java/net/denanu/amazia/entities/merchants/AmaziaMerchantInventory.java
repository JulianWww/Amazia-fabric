/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.entities.merchants;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class AmaziaMerchantInventory
implements Inventory {
    private final IAmaziaMerchant merchant;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private int offerIndex, currentOfferIdx;
    private int merchantRewardedExperience;

    public AmaziaMerchantInventory(IAmaziaMerchant merchant) {
        this.merchant = merchant;
        this.currentOfferIdx = -1;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = this.inventory.get(slot);
        if (slot == 2 && !itemStack.isEmpty()) {
            return Inventories.splitStack(this.inventory, slot, itemStack.getCount());
        }
        ItemStack itemStack2 = Inventories.splitStack(this.inventory, slot, amount);
        if (!itemStack2.isEmpty() && this.needsOfferUpdate(slot)) {
            this.updateOffers();
        }
        return itemStack2;
    }

    private boolean needsOfferUpdate(int slot) {
        return slot == 0 || slot == 1;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        if (this.needsOfferUpdate(slot)) {
            this.updateOffers();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.merchant.getCustomer() == player;
    }

    @Override
    public void markDirty() {
        this.updateOffers();
    }

    public void updateOffers() {
        ItemStack itemStack2;
        ItemStack itemStack;
        this.currentOfferIdx = (-1);
        if (this.inventory.get(0).isEmpty()) {
            itemStack = this.inventory.get(1);
            itemStack2 = ItemStack.EMPTY;
        } else {
            itemStack = this.inventory.get(0);
            itemStack2 = this.inventory.get(1);
        }
        if (itemStack.isEmpty()) {
            this.setStack(2, ItemStack.EMPTY);
            this.merchantRewardedExperience = 0;
            return;
        }
        AmaziaTradeOfferList tradeOfferList = this.merchant.getOffers();
        if (!tradeOfferList.isEmpty()) {
            AmaziaTradeOffer tradeOffer = tradeOfferList.getValidOffer(itemStack, itemStack2, this.offerIndex);
            if (tradeOffer == null || tradeOffer.isDisabled()) {
                this.setOffer(tradeOffer);
                tradeOffer = tradeOfferList.getValidOffer(itemStack2, itemStack, this.offerIndex);
            }
            if (tradeOffer != null && !tradeOffer.isDisabled()) {
                this.setOffer(tradeOffer);
                this.setStack(2, tradeOffer.copySellItem());
                this.merchantRewardedExperience = tradeOffer.getMerchantExperience();
            } else {
                this.setStack(2, ItemStack.EMPTY);
                this.merchantRewardedExperience = 0;
            }
        }
        this.merchant.onSellingItem(this.getStack(2));
    }

    @Nullable
    public AmaziaTradeOffer getTradeOffer() {
    	if (this.currentOfferIdx < 0) {
    		return null;
    	}
        return this.merchant.getOffers().get(offerIndex);
    }
    
    private void setOffer(AmaziaTradeOffer offer) {
    	if (offer == null) {
    		this.currentOfferIdx = -1;
    	}
    	else {
    		this.currentOfferIdx = offer.offerId;
    	}
    }

    public void setOfferIndex(int index) {
        this.offerIndex = index;
        this.updateOffers();
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    public int getMerchantRewardedExperience() {
        return this.merchantRewardedExperience;
    }
}

