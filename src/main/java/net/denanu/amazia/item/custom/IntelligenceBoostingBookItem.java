package net.denanu.amazia.item.custom;

import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class IntelligenceBoostingBookItem extends Item {

	public IntelligenceBoostingBookItem(final Settings settings) {
		super(settings);
	}

	@Override
    public boolean hasGlint(final ItemStack stack) {
        return true;
    }

	@Override
	public ActionResult useOnEntity(final ItemStack stack, final PlayerEntity user, final LivingEntity entity, final Hand hand) {
		if (entity instanceof IAmaziaIntelligenceEntity smart_entity) {
			if (user.world.isClient && smart_entity.getIntelligence() < 100f) {
				return ActionResult.SUCCESS;
			}
			if (smart_entity.modifyIntelligence(1f)) {
				stack.decrement(1);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
    }
}