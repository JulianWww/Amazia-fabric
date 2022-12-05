package net.denanu.amazia.entities.village.server.goal.cleric;

import net.denanu.amazia.JJUtils;
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
	private final TargetPredicate canTargetPlayers;
	private Entity lastTargetEntity;

	public GoToBlessEntityGoal(final ClericEntity e, final int priority, final int chance) {
		this(e, priority, chance, BaseAmaziaGoToBlockGoal.BASE_FOOD_USAGE, 1.0f);
	}
	public GoToBlessEntityGoal(final ClericEntity e, final int priority, final int chance, final int food, final float speed) {
		super(e, priority, food, speed);
		this.reciprocalChance = chance;

		this.canTargetPlayers = TargetPredicate.createNonAttackable().setBaseMaxDistance(25).setPredicate(GoToBlessEntityGoal::canBlessPlayer);
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

	private PlayerEntity getPlayerToBless() {
		return JJUtils.getRandomListElement(this.entity.world.getPlayers(this.canTargetPlayers, this.entity, this.getBox()));
	}

	private Box getBox() {
		return this.entity.getBoundingBox().expand(25, 4, 25);
	}
	private static boolean canBlessPlayer(final LivingEntity e) {
		return !e.hasStatusEffect(StatusEffects.LUCK);
	}

	@Override
	protected Entity getTargetEntity() {
		return this.getPlayerToBless();
	}
}
