package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class InteractWithContainerGoal extends TimedVillageGoal {
	private boolean isRunning;
	

	public InteractWithContainerGoal(AmaziaVillagerEntity e) {
		super(e, -2);
	}
	
	@Override
	public void start() {
		super.start();
		this.isRunning = true;
	}
	
	@Override
	public void stop() {
		super.stop();
		this.isRunning = false;
	}
	
	private boolean isWithinRange() {
		return (
					Math.abs(this.getContainerPos().getPos().getX() + 0.5 - this.entity.getX()) < 1.5 &&
					Math.abs(this.getContainerPos().getPos().getY() + 0.5 - this.entity.getY()) < 4 &&
					Math.abs(this.getContainerPos().getPos().getZ() + 0.5 - this.entity.getZ()) < 1.5
				);
	}
	
	@Override
	public boolean shouldContinue() {
		return this.isWithinRange() && super.shouldContinue();
	}
	
	@Override
	public void tick() {
		super.tick();
		this.entity.getLookControl().lookAt(
				this.getContainerPos().getPos().getX() + 0.5f,
				this.getContainerPos().getPos().getY() + 0.5f,
				this.getContainerPos().getPos().getZ() + 0.5
			);
	}

	public abstract StoragePathingData getContainerPos();

	public boolean isRunning() {
		return isRunning;
	}
	
	public static boolean canMergeItems(ItemStack first, ItemStack second) {
        if (!first.isOf(second.getItem())) {
            return false;
        }
        if (first.getDamage() != second.getDamage()) {
            return false;
        }
        if (first.getCount() > first.getMaxCount()) {
            return false;
        }
        return ItemStack.areNbtEqual(first, second);
    }
}
