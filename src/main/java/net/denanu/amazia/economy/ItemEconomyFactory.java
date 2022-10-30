package net.denanu.amazia.economy;

import java.util.ArrayList;

import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.item.Item;

public class ItemEconomyFactory {
	private final float basePride;
	private final float returnRate;
	private final float volatility;
	private final Item itm;
	private final RandomnessFactory<Integer> stackSizeGen;
	private final ArrayList<OfferModifier> modifiers;
	
	
	public ItemEconomyFactory(Item item, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory) {
		this.itm = item;
		this.basePride = baseValue;
		this.volatility = volatility;
		this.returnRate = returnRate;
		this.stackSizeGen = stackSizeFactory;
		
		this.modifiers = new ArrayList<OfferModifier>();
	}
	
	public ItemEconomyFactory modify(OfferModifier mod) {
		this.modifiers.add(mod);
		return this;
	}
	
	public ItemEconomy build() {
		return new ItemEconomy(this.itm, this.basePride, this.volatility, this.returnRate, this.stackSizeGen, this.modifiers);
	}

	public String getName() {
		return itm.getTranslationKey();
	}
}
