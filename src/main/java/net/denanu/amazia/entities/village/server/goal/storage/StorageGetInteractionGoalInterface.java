package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface StorageGetInteractionGoalInterface {
	public void StorageInteractionDone();
	public DoubleDownPathingData getTarget();
	public Item getItem();
}
