package net.denanu.amazia.economy.offerModifiers.item;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.denanu.amazia.economy.offerModifiers.price.AmaziaValueModifiers;
import net.denanu.amazia.utils.random.RandomCollection;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtElement;

public class EnchantmentModifier implements OfferModifier {
	private RandomCollection<Enchantment> possibleEnchants;
	private final float reselctionProbability;
	private final float initalProbability;
	
	
	public EnchantmentModifier(final float reselectionProbability, final float initalProbability) {
		this.possibleEnchants = new RandomCollection<Enchantment>();
		this.reselctionProbability = reselectionProbability;
		this.initalProbability = initalProbability;
	}
	
	private boolean canReselect() {
		return JJUtils.rand.nextFloat() <= this.reselctionProbability;
	}
	
	private boolean canStart() {
		return JJUtils.rand.nextFloat() <= this.initalProbability;
	}
	
	public EnchantmentModifier add(Enchantment enchant, float weight) {
		this.possibleEnchants.add(weight, enchant);
		return this;
	}

	@Override
	public void modify(AmaziaTradeOffer offer) {
		if (!offer.isBuy() && this.canStart()) {
			do {
				this.enchant(offer);
			} while (this.canReselect());
		}
	}
	
	private static boolean canPutOn(AmaziaTradeOffer offer, Enchantment enchant) {
		for (Enchantment applied : EnchantmentHelper.get(offer.item).keySet()) {
			if (!applied.canCombine(enchant)) {
				return false;
			}
		}
		return true;
	}
	
	private void enchant(AmaziaTradeOffer offer) {
		Enchantment enchant = this.possibleEnchants.next();
		
		if (canPutOn(offer, enchant)) {
			offer.item.addEnchantment(enchant, this.getLvl(enchant));
			
			regiserPriceMod(offer, enchant.getTranslationKey());
		}
	}
	
	private int getLvl(Enchantment enchant) {
		return enchant.getMaxLevel() > 1 ? JJUtils.rand.nextInt(1, enchant.getMaxLevel()) : 1;
	}
}
