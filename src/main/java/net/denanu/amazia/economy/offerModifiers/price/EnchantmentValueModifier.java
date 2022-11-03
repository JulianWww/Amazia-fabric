package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;

public abstract class EnchantmentValueModifier extends SimpleModifierEconomy {
	protected Enchantment enchant;

	public EnchantmentValueModifier(float price, float volatility, float returnRate, Enchantment enchant) {
		super(price, volatility, returnRate);
		this.enchant = enchant;
	}
	
	@Override
	public void modifiy(AmaziaTradeOffer offer) {
		offer.modifiedValue = (float) (offer.modifiedValue + this.currentPrice * Math.pow(2, this.getEnchantLvl(offer)));
	}
	
	protected int getEnchantLvl(AmaziaTradeOffer offer) {
		return EnchantmentHelper.getLevel(this.enchant, offer.item)-1;
		
	}

	public Enchantment getEnchant() {
		return this.enchant;
	}

}
