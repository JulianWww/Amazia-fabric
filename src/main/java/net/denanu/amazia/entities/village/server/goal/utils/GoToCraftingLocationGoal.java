package net.denanu.amazia.entities.village.server.goal.utils;

import net.denanu.amazia.entities.village.server.AmaziaSmelterVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;
import net.denanu.amazia.village.sceduling.utils.BlockAreaPathingData;
import net.minecraft.util.math.BlockPos;

public abstract class GoToCraftingLocationGoal<E extends AmaziaSmelterVillagerEntity> extends AmaziaGoToBlockGoal<E> {
	public GoToCraftingLocationGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return !this.entity.shouldDeposit && super.canStart() && this.entity.wantsToCraft() && CraftGoal.canCraft(this.entity);
	}

	@Override
	public void start() {
		super.start();
	}

	public abstract BlockAreaPathingData<?> getTarget();

	@Override
	protected BlockPos getTargetBlock() {
		final BlockAreaPathingData<?> loc = this.getTarget();
		if (loc == null) {
			return null;
		}
		this.entity.setTargetCraftingLocPos(loc.getPos());
		return loc.getAccessPoint();
	}
}