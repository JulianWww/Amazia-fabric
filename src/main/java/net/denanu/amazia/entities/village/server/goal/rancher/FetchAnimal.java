package net.denanu.amazia.entities.village.server.goal.rancher;

import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class FetchAnimal extends AmaziaGoToBlockGoal<RancherEntity> {

	public FetchAnimal(RancherEntity e, int priority) {
		super(e, priority);
	}

	@Override
	protected BlockPos getTargetBlock() {
		if (this.entity.targetAnimal == null) {
			this.entity.searchForTargetAnimal();
		}
		return this.entity.targetAnimal == null ? null : this.entity.targetAnimal.getBlockPos();
	}

	@Override
	public void fail() {
		this.entity.releaseTargetEntity();
	}
}
