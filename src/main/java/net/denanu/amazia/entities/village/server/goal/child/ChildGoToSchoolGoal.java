package net.denanu.amazia.entities.village.server.goal.child;

import net.denanu.amazia.block.custom.ChairBlock;
import net.denanu.amazia.entities.village.server.ChildEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.util.math.BlockPos;

public class ChildGoToSchoolGoal extends AmaziaGoToBlockGoal<ChildEntity> {
	BlockPos chairPos;

	public ChildGoToSchoolGoal(final ChildEntity e, final int priority) {
		super(e, priority);
	}

	public ChildGoToSchoolGoal(final ChildEntity e, final int priority, final int foodUsage) {
		super(e, priority, foodUsage);
	}

	public ChildGoToSchoolGoal(final ChildEntity e, final int priority, final int foodUsage, final float speed) {
		super(e, priority, foodUsage, speed);
	}

	@Override
	public boolean canStart() {
		return !this.entity.hasVehicle() && super.canStart();
	}

	@Override
	protected BlockPos getTargetBlock() {
		final NoHeightPathingData loc = this.entity.getVillage().getChairs().getLocation();
		if (loc != null) {
			this.chairPos = loc.getPos();
			return loc.getAccessPoint();
		}
		this.chairPos = null;
		return null;
	}

	@Override
	public void stop() {
		super.stop();
		ChairBlock.sit(
				this.entity.getWorld(),
				this.chairPos,
				this.entity
				);
		this.chairPos = null;
	}
}
