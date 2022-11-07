package net.denanu.amazia.entities.village.server.goal.rancher;

import java.util.Optional;

import net.denanu.amazia.components.AmaziaComponents;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.utils.registry.AmaziaRegistrys;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class FeedAnimalGoal extends TimedVillageGoal<RancherEntity> {

	public FeedAnimalGoal(RancherEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return this.genreallShouldRun() && !this.getFeedItem().isEmpty();
	}
	
	@Override
	public boolean shouldContinue() {
		return super.shouldContinue();
	}
	
	private boolean genreallShouldRun() {
		return this.entity.hasTargetAnimal() && 
			super.canStart() && 
			AmaziaComponents.getIsPartOfVillage(this.entity.targetAnimal) && 
			this.entity.getPos().isInRange(this.entity.targetAnimal.getPos(), 2) &&
			super.canStart();
	}
	
	@Override
	public void stop() {
		super.stop();
		this.entity.targetAnimal = null;
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getFeedTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.hasTargetAnimal() && !this.entity.targetAnimal.isLeashed()) {
			Optional<Integer> food = this.getFeedItem();
			if (!food.isEmpty()) {
				this.feed(food.get());
			}
		}
		
	}
	
	private Optional<Integer> getFeedItem() {
		SimpleInventory inventory = this.entity.getInventory();
		for (int idx = 0; idx < inventory.size(); idx++) {
			if (this.entity.targetAnimal.isBreedingItem(inventory.getStack(idx))) {
				return Optional.of(idx);
			}
		}
		this.requestFeedItem();
		return Optional.empty();
	}
	
	private void requestFeedItem() {
		Optional<Item> itm = AmaziaRegistrys.ANIMAL_FEED_ITEMS.getRandom(Registry.ENTITY_TYPE.getId(this.entity.targetAnimal.getType()));
		this.entity.requestItem(itm.get());
		this.entity.targetAnimal = null;
	}
	
	private void feed(int val) {
		ItemStack itm = this.entity.getInventory().getStack(val);
		if (!itm.isEmpty()) {
			this.entity.targetAnimal.lovePlayer(null);
			itm.decrement(1);
			this.entity.getInventory().setStack(val, itm);
		}
	}

}
