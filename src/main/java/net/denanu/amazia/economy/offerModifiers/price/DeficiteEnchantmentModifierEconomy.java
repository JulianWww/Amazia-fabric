package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.economy.itemEconomy.ItemEconomy;
import net.minecraft.enchantment.Enchantment;

public class DeficiteEnchantmentModifierEconomy extends EnchantmentValueModifier {

	public DeficiteEnchantmentModifierEconomy(Enchantment enchant, float price, float volatility, float returnRate) {
		super(-price, volatility, returnRate, enchant);
	}

	@Override
	protected void constrain() {
		if (this.currentPrice > 0) {
			this.currentPrice = 0;
		}
		if (this.currentPrice < -ItemEconomy.MAX_VALUE) {
			this.currentPrice = -ItemEconomy.MAX_VALUE;
		}
	}

}
