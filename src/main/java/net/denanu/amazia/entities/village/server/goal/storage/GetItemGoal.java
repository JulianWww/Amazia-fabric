package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaVillageGoal;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class GetItemGoal extends AmaziaVillageGoal<AmaziaVillagerEntity> implements StorageGetInteractionGoalInterface, StoragePathingInterface {
	private StoragePathingData target;
	private Item item;
	private boolean isPathing, isDone, containerInteracting;
	private GoToStorageGoal pathingSubGoal;
	private GetItemFromStorage interactionGoal;

	public GetItemGoal(AmaziaVillagerEntity e, int priority, VoidToVoidCallback callback) {
		super(e, priority);
		this.pathingSubGoal = new GoToStorageGoal(e, this);
		this.interactionGoal = new GetItemFromStorage(e, this, callback);
	}
	
	@Override
	public boolean canStart() {
		if (!super.canStart()) { return false; }
		if ((item == null || this.target == null) && this.entity.hasRequestedItems()) {
			item = this.entity.getRandomRequiredItem();
			if (item != null) {
				Amazia.LOGGER.info(item.toString());
				this.target = this.entity.getVillage().getStorage().getRequestLocation((ServerWorld)this.entity.getWorld(), this.getItem());
				if (this.target == null && this.entity.canCraft()) {
					this.entity.tryCraftingStart(item);
				}
				this.entity.removeRequestedItem(item);
			}
		}
		return this.target != null && item != null && this.pathingSubGoal.canStart();
	}
	
	@Override
	public boolean shouldContinue() {
		return this.canStart() && !this.isDone;
	}
	
	@Override
	public void start() {
		super.start();
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
		return;
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
			BlockPos t = this.target.getAccessPoint();
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
	public StoragePathingData getTarget() {
		return this.target;
	}

	@Override
	public Item getItem() {
		return item;
	}
}
