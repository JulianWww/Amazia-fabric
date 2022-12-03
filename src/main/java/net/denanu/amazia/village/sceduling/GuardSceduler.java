package net.denanu.amazia.village.sceduling;

import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import javax.annotation.Nullable;

import net.denanu.amazia.commands.AmaziaGameRules;
import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.opponents.OpponentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class GuardSceduler {
	protected final Village village;

	private final PriorityQueue<OpponentData> enemyQueue;
	private final Set<GuardEntity> combatants;

	public GuardSceduler(final Village _village) {
		this.village = _village;
		this.enemyQueue = new PriorityQueue<OpponentData>();
		this.combatants = new HashSet<GuardEntity>();
	}

	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		return nbt;
	}

	public void readNbt(final NbtCompound nbt) {
	}

	@Nullable
	public OpponentData getOpponent() {
		@Nullable
		final OpponentData target = this.enemyQueue.peek();
		if (target == null) {
			return null;
		}
		target.decrementPriority();
		if (target.getTarget() instanceof final PlayerEntity player) {
			if (player.isCreative() || player.isSpectator()) {
				return this.getOpponent();
			}
		}
		if (target.getPriority() < 0 || !target.getTarget().isAlive() || !this.village.isInVillage(target.getTarget())) {
			this.enemyQueue.poll();
			if (target.getTarget().world.getGameRules().getBoolean(AmaziaGameRules.VILLAGE_ENEMIES_GLOW)) {
				target.getTarget().setGlowing(false);
			}
			return this.getOpponent();
		}
		return target;
	}

	public void addOpponent(final Entity entity, final int priority) {
		if (entity instanceof final LivingEntity living && !(living instanceof AmaziaEntity)) {
			if (living instanceof final PlayerEntity player) {
				if (player.isCreative() || player.isSpectator()) {
					return;
				}
			}
			if (!this.isEnemey(living)) {
				this.enemyQueue.add(new OpponentData(living, priority));
			}
			if (entity.world.getGameRules().getBoolean(AmaziaGameRules.VILLAGE_ENEMIES_GLOW)) {
				entity.setGlowing(true);
			}
		}
	}

	private boolean isEnemey(final LivingEntity mob) {
		for (final OpponentData enemy : this.enemyQueue) {
			if (enemy.equals(mob)) {
				return true;
			}
		}
		return false;
	}

	public void addCombatant(final GuardEntity guardEntity) {
		this.combatants.add(guardEntity);
	}

	public void removeCombatant(final GuardEntity guardEntity) {
		this.combatants.remove(guardEntity);
	}

	public boolean isInCombat() {
		return !this.combatants.isEmpty();
	}

	public Collection<? extends LivingEntity> getCombatants() {
		return this.combatants;
	}
}
