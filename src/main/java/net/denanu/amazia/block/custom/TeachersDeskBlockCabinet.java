package net.denanu.amazia.block.custom;

import net.denanu.amazia.block.entity.DeskCabinetBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class TeachersDeskBlockCabinet extends TeachersDeskBlock implements BlockEntityProvider {
	public static final BooleanProperty OPEN = Properties.OPEN;
	public static final BooleanProperty LOCKED = Properties.LOCKED;

	public TeachersDeskBlockCabinet(final Settings settings) {
		super(settings);
	}

	private static final VoxelShape SINGE_EAST = VoxelShapes.union(
			Block.createCuboidShape(1,  0, 0,  16, 14, 2),
			Block.createCuboidShape(1, 	0, 14, 16, 14, 16),
			Block.createCuboidShape(1, 2, 2, 15, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape SINGE_WEST = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0,  15, 14, 2),
			Block.createCuboidShape(0, 	0, 14, 15, 14, 16),
			Block.createCuboidShape(1, 2,  2, 15, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape SINGE_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 1,  2, 14, 16),
			Block.createCuboidShape(14, 0, 1,  16, 14, 16),
			Block.createCuboidShape(2, 2, 1, 14, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape SINGE_NORTH = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0, 2, 14, 15),
			Block.createCuboidShape(14, 0, 0, 16, 14, 15),
			Block.createCuboidShape(2, 2, 1, 14, 16, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));

	private static final VoxelShape LEFT_EAST = VoxelShapes.union(
			Block.createCuboidShape(1,  0, 0,  16, 14, 2),
			Block.createCuboidShape(13, 2, 2, 15, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(1, 2, 2, 13, 14, 14));
	private static final VoxelShape LEFT_WEST = VoxelShapes.union(
			Block.createCuboidShape(0, 	0, 14, 15, 14, 16),
			Block.createCuboidShape(1, 2,  0,  3, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(3, 2,  2, 15, 14, 14));
	private static final VoxelShape LEFT_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(14, 0, 1,  16, 14, 16),
			Block.createCuboidShape(0, 2, 13, 14, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(2, 2, 1, 14, 16, 13));
	private static final VoxelShape LEFT_NORTH = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 0, 2, 14, 15),
			Block.createCuboidShape(2, 2, 1, 16, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(2, 2, 3, 14, 14, 15));

	private static final VoxelShape RIGHT_EAST = VoxelShapes.union(
			Block.createCuboidShape(1, 	0, 14, 16, 14, 16),
			Block.createCuboidShape(13, 2, 0, 15, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(1, 2, 2, 15, 16, 14));
	private static final VoxelShape RIGHT_WEST = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0, 15, 14, 2),
			Block.createCuboidShape(1, 2,  2, 3,  14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(3, 2, 2,  15, 14, 14));
	private static final VoxelShape RIGHT_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 1,  2, 14, 16),
			Block.createCuboidShape(2, 2, 13, 16, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(2, 2, 1, 14, 14, 13));
	private static final VoxelShape RIGHT_NORTH = VoxelShapes.union(
			Block.createCuboidShape(14, 0, 0, 16, 14, 15),
			Block.createCuboidShape(0, 2, 1, 14, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(2, 2, 3, 14, 14, 15));

	private static final VoxelShape MIDDLE_EAST = VoxelShapes.union(
			Block.createCuboidShape(13, 2, 0, 15, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(1, 2, 2, 13, 14, 14));
	private static final VoxelShape MIDDLE_WEST = VoxelShapes.union(
			Block.createCuboidShape(1, 2,  0,  3, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(3, 2,  2, 15, 14, 14));
	private static final VoxelShape MIDDLE_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(0, 2, 13, 16, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(2, 2, 1, 14, 14, 13));
	private static final VoxelShape MIDDLE_NORTH = VoxelShapes.union(
			Block.createCuboidShape(0, 2, 1, 16, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16),
			Block.createCuboidShape(2, 2, 3, 14, 16, 15));

	@Override
	public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos, final ShapeContext context) {
		return switch (TeachersDeskBlock.getType(state)) {
		default -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlockCabinet.SINGE_SOUTH;
			case NORTH -> TeachersDeskBlockCabinet.SINGE_NORTH;
			case EAST -> TeachersDeskBlockCabinet.SINGE_EAST;
			case WEST -> TeachersDeskBlockCabinet.SINGE_WEST;
			};
		}
		case LEFT -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlockCabinet.LEFT_SOUTH;
			case NORTH -> TeachersDeskBlockCabinet.LEFT_NORTH;
			case EAST -> TeachersDeskBlockCabinet.LEFT_EAST;
			case WEST -> TeachersDeskBlockCabinet.LEFT_WEST;
			};
		}
		case RIGHT -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlockCabinet.RIGHT_SOUTH;
			case NORTH -> TeachersDeskBlockCabinet.RIGHT_NORTH;
			case EAST -> TeachersDeskBlockCabinet.RIGHT_EAST;
			case WEST -> TeachersDeskBlockCabinet.RIGHT_WEST;
			};
		}
		case MIDDLE -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlockCabinet.MIDDLE_SOUTH;
			case NORTH -> TeachersDeskBlockCabinet.MIDDLE_NORTH;
			case EAST -> TeachersDeskBlockCabinet.MIDDLE_EAST;
			case WEST -> TeachersDeskBlockCabinet.MIDDLE_WEST;
			};
		}
		};
	}

	@Override
	protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(TeachersDeskBlockCabinet.OPEN);
		builder.add(TeachersDeskBlockCabinet.LOCKED);
	}

	@Override
	public BlockState getPlacementState(final ItemPlacementContext ctx) {
		final BlockState state = super.getPlacementState(ctx);
		final BlockPos pos = ctx.getBlockPos().offset(state.get(TeachersDeskBlock.FACING));
		return state.with(TeachersDeskBlockCabinet.OPEN, false).with(TeachersDeskBlockCabinet.LOCKED, !ctx.getWorld().isAir(pos));
	}

	@Override
	@Deprecated
	public BlockState getStateForNeighborUpdate(final BlockState state, final Direction direction, final BlockState neighborState, final WorldAccess world, final BlockPos pos, final BlockPos neighborPos) {
		final BlockPos pos2 = pos.offset(state.get(TeachersDeskBlock.FACING), -1);
		return state.with(TeachersDeskBlockCabinet.LOCKED, !world.isAir(pos2));
	}

	@Override
	public ActionResult onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit)
	{
		if(state.get(TeachersDeskBlock.FACING).getOpposite() == hit.getSide())
		{
			if(!world.isClient && world.getBlockEntity(pos) instanceof final LootableContainerBlockEntity blockEntity)
			{
				player.openHandledScreen(
						new SimpleNamedScreenHandlerFactory(blockEntity::createMenu, blockEntity.hasCustomName() ? blockEntity.getCustomName() : this.getName())
						);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	@Override
	public BlockEntity createBlockEntity(final BlockPos pos, final BlockState state) {
		return new DeskCabinetBlockEntity(pos, state);
	}
}
