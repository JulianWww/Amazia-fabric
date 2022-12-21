package net.denanu.amazia.entities.village.server.goal.rancher;

import net.denanu.amazia.components.AmaziaEntityComponents;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
import net.minecraft.util.math.BlockPos;

public class BringAnimalsToPen extends AmaziaGoToBlockGoal<RancherEntity> {

	public BringAnimalsToPen(final RancherEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return this.entity.hasTargetAnimal() &&
				this.entity.targetAnimal.isLeashed() &&
				super.canStart() &&
				this.entity.targetAnimal.getHoldingEntity() == this.entity &&
				!AmaziaEntityComponents.getIsPartOfVillage(this.entity.targetAnimal);
	}

	@Override
	public void stop() {
		super.stop();
		if (this.entity.targetAnimal != null && this.entity.targetAnimal.getHoldingEntity() == this.entity){
			this.entity.targetAnimal.teleport(this.entity.getX(), this.entity.getY(), this.entity.getZ());
			this.entity.targetAnimal.detachLeash(true, false);
			AmaziaEntityComponents.setIsPartOfVillage(this.entity.targetAnimal, true);
			AmaziaXpGainMap.gainMoveAnimalXp(this.entity);
			this.entity.releaseTargetEntity();
		}
	}

	@Override
	protected BlockPos getTargetBlock() {
		BlockPos out = this.entity.getVillage().getRanching().getEntityTargetPos(this.entity.targetAnimal);
		return out != null ? out.up() : null;
	}

}
