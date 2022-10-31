package net.denanu.amazia.economy.offerModifiers.item;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.economy.offerModifiers.price.AmaziaValueModifiers;
import net.denanu.amazia.utils.random.RandomCollection;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentModifier implements OfferModifier {
	private RandomCollection<Enchantment> possibleEnchants;
	
	
	public EnchantmentModifier() {
		this.possibleEnchants = new RandomCollection<Enchantment>();
	}
	
	public EnchantmentModifier add(Enchantment enchant, float weight) {
		this.possibleEnchants.add(weight, enchant);
		return this;
	}

	@Override
	public void modify(AmaziaTradeOffer offer) {
		if (!offer.isBuy()) {
			int enchantCount = this.getEnchantCount();
			for (int i=0; i<enchantCount; i++) {
				this.enchant(offer);
			}
		}
	}
	
	private void enchant(AmaziaTradeOffer offer) {
		Enchantment enchant = this.possibleEnchants.next();
		offer.item.addEnchantment(enchant, this.getLvl(enchant));
		
		regiserPriceMod(offer, AmaziaValueModifiers.PROTECTION);
	}
	
	private int getLvl(Enchantment enchant) {
		return 1;
	}
	
	private int getEnchantCount() {
		return 1;
	}
}
