package net.denanu.amazia.village.sceduling;

import java.util.PriorityQueue;

import javax.annotation.Nullable;

import net.denanu.amazia.commands.AmaziaGameRules;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.opponents.OpponentData;
import net.minecraft.entity.mob.MobEntity;
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

	@Nullable
	public OpponentData getOpponent() {
		@Nullable
		final OpponentData target = this.enemyQueue.peek();
		if (target == null) {
			return null;
		}
		target.decrementPriority();
		if (target.getPriority() > 0 || !target.getTarget().isAlive() || !this.village.isInVillage(target.getTarget())) {
			this.enemyQueue.poll();
			return this.getOpponent();
		}
		return target;
	}

	public void addOpponent(final MobEntity mob, final int priority) {
		if (!this.isEnemey(mob)) {
			this.enemyQueue.add(new OpponentData(mob, priority));
		}
		if (mob.world.getGameRules().getBoolean(AmaziaGameRules.VILLAGE_ENEMIES_GLOW)) {
			mob.setGlowing(true);
		}
	}

	private boolean isEnemey(final MobEntity mob) {
		for (final OpponentData enemy : this.enemyQueue) {
			if (enemy.equals(mob)) {
				return true;
			}
		}
		return false;
	}

	public void readNbt(final NbtCompound nbt) {
	}
}
