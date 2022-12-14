package net.denanu.amazia.entities.village.server.goal.lumber;

import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
import net.denanu.amazia.village.sceduling.LumberSceduler;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class HarvestTreeGoal extends TimedVillageGoal<LumberjackEntity> {
	boolean isRunning;

	public HarvestTreeGoal(final LumberjackEntity e, final int priority) {
		super(e, priority);
		this.isRunning = false;
	}

	@Override
	public boolean canStart() {
		if (!super.canStart()) {
			return false;
		}
		if (!this.entity.canLumber()) {
			this.entity.requestAxe();
			return false;
		}
		if (!this.entity.hasLumberLoc()) {
			this.entity.setLumberingLoc(this.entity.getVillage().getLumber().getHarvestLocation());
		}
		return this.entity.hasLumberLoc() && this.entity.isInLumberLoc()
				&& this.resetIfNecesary(this.entity.getLumberingLoc().getPos().isFull(this.entity.getWorld())); // check
		// if a
		// tree
		// is
		// present
	}

	private boolean resetIfNecesary(final boolean empty) {
		if (!empty && this.isRunning) {
			this.entity.setLumberingLoc(null);
		}
		return empty;
	}

	@Override
	public void start() {
		super.start();
		this.isRunning = true;
	}

	@Override
	public void stop() {
		super.stop();
		this.isRunning = false;
		this.entity.setLumberingLoc(null);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getPlantTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.hasLumberLoc()) {
			ActivityFoodConsumerMap.harvestTreeUseFood(this.entity,
					this.breakBlock(this.entity.getLumberingLoc().getPos().getPlantLoc()));
			AmaziaXpGainMap.gainChopTreeXp(this.entity);
			HappynessMap.looseChopTreeHappyness(this.entity);
			this.entity.setCollectTimer();
		}
	}

	private int breakBlock(final BlockPos pos) {
		if (this.isInRange(pos)) {
			this.entity.getWorld().breakBlock(pos, true);

			return 1 + this.breakBlock(pos.up()) + this.breakBlock(pos.down()) + this.breakSide(pos.east())
			+ this.breakSide(pos.west()) + this.breakSide(pos.south()) + this.breakSide(pos.north());
		}
		return 0;
	}

	private int breakSide(final BlockPos pos) {
		return this.breakBlock(pos.up()) + this.breakBlock(pos) + this.breakBlock(pos.down());
	}

	private boolean isInRange(final BlockPos pos) {
		return Math.abs(pos.getX() - this.entity.getLumberingLoc().getPos().getX()) < 5
				&& Math.abs(pos.getZ() - this.entity.getLumberingLoc().getPos().getZ()) < 5
				&& pos.getY() >= this.entity.getLumberingLoc().getPos().getY() && (this.isLog(pos) || this.isLeaf(pos));
	}

	private boolean isLog(final BlockPos pos) {
		return LumberSceduler.isLog((ServerWorld) this.entity.getWorld(), pos);
	}

	private boolean isLeaf(final BlockPos pos) {
		return this.entity.world.getBlockState(pos).getBlock() instanceof LeavesBlock;
	}
}
