package net.denanu.amazia.economy.offerModifiers.price;

import net.denanu.amazia.economy.itemEconomy.ItemEconomy;

public class DeficiteModifierEconomy extends SimpleModifierEconomy {

	public DeficiteModifierEconomy(float price, float volatility, float returnRate) {
		super(-price, volatility, returnRate);
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
