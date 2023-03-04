package net.denanu.amazia.advancement.criterion;

import net.minecraft.advancement.criterion.Criteria;

public class AmaziaCriterions {
	public static final GainTitleCriterion GAIN_TITLE = Criteria.register(new GainTitleCriterion());
	public static final GainAdvancementCriterion GAIN_ADVANCEMENT = Criteria.register(new GainAdvancementCriterion());

	public static void setup() {}
}
