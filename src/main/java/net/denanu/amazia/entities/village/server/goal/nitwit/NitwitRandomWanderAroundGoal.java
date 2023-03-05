package net.denanu.amazia.entities.village.server.goal.nitwit;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class NitwitRandomWanderAroundGoal<T extends AmaziaVillagerEntity> extends AmaziaGoToBlockGoal<T> {
	private float updateTime = 0;
	private int age = 0;


	public NitwitRandomWanderAroundGoal(final T e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		if (this.age > 20) {
			if (this.updateTime < this.entity.getRandom().nextFloat()) {
				this.updateTime = this.entity.getRandom().nextFloat();
				this.updateTime = 0.7f - JJUtils.square(JJUtils.square(this.updateTime)) * 0.5f;
				return super.canStart();
			}
			this.age = 0;
		}
		this.age++;
		return false;
	}

	@Override
	public boolean shouldContinue() {
		return this.subShouldContinue();
	}

	@Override
	protected BlockPos getTargetBlock() {
		return this.entity.getVillage().getPathingGraph().getRandomNode();
	}

	@Override
	protected boolean canRun() {
		return true;
	}
}
