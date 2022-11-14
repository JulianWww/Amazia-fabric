package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.util.math.BlockPos;

public class GoToAnvilGoal extends AmaziaGoToBlockGoal<BlacksmithEntity> {

	public GoToAnvilGoal(final BlacksmithEntity e, final int priority) {
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

	@Override
	protected BlockPos getTargetBlock() {
		final NoHeightPathingData loc = this.entity.getVillage().getBlacksmithing().getLocation();
		if (loc == null) {
			return null;
		}
		this.entity.setTargetAnvilPos(loc.getPos());
		return loc.getAccessPoint();
	}

}
