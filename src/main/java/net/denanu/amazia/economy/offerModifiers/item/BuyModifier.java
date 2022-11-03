package net.denanu.amazia.economy.offerModifiers.item;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;

public class BuyModifier implements OfferModifier {
	private final boolean value;
	
	public BuyModifier(boolean value) {
		this.value = value;
	}
	
	@Override
	public void modify(AmaziaTradeOffer offer) {
		offer.setIsBuy(value);
	}

}
