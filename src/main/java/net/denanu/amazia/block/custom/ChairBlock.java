package net.denanu.amazia.block.custom;

import net.denanu.amazia.entities.furnature.SeatEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ChairBlock extends HorizontalFacingBlock {
	private static VoxelShape SOUTH_BOX = VoxelShapes.union(
			Block.createCuboidShape(1,  0, 0,  16, 14, 2),
			Block.createCuboidShape(1, 	0, 14, 16, 14, 16),
			Block.createCuboidShape(13, 2, 2, 15, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape NORTH_BOX = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0,  15, 14, 2),
			Block.createCuboidShape(0, 	0, 14, 15, 14, 16),
			Block.createCuboidShape(1, 2,  2,  3, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape EAST_BOX = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 1,  2, 14, 16),
			Block.createCuboidShape(14, 0, 1,  16, 14, 16),
			Block.createCuboidShape(2, 2, 13, 14, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape WEST_BOX = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0, 2, 14, 15),
			Block.createCuboidShape(14, 0, 0, 16, 14, 15),
			Block.createCuboidShape(2, 2, 1, 14, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));

	private static VoxelShape getBaseBox() {
		return VoxelShapes.union(
				Block.createCuboidShape(1.5, 8, 1.5, 14.5, 10, 14.5),
				Block.createCuboidShape(2, 0, 2, 4, 8, 4),
				Block.createCuboidShape(12, 0, 12, 14, 8, 14),
				Block.createCuboidShape(2, 0, 12, 4, 8, 14),
				Block.createCuboidShape(12, 0, 2, 14, 8, 4)
				);
	}

	public ChairBlock(final Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos, final ShapeContext context) {
		return switch (TeachersDeskBlock.getFacing(state)) {
		default -> VoxelShapes.union(
				Block.createCuboidShape(1.5,10,12.5, 14.5,20,14.5),
				ChairBlock.getBaseBox()
				);
		case NORTH -> VoxelShapes.union(
				Block.createCuboidShape(1.5,10,1.5, 14.5,20,3.5),
				ChairBlock.getBaseBox()
				);
		case EAST -> VoxelShapes.union(
				Block.createCuboidShape(12.5,10,1.5, 14.5,20,14.5),
				ChairBlock.getBaseBox()
				);
		case WEST -> VoxelShapes.union(
				Block.createCuboidShape(1.5,10,1.5, 3.5,20,14.5),
				ChairBlock.getBaseBox()
				);
		};
	}

	@Override
	public BlockState getPlacementState(final ItemPlacementContext ctx) {
		return this.getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
	}

	@Override
	protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING);
	}

	@Override
	public ActionResult onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit) {
		return SeatEntity.create(world, pos, this.jumpVelocityMultiplier, player, state.get(HorizontalFacingBlock.FACING));
	}
}
