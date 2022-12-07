package net.denanu.amazia.entities.village.server.goal.cleric;

import java.util.Collection;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SelectHealingTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
	public SelectHealingTargetGoal(final MobEntity mob, final Class<T> targetClass, final boolean checkVisibility) {
		super(mob, targetClass, checkVisibility);
		this.makeNonAttackingPredicate(null);
	}

	public SelectHealingTargetGoal(final MobEntity mob, final Class<T> targetClass, final boolean checkVisibility, final Predicate<LivingEntity> targetPredicate) {
		super(mob, targetClass, checkVisibility, targetPredicate);
		this.makeNonAttackingPredicate(targetPredicate);
	}

	public SelectHealingTargetGoal(final MobEntity mob, final Class<T> targetClass, final boolean checkVisibility, final boolean checkCanNavigate) {
		super(mob, targetClass, checkVisibility, checkCanNavigate);
		this.makeNonAttackingPredicate(null);
	}

	public SelectHealingTargetGoal(final MobEntity mob, final Class<T> targetClass, final int reciprocalChance, final boolean checkVisibility, final boolean checkCanNavigate, @Nullable final Predicate<LivingEntity> targetPredicate) {
		super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
		this.makeNonAttackingPredicate(targetPredicate);
	}

	private void makeNonAttackingPredicate(final Predicate<LivingEntity> pred) {
		this.targetPredicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(pred);
	}

	@Override
	protected void findClosestTarget() {
		this.targetEntity =  this.getLowestHpEntity(
				this.targetClass == PlayerEntity.class ?
						this.mob.world.getPlayers(this.targetPredicate, this.mob, this.getSearchBox(this.getFollowRange())) :
							this.mob.world.getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), e -> true)
				);
		return;

	}


	@Nullable
	public LivingEntity getLowestHpEntity(final Collection<? extends LivingEntity> entityList) {
		float maxHp = -1.0f;
		LivingEntity target = null;
		for (final LivingEntity current : entityList) {
			if (!this.targetPredicate.test(this.mob, current)) {
				continue;
			}
			final float currentHp = current.getHealth();
			if (maxHp != -1 && currentHp >= maxHp) {
				continue;
			}
			maxHp = currentHp;
			target = current;
		}
		return target;
	}
}
