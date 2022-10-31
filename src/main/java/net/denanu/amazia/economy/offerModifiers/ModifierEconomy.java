package net.denanu.amazia.economy.offerModifiers;

import net.denanu.amazia.economy.AmaziaTradeOffer;

public interface ModifierEconomy {
	public void modifiy(AmaziaTradeOffer offer);
	public void update(int quanity, boolean isBuy);
	public float getCurrentPrice();
}
