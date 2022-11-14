package net.denanu.amazia.entities.village.server.goal.rancher;

import net.denanu.amazia.components.AmaziaEntityComponents;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaVillageGoal;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;

public class LeashAnimal extends TimedVillageGoal<RancherEntity> {

	public LeashAnimal(RancherEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return this.entity.hasTargetAnimal() && 
				super.canStart() && 
				!this.entity.targetAnimal.isLeashed() && 
				this.entity.getPos().isInRange(this.entity.targetAnimal.getPos(), 2) && 
				!AmaziaEntityComponents.getIsPartOfVillage(this.entity.targetAnimal);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getLeashTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.hasTargetAnimal() && !this.entity.targetAnimal.isLeashed()) {
			this.entity.targetAnimal.attachLeash(this.entity, true);
		}
		
	} 

}
