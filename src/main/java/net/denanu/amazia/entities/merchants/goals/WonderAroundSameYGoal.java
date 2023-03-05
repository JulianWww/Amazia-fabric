package net.denanu.amazia.entities.merchants.goals;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.entities.merchants.AmaziaMerchant;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class WonderAroundSameYGoal extends WanderAroundGoal {
	AmaziaMerchant merchant;

	public WonderAroundSameYGoal(final AmaziaMerchant mob, final double speed) {
		super(mob, speed);
		this.merchant = mob;
	}
	public WonderAroundSameYGoal(final AmaziaMerchant mob, final double speed, final int chance) {
		super(mob, speed, chance);
		this.merchant = mob;
	}

	public WonderAroundSameYGoal(final AmaziaMerchant entity, final double speed, final int chance, final boolean canDespawn) {
		super(entity, speed, chance, canDespawn);
		this.merchant = entity;
	}

	@Override
	public boolean canStart() {
		return this.merchant.canWander() && super.canStart();
	}

	@Override
	@Nullable
	protected Vec3d getWanderTarget() {
		final Vec3d pos = NoPenaltyTargeting.find(this.mob, 10, 0);
		if (pos!=null) {
			((ServerWorld)this.mob.world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 0);
		}
		return pos;
	}

}
