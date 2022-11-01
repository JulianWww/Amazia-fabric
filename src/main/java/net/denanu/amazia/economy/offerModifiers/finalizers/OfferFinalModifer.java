package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.Economy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class OfferFinalModifer {
	public Identifier ident;
	
	public OfferFinalModifer(Identifier ident) {
		Economy.registerFinalizer(ident, this);
		this.ident = ident;
	}
	
	public abstract void modify(AmaziaTradeOffer offer, LivingEntity merchant);
}
