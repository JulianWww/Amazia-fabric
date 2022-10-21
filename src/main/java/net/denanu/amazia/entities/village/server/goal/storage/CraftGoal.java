package net.denanu.amazia.entities.village.server.goal.storage;

import java.util.Map.Entry;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftGoal extends TimedVillageGoal<AmaziaVillagerEntity> {
	public CraftGoal(AmaziaVillagerEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.wantsToCraft() && this.canCraft();
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getCraftingTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.wantsToCraft() && this.canCraft()) {
			this.craft();
		}
	}
	
	protected void craft() {
		for (Entry<Item, Integer> ingredient : this.entity.getCraftInput().entrySet()) {
			this.entity.removeItemFromInventory(ingredient.getKey(), ingredient.getValue());
		}
		ItemStack outStack = this.entity.getWantsToCraft().getOutput().copy();
		outStack = this.entity.getInventory().addStack(outStack);
		this.entity.dropStack(outStack);
		this.entity.endCraft();
	}
	
	protected boolean canCraft() {
		for (Entry<Item, Integer> ingredient : this.entity.getCraftInput().entrySet()) {
			if (!this.entity.hasItem(ingredient.getKey(), ingredient.getValue())) {
				return false;
			}
		}
		return true;
	}
}
