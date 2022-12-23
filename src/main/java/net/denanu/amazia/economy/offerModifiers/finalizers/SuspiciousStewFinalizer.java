package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.util.Identifier;

public class SuspiciousStewFinalizer extends OfferFinalModifer {
	private WeightedRandomCollection<StatusEffect> effects;
	private RandomnessFactory<Integer> durationFactory;

	public SuspiciousStewFinalizer(Identifier ident, RandomnessFactory<Integer> durationFactory) {
		super(ident);
		this.effects = new WeightedRandomCollection<StatusEffect>();
		this.durationFactory = durationFactory;
	}
	
	public SuspiciousStewFinalizer add(StatusEffect effect, float weight) {
		this.effects.add(weight, effect);
		return this;
	}

	@Override
	public void modify(AmaziaTradeOffer offer, LivingEntity merchant) {
		offer.setIsBuy(false);
		SuspiciousStewItem.addEffectToStew(offer.item, this.effects.next(), this.durationFactory.next());
		return;
	}

}
