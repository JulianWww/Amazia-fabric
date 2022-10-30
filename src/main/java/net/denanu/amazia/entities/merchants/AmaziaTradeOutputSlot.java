/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.entities.merchants;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.stat.Stats;

public class AmaziaTradeOutputSlot
extends Slot {
    private final AmaziaMerchantInventory merchantInventory;
    private final PlayerEntity player;
    private int amount;
    private final IAmaziaMerchant merchant;

    public AmaziaTradeOutputSlot(PlayerEntity player, IAmaziaMerchant merchant, AmaziaMerchantInventory merchantInventory, int index, int x, int y) {
        super(merchantInventory, index, x, y);
        this.player = player;
        this.merchant = merchant;
        this.merchantInventory = merchantInventory;
    }
    
    @Override
    public void setStack(ItemStack stack) {
    	super.setStack(stack);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }
        return super.takeStack(amount);
    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        stack.onCraft(this.player.world, this.player, this.amount);
        this.amount = 0;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        AmaziaTradeOffer tradeOffer = this.merchantInventory.getTradeOffer();
        if (tradeOffer != null) {
            ItemStack itemStack2;
            ItemStack itemStack = this.merchantInventory.getStack(0);
            if (tradeOffer.depleteBuyItems(itemStack, itemStack2 = this.merchantInventory.getStack(1)) || tradeOffer.depleteBuyItems(itemStack2, itemStack)) {
                this.merchant.trade(tradeOffer);
                player.incrementStat(Stats.TRADED_WITH_VILLAGER);
                this.merchantInventory.setStack(0, itemStack);
                this.merchantInventory.setStack(1, itemStack2);
            }
        }
    }
}

