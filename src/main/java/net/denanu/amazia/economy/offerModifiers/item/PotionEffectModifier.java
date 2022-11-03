package net.denanu.amazia.economy.offerModifiers.item;

import java.util.List;
import java.util.stream.Collectors;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.registry.Registry;

public class PotionEffectModifier implements OfferModifier {

	@Override
	public void modify(AmaziaTradeOffer offer) {
		List<Potion> list = Registry.POTION.stream().filter(potion -> !potion.getEffects().isEmpty() && BrewingRecipeRegistry.isBrewable(potion)).collect(Collectors.toList());
        Potion potion2 = (Potion)list.get(JJUtils.rand.nextInt(list.size()));
        PotionUtil.setPotion(offer.item, potion2);
	}

}
