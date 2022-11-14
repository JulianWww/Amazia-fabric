package net.denanu.amazia.mixin.enemies;

import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

public class EnemyGoalBuilder {

	public static Goal buildAmaziaTargetGoal(final MobEntity mob) {
		return new ActiveTargetGoal<AmaziaEntity>(mob, AmaziaEntity.class, true);
	}

}
