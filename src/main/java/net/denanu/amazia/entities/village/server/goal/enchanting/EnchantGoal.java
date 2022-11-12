package net.denanu.amazia.entities.village.server.goal.enchanting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.denanu.amazia.entities.village.server.EnchanterEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.entities.village.server.goal.enchanting.utils.AmaziaEnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class EnchantGoal extends TimedVillageGoal<EnchanterEntity> {

	public EnchantGoal(EnchanterEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return !this.entity.canDepositItems() && super.canStart() && this.entity.hasEnchantItem() && this.entity.getTargetPos() != null && this.entity.getBlockPos().getManhattanDistance(this.entity.getTargetPos()) <= 1;
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void stop() {
		super.stop();
		this.entity.setTargetPos(null);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getEnchantTime();
	}

	@Override
	protected void takeAction() {
		Optional<Integer> itmKey = this.entity.getEnchantableItem();
		if (itmKey.isPresent()) {
			ItemStack stack = this.entity.getInventory().getStack(itmKey.get());
			List<EnchantmentLevelEntry> enchantments = AmaziaEnchantmentHelper.generateEnchantments(this.entity.getRandom(), stack, this.entity.getEnchantinAbility(), true);
			
			if (!enchantments.isEmpty()) {
				AmaziaEnchantmentHelper.enchant(stack, enchantments);
				this.entity.emmitMood(VillagerMoods.HAPPY);
				this.entity.getInventory().setStack(itmKey.get(), stack);
			}
			else {
				this.entity.emmitMood(VillagerMoods.ANGRY);
			}
			this.entity.returnItem();
			this.entity.looseEnchantItem();
		}
	}
}
