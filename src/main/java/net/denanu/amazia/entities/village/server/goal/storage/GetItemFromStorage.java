package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;

public class GetItemFromStorage extends InteractWithContainerGoal {
	private StorageGetInteractionGoalInterface master;
	
	public GetItemFromStorage(AmaziaVillagerEntity e, StorageGetInteractionGoalInterface master) {
		super(e);
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		return;
	}
	
	@Override
	public void stop() {
		super.stop();
		master.StorageInteractionDone();
	}

	@Override
	public StoragePathingData getContainerPos() {
		return this.master.getTarget();
	}

}
