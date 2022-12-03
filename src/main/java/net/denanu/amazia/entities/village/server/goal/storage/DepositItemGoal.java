package net.denanu.amazia.entities.village.server.goal.storage;

import javax.annotation.Nullable;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.BaseAmaziaVillageGoal;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import oshi.util.tuples.Triplet;

public class DepositItemGoal extends BaseAmaziaVillageGoal<AmaziaVillagerEntity> implements StoragePathingInterface, StoragePutInteractionGoalInterface {
	private DoubleDownPathingData target;
	private Triplet<ItemStack, Integer, Integer> item;
	private boolean isPathing, isDone, containerInteracting;
	private final GoToStorageGoal pathingSubGoal;
	private final PutItemInContainerGoal interactionGoal;
	@Nullable
	private final VoidToVoidCallback callback;

	public DepositItemGoal(final AmaziaVillagerEntity e, final int priority, final VoidToVoidCallback callback) {
		super(e, priority);
		this.pathingSubGoal = new GoToStorageGoal(e, this);
		this.interactionGoal = new PutItemInContainerGoal(e, this);
		this.callback = callback;
	}

	@Override
	public boolean canStart() {
		final boolean out = this.getCanStart();
		if (this.entity.isDeposeting()) {
			this.entity.setDeposeting(this.target != null);
		}
		return out;
	}
	private boolean getCanStart() {
		if (!super.canStart()) { return false; }
		if (this.item == null || this.target == null) {
			this.item = this.entity.getDepositableItems();
			if (this.item != null && !this.item.getA().isEmpty()) {
				this.target = this.entity.getVillage().getStorage().getDepositLocation((ServerWorld)this.entity.getWorld(), this.getItem());
			}
		}
		return this.target != null && this.item != null && !this.item.getA().isEmpty() && this.pathingSubGoal.canStart();
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart() && !this.isDone;
	}

	@Override
	public void start() {
		super.start();
		this.entity.setDeposeting(true);
		this.pathingSubGoal.start();
		this.isPathing = true;
		this.containerInteracting = false;
		this.isDone = false;
	}

	@Override
	public void stop() {
		super.stop();
		this.target = null;
		this.item = null;
		this.isDone = false;
		if (this.callback != null) {
			this.callback.call();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isPathing) {
			if (this.switchFromPathing()) { // there is an implied not (all works out)
				this.pathingSubGoal.tick();
			}
		}
		else if (this.containerInteracting) {
			if (!this.interactionGoal.shouldContinue()) {
				this.interactionGoal.stop(); return;
			}
			this.interactionGoal.tick();
		}
	}

	public boolean switchFromPathing() {
		if (!this.pathingSubGoal.shouldContinue()) {
			this.pathingSubGoal.stop();
			return false;
		}
		return true;
	}

	@Override
	public BlockPos getTargetBlockPos() {
		if (this.target != null) {
			final BlockPos t = this.target.getAccessPoint();
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	@Override
	public boolean canStartPathing() {
		return true;
	}

	@Override
	public void endPathingPhase() {
		this.isPathing = false;
		if (this.interactionGoal.canStart() && this.shouldContinue()) {
			this.containerInteracting = true;
			this.interactionGoal.start();
		}
		else {
			this.isDone = true;
		}
	}

	@Override
	public void StorageInteractionDone() {
		this.containerInteracting = false;
		this.isDone = true;
	}

	@Override
	public DoubleDownPathingData getTarget() {
		return this.target;
	}

	@Override
	public ItemStack getItem() {
		return this.item.getA();
	}

	@Override
	public int getItemIdx() {
		return this.item.getB();
	}

	@Override
	public int getMaxDepositable() {
		return this.item.getC();
	}
}
