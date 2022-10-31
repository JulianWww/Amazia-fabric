package net.denanu.amazia.economy.offerModifiers;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.minecraft.util.Identifier;

public interface OfferModifier {
	public void modify(AmaziaTradeOffer offer);
	
	public default void regiserPriceMod(AmaziaTradeOffer offer, String modifier) {
		offer.priceModifiers.add(
				modifier
			);
	}
}
