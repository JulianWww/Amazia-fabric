package net.denanu.amazia.entities.village.server.goal.enchanting;

import net.denanu.amazia.entities.village.server.EnchanterEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.sceduling.utils.EnchantingPathingData;
import net.minecraft.util.math.BlockPos;

public class GoToEnchantingTable extends AmaziaGoToBlockGoal<EnchanterEntity> {

	public GoToEnchantingTable(EnchanterEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return 
				this.entity.requestLapisOrCanEnchant() && 
				!this.entity.canDepositItems() && 
				super.canStart() &&
				this.hasOrRequestEnchantableItem();
	}
	
	public boolean hasOrRequestEnchantableItem() {
		if (this.entity.hasEnchantItem()) {
			return true;
		}
		if (!this.entity.hasRequestedItems())
			this.entity.requestEnchantableItem();
		return false;
	}

	protected BlockPos subGetTargetBlock() {
		EnchantingPathingData table = this.entity.getVillage().getEnchanting().getTableLocation();
		return table == null ? null : table.getAccessPoint();
	}

	@Override
	protected BlockPos getTargetBlock() {
		this.entity.setTargetPos(this.subGetTargetBlock());
		return this.entity.getTargetPos();
	}

}
