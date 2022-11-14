package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.util.math.BlockPos;

public class GoToBlastFurnaceGoal extends AmaziaGoToBlockGoal<BlacksmithEntity> {

	public GoToBlastFurnaceGoal(final BlacksmithEntity e, final int priority) {
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
		if (this.entity.canBlast()) {
			return true;
		}
		if (!this.entity.hasRequestedItems() && this.entity.age % 200 == 0) {
			this.entity.requestBlastable();
		}
		return false;
	}

	protected BlockPos subGetTargetBlock() {
		final DoubleDownPathingData  table = this.entity.getVillage().getBlasting().getLocation();
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