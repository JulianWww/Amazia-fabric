package net.denanu.amazia.entities.village.server.goal.lumber;

import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.village.sceduling.LumberSceduler;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class HarvestTreeGoal extends TimedVillageGoal<LumberjackEntity> {

	public HarvestTreeGoal(LumberjackEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		if (!super.canStart()) { return false; }
		if (!this.entity.canLumber()) {
			this.entity.requestAxe();
			return false;
		}
		if (!this.entity.hasLumberLoc() && false) {
			this.entity.setLumberingLoc(this.entity.getVillage().getLumber().getHarvestLocation());
		}
		return this.entity.hasLumberLoc() && this.entity.isInLumberLoc() && 
				this.entity.getLumberingLoc().getPos().isFull(this.entity.getWorld()); // check if a tree is present
	}
	
	@Override
	public void stop() {
		super.stop();
		this.entity.setLumberingLoc(null);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getPlantTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.hasLumberLoc()) {
			this.breakBlock(this.entity.getLumberingLoc().getPos().getPlantLoc());
			this.entity.setCollectTimer();
		}
	}
	
	private void breakBlock(BlockPos pos) {
		if (this.isInRange(pos)) {
			this.entity.getWorld().breakBlock(pos, true);
			
			this.breakBlock(pos.up());
			this.breakBlock(pos.down());
			this.breakSide(pos.east());
			this.breakSide(pos.west());
			this.breakSide(pos.south());
			this.breakSide(pos.north());
		}
	}
	
	private void breakSide(BlockPos pos) {
		this.breakBlock(pos.up());
		this.breakBlock(pos);
		this.breakBlock(pos.down());
	}
	
	private boolean isInRange(BlockPos pos) {
		return 	Math.abs(pos.getX() - this.entity.getLumberingLoc().getPos().getX()) < 5 && 
				Math.abs(pos.getZ() - this.entity.getLumberingLoc().getPos().getZ()) < 5 && 
				pos.getY() >= this.entity.getLumberingLoc().getPos().getY() &&
				(this.isLog(pos) || this.isLeaf(pos));
	}
	
	private boolean isLog(BlockPos pos) {
		return LumberSceduler.isLog((ServerWorld)this.entity.getWorld(), pos);
	}
	private boolean isLeaf(BlockPos pos) {
		return this.entity.world.getBlockState(pos).getBlock() instanceof LeavesBlock;
	}
}
