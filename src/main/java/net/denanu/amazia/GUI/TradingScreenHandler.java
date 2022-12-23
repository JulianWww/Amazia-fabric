package net.denanu.amazia.GUI;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.denanu.amazia.entities.merchants.AmaziaMerchantInventory;
import net.denanu.amazia.entities.merchants.AmaziaTradeOutputSlot;
import net.denanu.amazia.entities.merchants.SimpleAmaziaMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class TradingScreenHandler extends ScreenHandler {
	private IAmaziaMerchant merchant;
	private AmaziaMerchantInventory inventory; 
	
	public TradingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleAmaziaMerchant(playerInventory.player));
    }
	
    public TradingScreenHandler(int syncId, PlayerInventory playerInventory, IAmaziaMerchant merchant) {
    	super(AmaziaScreens.TRADING_SCREEN_HANDLER, syncId);
    	this.merchant = merchant;
    	this.inventory = new AmaziaMerchantInventory(this.merchant);
    	
    	this.addSlot(new Slot(this.inventory, 0, 136, 37));
        this.addSlot(new Slot(this.inventory, 1, 162, 37));
        this.addSlot(new AmaziaTradeOutputSlot(playerInventory.player, merchant, this.inventory, 2, 220, 37));
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 108 + i * 18, 142));
        }
    }

	@Override
	public boolean canUse(PlayerEntity var1) {
		return true;
	}
	
	@Override
    public void onContentChanged(Inventory inventory) {
        this.inventory.updateOffers();
        super.onContentChanged(inventory);
    }

	public AmaziaTradeOfferList getOffers() {
		return this.merchant.getOffers();
	}
	
	public void setOffers(AmaziaTradeOfferList offers) {
		this.merchant.setOffersFromServer(offers);
	}
	
	@Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.merchant.setCustomer(null);
        if (this.merchant.isClient()) {
            return;
        }
        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity)player).isDisconnected()) {
            ItemStack itemStack = this.inventory.removeStack(0);
            if (!itemStack.isEmpty()) {
                player.dropItem(itemStack, false);
            }
            if (!(itemStack = this.inventory.removeStack(1)).isEmpty()) {
                player.dropItem(itemStack, false);
            }
        } else if (player instanceof ServerPlayerEntity) {
            player.getInventory().offerOrDrop(this.inventory.removeStack(0));
            player.getInventory().offerOrDrop(this.inventory.removeStack(1));
        }
    }

	public void setRecipeIndex(int index) {
        this.inventory.setOfferIndex(index);
    }
	
	@Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index == 0 || index == 1 ? !this.insertItem(itemStack2, 3, 39, false) : (index >= 3 && index < 30 ? !this.insertItem(itemStack2, 30, 39, false) : index >= 30 && index < 39 && !this.insertItem(itemStack2, 3, 30, false))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

	public void switchTo(int recipeIndex) {
        ItemStack itemStack2;
        if (this.getRecipes().size() <= recipeIndex) {
            return;
        }
        ItemStack itemStack = this.inventory.getStack(0);
        if (!itemStack.isEmpty()) {
            if (!this.insertItem(itemStack, 3, 39, true)) {
                return;
            }
            this.inventory.setStack(0, itemStack);
        }
        if (!(itemStack2 = this.inventory.getStack(1)).isEmpty()) {
            if (!this.insertItem(itemStack2, 3, 39, true)) {
                return;
            }
            this.inventory.setStack(1, itemStack2);
        }
        if (this.inventory.getStack(0).isEmpty() && this.inventory.getStack(1).isEmpty()) {
            ItemStack itemStack3 = ((AmaziaTradeOffer)this.getRecipes().get(recipeIndex)).getFistBuyItem();
            this.autofill(0, itemStack3);
            ItemStack itemStack4 = ((AmaziaTradeOffer)this.getRecipes().get(recipeIndex)).getSecondBuyItem();
            this.autofill(1, itemStack4);
        }
    }

	private void autofill(int slot, ItemStack stack) {
        if (!stack.isEmpty()) {
            for (int i = 3; i < 39; ++i) {
                ItemStack itemStack = ((Slot)this.slots.get(i)).getStack();
                if (itemStack.isEmpty() || !ItemStack.canCombine(stack, itemStack)) continue;
                ItemStack itemStack2 = this.inventory.getStack(slot);
                int j = itemStack2.isEmpty() ? 0 : itemStack2.getCount();
                int k = Math.min(stack.getMaxCount() - j, itemStack.getCount());
                ItemStack itemStack3 = itemStack.copy();
                int l = j + k;
                itemStack.decrement(k);
                itemStack3.setCount(l);
                this.inventory.setStack(slot, itemStack3);
                if (l >= stack.getMaxCount()) break;
            }
        }
    }

	public int getLevelProgress() {
		return 0;
	}

	public AmaziaTradeOfferList getRecipes() {
		return this.getOffers();
	}

	public int getExperience() {
		return 0;
	}

	public int getMerchantRewardedExperience() {
		return 0;
	}

	public boolean isLeveled() {
		return false;
	}

	public boolean canRefreshTrades() {
		return false;
	}
	
	public void updateCurrentTrade() {
		this.inventory.updateOffers();
	}
}
