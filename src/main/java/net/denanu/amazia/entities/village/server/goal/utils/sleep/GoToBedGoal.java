package net.denanu.amazia.entities.village.server.goal.utils.sleep;

import net.denanu.amazia.block.AmaziaBlockProperties;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.scedule.VillageActivityGroups;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.block.BedBlock;
import net.minecraft.util.math.BlockPos;

public class GoToBedGoal extends AmaziaGoToBlockGoal<AmaziaVillagerEntity> {

	public GoToBedGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}
	public GoToBedGoal(final AmaziaVillagerEntity e, final int priority, final int foodUsage) {
		super(e, priority, foodUsage);
	}
	public GoToBedGoal(final AmaziaVillagerEntity e, final int priority, final int foodUsage, final float speed) {
		super(e, priority, foodUsage, speed);
	}

	@Override
	public boolean canStart( ) {
		return !this.entity.isSleeping() && super.canStart();
	}

	@Override
	protected BlockPos getTargetBlock() {
		if (this.entity.hasBedLoc()) {
			return this.entity.getBedAccessPoint();
		}
		final NoHeightPathingData pos = this.entity.getVillage().getBeds().getLocation(state -> state.getBlock() instanceof BedBlock && !state.get(AmaziaBlockProperties.RESERVED));
		if (pos != null) {
			this.entity.reserveBed(pos.getPos());
			return pos.getAccessPoint();
		}
		return null;
	}

	@Override
	protected boolean canRun() {
		return this.entity.getActivityScedule().getPerformActionGroup() == VillageActivityGroups.SLEEP;
	}

	@Override
	public void stop() {
		if (this.entity.getBedLocation().getSquaredDistance(this.entity.getBlockPos()) < 9) {
			this.entity.sleep(this.entity.getBedLocation());
		}
		this.entity.getMoveControl().stop();
		super.stop();
	}
}
