package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.item.ItemStack;

public abstract class InteractWithContainerGoal extends TimedVillageGoal<AmaziaVillagerEntity> {
	private boolean isRunning;


	public InteractWithContainerGoal(final AmaziaVillagerEntity e) {
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
		return Math.abs(this.getContainerPos().getPos().getX() + 0.5 - this.entity.getX()) < 2 &&
				Math.abs(this.getContainerPos().getPos().getY() + 0.5 - this.entity.getY()) < 4 &&
				Math.abs(this.getContainerPos().getPos().getZ() + 0.5 - this.entity.getZ()) < 2;
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

	public abstract DoubleDownPathingData getContainerPos();

	public boolean isRunning() {
		return this.isRunning;
	}

	public static boolean canMergeItems(final ItemStack first, final ItemStack second) {
		if (!first.isOf(second.getItem()) || first.getDamage() != second.getDamage() || first.getCount() >= first.getMaxCount()) {
			return false;
		}
		return ItemStack.areNbtEqual(first, second);
	}
}
