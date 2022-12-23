package net.denanu.amazia.entities.village.server.goal.mechanics.education;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.util.math.BlockPos;

public class GoToLibraryGoal extends AmaziaGoToBlockGoal<AmaziaVillagerEntity> {

	public GoToLibraryGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}
	public GoToLibraryGoal(final AmaziaVillagerEntity e, final int priority, final int foodUsage) {
		super(e, priority, foodUsage);
	}

	public GoToLibraryGoal(final AmaziaVillagerEntity e, final int priority, final int foodUsage, final float speed) {
		super(e, priority, foodUsage, speed);
	}

	@Override
	public boolean canStart() {
		return super.canStart();
	}

	@Override
	protected BlockPos getTargetBlock() {
		final NoHeightPathingData data = this.entity.getVillage().getLibrary().getLocation();
		return data == null ? null : data.getAccessPoint();
	}

}
