package net.denanu.amazia.entities.village.server.goal;

import net.minecraft.entity.ai.goal.Goal;

public class FollowPlayerGoal extends Goal {

	@Override
	public boolean canStart() {
		return false;
	}

}
