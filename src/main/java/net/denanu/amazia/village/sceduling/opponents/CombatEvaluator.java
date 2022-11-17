package net.denanu.amazia.village.sceduling.opponents;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class CombatEvaluator {
	public static float getWeaponPriority(final ItemStack item, final LivingEntity target) {
		return EnchantmentHelper.getAttackDamage(item, target.getGroup()) + CombatEvaluator.getDamage(item);
	}

	private static float getDamage(final ItemStack item) {
		if (item.getItem() instanceof final SwordItem sword) {
			return sword.getAttackDamage();
		}
		return 0.0f;
	}

	public static boolean isBetter(final SwordItem old, final SwordItem next) {
		if (old == null) {
			return true;
		}
		return old.getAttackDamage() < next.getAttackDamage();
	}

	public static boolean isBetter(final ArmorItem old, final ArmorItem next) {
		if (old.getProtection() == next.getProtection()) {
			return old.getToughness() < next.getToughness();
		}
		return old.getProtection() < next.getProtection();
	}

	public static boolean isBetter(final Item old, final ArmorItem next) {
		if (old instanceof final ArmorItem oldArmor) {
			return CombatEvaluator.isBetter(oldArmor, next);
		}
		return true;
	}
}
