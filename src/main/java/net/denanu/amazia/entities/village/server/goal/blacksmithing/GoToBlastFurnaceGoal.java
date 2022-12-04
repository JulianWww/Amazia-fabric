package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import net.denanu.amazia.entities.village.server.AmaziaSmelterVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.sceduling.utils.BlockAreaPathingData;
import net.minecraft.util.math.BlockPos;

public class GoToBlastFurnaceGoal<E extends AmaziaSmelterVillagerEntity> extends AmaziaGoToBlockGoal<E> {

	public GoToBlastFurnaceGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return
				!this.entity.canDepositItems() &&
				this.entity.requestCoalOrCanRun() &&
				super.canStart() &&
				this.hasOrRequestBlastableItem();
	}

	public boolean hasOrRequestBlastableItem() {
		if (this.entity.canSmelt()) {
			return true;
		}
		if (!this.entity.hasRequestedItems() && this.entity.age % 200 == 0) {
			this.entity.requestSmeltable();
		}
		return false;
	}

	protected BlockAreaPathingData<?> getTarget() {
		return this.entity.getVillage().getBlasting().getLocation();
	}

	protected BlockPos subGetTargetBlock() {
		final BlockAreaPathingData<?> table = this.getTarget();
		if (table == null) {
			return null;
		}
		this.entity.setTargetPos(table.getPos());
		return table.getAccessPoint();
	}

	@Override
	protected BlockPos getTargetBlock() {
		return this.subGetTargetBlock();
	}
}