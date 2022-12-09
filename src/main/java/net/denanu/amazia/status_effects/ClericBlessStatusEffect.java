package net.denanu.amazia.status_effects;

import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ClericBlessStatusEffect extends StatusEffect {
	public ClericBlessStatusEffect() {
		super(StatusEffectCategory.BENEFICIAL, 0xffd500);
	}

	@Override
	public boolean canApplyUpdateEffect(final int duration, final int amplifier) {
		return false;
	}

	@Override
	public void onRemoved(final LivingEntity entity, final AttributeContainer attributes, final int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		this.setLevelBoost(entity, amplifier);
	}

	@Override
	public void onApplied(final LivingEntity entity, final AttributeContainer attributes, final int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		this.setLevelBoost(entity, amplifier);
	}

	private void setLevelBoost(final LivingEntity entity, final int value) {
		if (entity instanceof final IAmaziaLevelProviderEntity leveler) {
			leveler.setLevelBoost(value);
		}
	}
}
