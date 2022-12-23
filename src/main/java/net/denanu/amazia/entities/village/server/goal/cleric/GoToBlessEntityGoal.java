package net.denanu.amazia.entities.village.server.goal.cleric;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.goal.BaseAmaziaGoToBlockGoal;
import net.denanu.amazia.entities.village.server.goal.utils.AmaziaGoToEntityGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class GoToBlessEntityGoal extends AmaziaGoToEntityGoal<ClericEntity> {
	int counter = 0;
	private final int reciprocalChance;
	private final TargetPredicate canTarget;
	private Entity lastTargetEntity;

	public GoToBlessEntityGoal(final ClericEntity e, final int priority, final int chance) {
		this(e, priority, chance, BaseAmaziaGoToBlockGoal.BASE_FOOD_USAGE, 1.0f);
	}
	public GoToBlessEntityGoal(final ClericEntity e, final int priority, final int chance, final int food, final float speed) {
		super(e, priority, food, speed);
		this.reciprocalChance = chance;

		this.canTarget = TargetPredicate.createNonAttackable().setBaseMaxDistance(25).setPredicate(GoToBlessEntityGoal::canBless);
	}

	@Override
	public void stop() {
		this.lastTargetEntity = this.targetEntity;
		super.stop();
	}

	public Entity getLastTarget() {
		return this.lastTargetEntity;
	}
	public void forgetLastTarget() {
		this.lastTargetEntity = null;
	}

	@Override
	public boolean canStart() {
		if (this.reciprocalChance > 0 && this.entity.getRandom().nextInt(this.reciprocalChance) != 0) {
			return false;
		}
		return super.canStart();
	}

	@Override
	public boolean shouldContinue() {
		return this.subShouldContinue();
	}

	@Nullable
	private PlayerEntity getPlayerToBless() {
		return JJUtils.getRandomListElement(this.entity.world.getPlayers(this.canTarget, this.entity, this.getBox()));
	}

	@Nullable
	private Entity getBlessableEntity() {
		return this.entity.world.getClosestEntity(
				AmaziaVillagerEntity.class,
				this.canTarget,
				this.entity,
				this.entity.getX(),
				this.entity.getY(),
				this.entity.getZ(),
				this.getBox()
				);
	}

	private Box getBox() {
		return this.entity.getBoundingBox().expand(25, 4, 25);
	}
	private static boolean canBless(final LivingEntity e) {
		return !e.hasStatusEffect(StatusEffects.LUCK);
	}

	@Override
	@Nullable
	protected Entity getTargetEntity() {
		Entity player;
		return (player = this.getPlayerToBless()) == null ? this.getBlessableEntity() : player;
	}
}
