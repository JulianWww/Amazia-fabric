package net.denanu.amazia.item.custom;

import java.util.Set;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.AmaziaBlockComponents;
import net.denanu.amazia.entities.village.server.ChildEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.BedPart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmaziaChildSpawnItem extends Item {

	public AmaziaChildSpawnItem(final Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(final ItemUsageContext context) {
		Amazia.LOGGER.info("spawned Child");
		BlockState state;
		if ((state = context.getWorld().getBlockState(context.getBlockPos())).getBlock() instanceof BedBlock) {
			final World world = context.getWorld();
			if (world.isClient) {
				return ActionResult.SUCCESS;
			}
			final ChildEntity child = new ChildEntity(context.getWorld());
			child.setPosition(context.getHitPos());
			child.setup();
			context.getWorld().spawnEntity(child);

			for (final BlockPos pos : this.getVillagePoses(state, context)) {
				Amazia.LOGGER.info(pos.toString());
				if (child.scanForVillage(pos)) {
					break;
				}
			}

			return ActionResult.CONSUME;
		}
		return ActionResult.PASS;
	}

	private Set<BlockPos> getVillagePoses(final BlockState state, final ItemUsageContext context) {
		if (state.get(BedBlock.PART) != BedPart.HEAD) {
			return this.getVillagePosesFromBed(context, context.getBlockPos().offset(state.get(HorizontalFacingBlock.FACING)));
		}
		return this.getVillagePosesFromBed(context, context.getBlockPos());
	}

	private Set<BlockPos> getVillagePosesFromBed(final ItemUsageContext context, final BlockPos pos) {
		return AmaziaBlockComponents.getVillages(context.getWorld().getBlockEntity(pos));
	}

}
