package net.denanu.amazia.entities.village.server;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class AmaziaVillagerEntity extends AmaziaEntity {
	protected AmaziaVillagerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public PassiveEntity createChild(ServerWorld arg0, PassiveEntity arg1) {
		return null;
	}

	public void registerBaseGoals() {
		this.goalSelector.add(1, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(1000, new LookAroundGoal(this));
	}
}
