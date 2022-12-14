package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import net.denanu.amazia.entities.village.server.AmaziaSmelterVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.entities.village.server.goal.storage.InteractWithContainerGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class PutItemsInFurnaceGoal<E extends AmaziaSmelterVillagerEntity> extends TimedVillageGoal<E> {

	public PutItemsInFurnaceGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return
				super.canStart() &&
				this.entity.canSmelt() &&
				this.entity.getBlockPos().getManhattanDistance(this.entity.getTargetPos()) <= 2;
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		if (this.entity.getSmeltingItem().isPresent() && this.entity.hasCoal()) {
			if (((ServerWorld)this.entity.world).getBlockEntity(this.entity.getTargetPos()) instanceof final AbstractFurnaceBlockEntity furnace) {
				this.moveItems(this.entity.getSmeltingItem().get(), 0, furnace);

				for (int i=0; i<this.entity.getInventory().size(); i++) {
					this.moveItems(i, 1, furnace);
				}

				furnace.setStack(2, this.entity.getInventory().addStack(furnace.getStack(2)));

				this.entity.requestSmeltable();
			}
		}

	}

	private final void moveItems(final int blatingItem, final int slot, final AbstractFurnaceBlockEntity furnace) {
		final ItemStack blastStack = this.entity.getInventory().getStack(blatingItem);
		final ItemStack currentStack = furnace.getStack(slot);
		if (furnace.isValid(slot, blastStack)) {
			if (currentStack.isEmpty()) {
				furnace.setStack(slot, blastStack);
				this.entity.getInventory().setStack(blatingItem, ItemStack.EMPTY);
			}
			else if (InteractWithContainerGoal.canMergeItems(blastStack, currentStack)) {
				final int i = currentStack.getMaxCount() - currentStack.getCount();
				final int j = Math.min(blastStack.getCount(), i);
				currentStack.increment(j);
				blastStack.decrement(j);

				furnace.setStack(slot, currentStack);
				this.entity.getInventory().setStack(blatingItem, blastStack);
			}
			ActivityFoodConsumerMap.interactWithContainerUseFood(this.entity);
		}
	}
}
