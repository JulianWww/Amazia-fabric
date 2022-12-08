package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaVillageGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.village.structures.MineStructure;

public class MoveToEndOfMine extends AmaziaVillageGoal<MinerEntity> {

	public MoveToEndOfMine(final MinerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.isInMine() && !this.entity.getMine().isAtEnd(this.entity);
	}

	@Override
	public void start() {
		super.start();

	}
	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void tick() {
		super.tick();
		final MineStructure mine = this.entity.getMine();
		if (mine != null) {
			final double x = Math.floor(this.entity.getX() - mine.getDirection().getOffsetX() * 2) + 0.5;
			final double y = this.entity.getY();
			final double z = Math.floor(this.entity.getZ() - mine.getDirection().getOffsetZ() * 2) + 0.5;
			this.entity.getMoveControl().moveTo(x, y, z, 1.0f);
			ActivityFoodConsumerMap.walkUseFood(this.entity, 50);
		}
	}
}
