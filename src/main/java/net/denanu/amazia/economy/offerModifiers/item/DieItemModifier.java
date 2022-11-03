package net.denanu.amazia.economy.offerModifiers.item;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.offerModifiers.OfferModifier;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.DyeColor;

public class DieItemModifier implements OfferModifier {

	@Override
	public void modify(AmaziaTradeOffer offer) {
		if (!offer.isBuy() && JJUtils.rand.nextInt(10) > 1 && offer.item.getItem() instanceof DyeableArmorItem) {
			ArrayList<DyeItem> list = Lists.newArrayList();
			list.add(DieItemModifier.getDye(JJUtils.rand));
			if (JJUtils.rand.nextFloat() > 0.7f) {
				list.add(DieItemModifier.getDye(JJUtils.rand));
			}
			if (JJUtils.rand.nextFloat() > 0.8f) {
				list.add(DieItemModifier.getDye(JJUtils.rand));
			}
			offer.item = DyeableItem.blendAndSetColor(offer.item, list);
		}
     }

	 private static DyeItem getDye(Random rand) {
         return DyeItem.byColor(DyeColor.byId(rand.nextInt(16)));
     }
}
