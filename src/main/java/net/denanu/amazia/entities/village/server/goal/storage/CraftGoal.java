package net.denanu.amazia.entities.village.server.goal.storage;

import java.util.Map.Entry;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftGoal<E extends AmaziaVillagerEntity> extends TimedVillageGoal<E> {
	public CraftGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return this.entity.getCanUpdate() && super.canStart() && this.entity.wantsToCraft() && CraftGoal.canCraft(this.entity);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getCraftingTime();
	}

	@Override
	protected void takeAction() {
		this.takeCraftAction();
		this.entity.endCraft();
	}

	public void takeCraftAction() {
		if (this.entity.wantsToCraft() && CraftGoal.canCraft(this.entity)) {
			this.craft();
		}
	}

	protected void craft() {
		for (final Entry<Item, Integer> ingredient : this.entity.getCraftInput().entrySet()) {
			this.entity.removeItemFromInventory(ingredient.getKey(), ingredient.getValue());
		}
		ItemStack outStack = this.entity.getWantsToCraft().getOutput().copy();
		ActivityFoodConsumerMap.craftItemUseFood(this.entity, outStack.getItem());
		HappynessMap.looseCraftHappyness(this.entity);
		outStack = this.entity.getInventory().addStack(outStack);
		this.entity.dropStack(outStack);
	}

	public static<E extends AmaziaVillagerEntity> boolean canCraft(final E entity) {
		for (final Entry<Item, Integer> ingredient : entity.getCraftInput().entrySet()) {
			if (!entity.hasItem(ingredient.getKey(), ingredient.getValue())) {
				if (!entity.hasRequestedItems()) {
					entity.endCraft();
				}
				return false;
			}
		}
		return true;
	}
}
