package net.denanu.amazia.entities.village.server.goal.lumber;

import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class PlantSaplingGoal extends TimedVillageGoal<LumberjackEntity> {
	boolean isRunning;

	public PlantSaplingGoal(final LumberjackEntity e, final int priority) {
		super(e, priority);
		this.isRunning = false;
	}

	@Override
	public boolean canStart() {
		if (!super.canStart()) {
			return false;
		}
		if (!this.entity.hasSapling()) {
			this.entity.requestSapling();
			return false;
		}
		if (!this.entity.hasLumberLoc()) {
			this.entity.setLumberingLoc(this.entity.getVillage().getLumber().getPlantLocation());
		}
		return this.entity.hasLumberLoc() && this.entity.isInLumberLoc()
				&& this.resetIfNecesary(this.entity.getLumberingLoc().getPos().isEmpty(this.entity.getWorld()));
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
			final int sapling = this.entity.getStackPosOfSapleing();
			if ((sapling >= 0) && this.placeBlock(sapling, this.entity.getLumberingLoc().getPos().getPlantLoc())) {
				ActivityFoodConsumerMap.plantSaplingUseFood(this.entity);
				AmaziaXpGainMap.gainPlantSaplingXP(this.entity);
			}
		}
	}

	private boolean placeBlock(final int sapling, final BlockPos pos) {
		final ItemStack stack = this.entity.getInventory().getStack(sapling);
		if (this.placeBlockType(stack, Items.ACACIA_SAPLING, Blocks.ACACIA_SAPLING, pos, sapling)
				|| this.placeBlockType(stack, Items.BIRCH_SAPLING, Blocks.BIRCH_SAPLING, pos, sapling)
				|| this.placeBlockType(stack, Items.DARK_OAK_SAPLING, Blocks.DARK_OAK_SAPLING, pos, sapling)
				|| this.placeBlockType(stack, Items.JUNGLE_SAPLING, Blocks.JUNGLE_SAPLING, pos, sapling)
				|| this.placeBlockType(stack, Items.OAK_SAPLING, Blocks.OAK_SAPLING, pos, sapling)
				|| this.placeBlockType(stack, Items.SPRUCE_SAPLING, Blocks.SPRUCE_SAPLING, pos, sapling)) {
			return true;
		}
		return false;
	}

	private boolean placeBlockType(final ItemStack stack, final Item itm, final Block block, final BlockPos pos,
			final int idx) {
		if (stack.isOf(itm)) {
			this.entity.getWorld().setBlockState(pos, block.getDefaultState());
			stack.decrement(1);
			this.entity.getInventory().setStack(idx, stack);
			return true;
		}
		return false;
	}

}
