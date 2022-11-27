package net.denanu.amazia.village.sceduling;

import java.util.PriorityQueue;

import javax.annotation.Nullable;

import net.denanu.amazia.commands.AmaziaGameRules;
import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.opponents.OpponentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class GuardSceduler {
	protected final Village village;

	private final PriorityQueue<OpponentData> enemyQueue;

	public GuardSceduler(final Village _village) {
		this.village = _village;
		this.enemyQueue = new PriorityQueue<OpponentData>();
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
}
