package net.denanu.amazia.entities.merchants.goals;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class WonderAroundSameYGoal extends WanderAroundGoal {
	public WonderAroundSameYGoal(PathAwareEntity mob, double speed) {
		super(mob, speed);
	}
	public WonderAroundSameYGoal(PathAwareEntity mob, double speed, int chance) {
		super(mob, speed, chance);
	}
	
	public WonderAroundSameYGoal(PathAwareEntity entity, double speed, int chance, boolean canDespawn) {
		super(entity, speed, chance, canDespawn);
	}
	
	@Nullable
    protected Vec3d getWanderTarget() {
		Vec3d pos = NoPenaltyTargeting.find(this.mob, 10, 0);
		if (pos!=null)((ServerWorld)this.mob.world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 0);
        return pos;
	}

}
