package net.denanu.amazia.economy.offerModifiers.item;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemReselector implements OfferModifier {
	private WeightedRandomCollection<Item> items;
	
	public ItemReselector() {
		this.items = new WeightedRandomCollection<Item>();
	}
	
	public ItemReselector add(Item itm, float weight) {
		this.items.add((float) weight, itm);
		return this;
	}

	@Override
	public void modify(AmaziaTradeOffer offer) {
		offer.item = new ItemStack(this.items.next(), offer.item.getCount());
	}
}
