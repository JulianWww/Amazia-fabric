package net.denanu.amazia.economy.offerModifiers.item;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;

public class BuyOnlyModifier implements OfferModifier {
	@Override
	public void modify(AmaziaTradeOffer offer) {
		offer.setIsBuy(false);
	}

}
