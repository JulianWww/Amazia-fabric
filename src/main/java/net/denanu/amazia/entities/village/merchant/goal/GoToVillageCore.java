package net.denanu.amazia.entities.village.merchant.goal;

import net.denanu.amazia.entities.village.merchant.AmaziaVillageMerchant;
import net.denanu.amazia.entities.village.merchant.AmaziaVillageMerchant.MovementPhases;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.networking.s2c.AmaziaCustomChatMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class GoToVillageCore extends AmaziaGoToBlockGoal<AmaziaVillageMerchant> {
	MovementPhases action;

	public GoToVillageCore(final AmaziaVillageMerchant e, final int priority) {
		super(e, priority);
	}

	public GoToVillageCore(final AmaziaVillageMerchant e, final int priority, final int foodUsage) {
		super(e, priority, foodUsage);
	}

	public GoToVillageCore(final AmaziaVillageMerchant e, final int priority, final int foodUsage, final float speed) {
		super(e, priority, foodUsage, speed);
	}

	@Override
	public boolean canStart() {
		return this.entity.hasVillage() && !this.entity.canWander() && super.canStart();
	}

	@Override
	public boolean shouldContinue() {
		return this.entity.getMove_phase() == this.action && super.shouldContinue();
	}

	@Override
	public void stop() {
		super.stop();
		this.entity.reachCore(this.action);
		this.entity.setHome();
	}

	@Override
	public void fail() {
		if (this.entity.hasVillage()) {
			final ServerPlayerEntity mayor = this.entity.getVillage().getMayor();
			if (mayor != null) {
				AmaziaCustomChatMessageS2CPacket.sendWarning(mayor, "amazia.village.merchant.nopath", Text.literal(", ").append(this.entity.getName()).append(","));
			}
		}
		this.entity.discard();
	}

	@Override
	protected BlockPos getTargetBlock() {
		this.action = this.entity.getMove_phase();
		return this.entity.cannotDespawn() ? this.entity.getVillage().getOrigin().up() : this.entity.getOrigin();
	}




}
