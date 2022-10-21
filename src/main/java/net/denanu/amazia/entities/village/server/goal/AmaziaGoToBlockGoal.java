package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.pathing.PathingPath;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class AmaziaGoToBlockGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaGoToBlockGoal<E> {

	public AmaziaGoToBlockGoal(E e, int priority) {
		super(e, priority);
	}
	@Override
	public boolean canStart() {
		return super.canStart() && !this.entity.isDeposeting();
	}
}
