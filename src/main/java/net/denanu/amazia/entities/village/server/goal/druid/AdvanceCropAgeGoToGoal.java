package net.denanu.amazia.entities.village.server.goal.druid;

import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class AdvanceCropAgeGoToGoal extends AmaziaGoToBlockGoal<DruidEntity> {
	public AdvanceCropAgeGoToGoal(final DruidEntity e, final int priority) {
		super(e, priority);
	}

	public AdvanceCropAgeGoToGoal(final DruidEntity e, final int priority, final float speed) {
		super(e, priority, speed);
	}

	@Override
	protected BlockPos getTargetBlock() {
		return this.entity.setToRegrow(this.entity.getVillage().getFarming().getGrowing());
	}

}
