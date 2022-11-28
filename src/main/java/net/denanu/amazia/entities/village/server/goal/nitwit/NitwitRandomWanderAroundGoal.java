package net.denanu.amazia.entities.village.server.goal.nitwit;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.NitwitEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class NitwitRandomWanderAroundGoal extends AmaziaGoToBlockGoal<NitwitEntity> {
	private int updateTime = 0;


	public NitwitRandomWanderAroundGoal(final NitwitEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		if (this.updateTime < 0) {
			this.updateTime = (int) JJUtils.rand.nextGaussian(10f, 60f);
			return super.canStart();
		}
		this.updateTime--;
		return false;
	}

	@Override
	protected BlockPos getTargetBlock() {
		return this.entity.getVillage().getPathingGraph().getRandomNode();
	}

}
