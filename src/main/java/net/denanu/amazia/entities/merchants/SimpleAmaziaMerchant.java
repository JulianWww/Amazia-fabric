package net.denanu.amazia.entities.merchants;

import java.util.Collection;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SimpleAmaziaMerchant implements IAmaziaMerchant {
	private final PlayerEntity player;
    private AmaziaTradeOfferList offers = new AmaziaTradeOfferList();
    
    public SimpleAmaziaMerchant(PlayerEntity player) {
    	this.player = player;
    }

	@Override
	public float getProfitMargin() {
		return 1.3f;
	}

	@Override
	public Collection<String> getTradePossibilities() {
		return null;
	}

	@Override
	public AmaziaTradeOfferList getOffers() {
		return this.offers;
	}

	@Override
	public void setOffersFromServer(AmaziaTradeOfferList offers) {
		this.offers = offers;
	}

	@Override
	public void setCustomer(PlayerEntity player) {
	}

	@Override
	public boolean isClient() {
		return this.player.world.isClient;
	}

	@Override
	public void onSellingItem(ItemStack stack) {
	}

	@Override
	public PlayerEntity getCustomer() {
		return this.player;
	}

	@Override
    public void trade(AmaziaTradeOffer offer) {
        offer.use();
    }
}
