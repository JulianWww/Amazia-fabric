package net.denanu.amazia.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TeachersDeskBlock extends HorizontalFacingBlock {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

	public TeachersDeskBlock(final Settings settings) {
		super(settings);
	}

	private static VoxelShape SINGE_EAST = VoxelShapes.union(
			Block.createCuboidShape(1,  0, 0,  16, 14, 2),
			Block.createCuboidShape(1, 	0, 14, 16, 14, 16),
			Block.createCuboidShape(13, 2, 2, 15, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape SINGE_WEST = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0,  15, 14, 2),
			Block.createCuboidShape(0, 	0, 14, 15, 14, 16),
			Block.createCuboidShape(1, 2,  2,  3, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape SINGE_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 1,  2, 14, 16),
			Block.createCuboidShape(14, 0, 1,  16, 14, 16),
			Block.createCuboidShape(2, 2, 13, 14, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape SINGE_NORTH = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0, 2, 14, 15),
			Block.createCuboidShape(14, 0, 0, 16, 14, 15),
			Block.createCuboidShape(2, 2, 1, 14, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));

	private static final VoxelShape LEFT_EAST = VoxelShapes.union(
			Block.createCuboidShape(1,  0, 0,  16, 14, 2),
			Block.createCuboidShape(13, 2, 2, 15, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape LEFT_WEST = VoxelShapes.union(
			Block.createCuboidShape(0, 	0, 14, 15, 14, 16),
			Block.createCuboidShape(1, 2,  0,  3, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape LEFT_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(14, 0, 1,  16, 14, 16),
			Block.createCuboidShape(0, 2, 13, 14, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape LEFT_NORTH = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 0, 2, 14, 15),
			Block.createCuboidShape(2, 2, 1, 16, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));

	private static final VoxelShape RIGHT_EAST = VoxelShapes.union(
			Block.createCuboidShape(1, 	0, 14, 16, 14, 16),
			Block.createCuboidShape(13, 2, 0, 15, 14, 14),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape RIGHT_WEST = VoxelShapes.union(
			Block.createCuboidShape(0,  0, 0,  15, 14, 2),
			Block.createCuboidShape(1, 2,  2,  3, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape RIGHT_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 1,  2, 14, 16),
			Block.createCuboidShape(2, 2, 13, 16, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape RIGHT_NORTH = VoxelShapes.union(
			Block.createCuboidShape(14, 0, 0, 16, 14, 15),
			Block.createCuboidShape(0, 2, 1, 14, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));

	private static final VoxelShape MIDDLE_EAST = VoxelShapes.union(
			Block.createCuboidShape(13, 2, 0, 15, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape MIDDLE_WEST = VoxelShapes.union(
			Block.createCuboidShape(1, 2,  0,  3, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape MIDDLE_SOUTH = VoxelShapes.union(
			Block.createCuboidShape(0, 2, 13, 16, 14, 15),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));
	private static final VoxelShape MIDDLE_NORTH = VoxelShapes.union(
			Block.createCuboidShape(0, 2, 1, 16, 16, 3),
			Block.createCuboidShape(0, 14, 0, 16, 16, 16));

	@Override
	public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos, final ShapeContext context) {
		return switch (TeachersDeskBlock.getType(state)) {
		default -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlock.SINGE_SOUTH;
			case NORTH -> TeachersDeskBlock.SINGE_NORTH;
			case EAST -> TeachersDeskBlock.SINGE_EAST;
			case WEST -> TeachersDeskBlock.SINGE_WEST;
			};
		}
		case LEFT -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlock.LEFT_SOUTH;
			case NORTH -> TeachersDeskBlock.LEFT_NORTH;
			case EAST -> TeachersDeskBlock.LEFT_EAST;
			case WEST -> TeachersDeskBlock.LEFT_WEST;
			};
		}
		case RIGHT -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlock.RIGHT_SOUTH;
			case NORTH -> TeachersDeskBlock.RIGHT_NORTH;
			case EAST -> TeachersDeskBlock.RIGHT_EAST;
			case WEST -> TeachersDeskBlock.RIGHT_WEST;
			};
		}
		case MIDDLE -> {
			yield switch (TeachersDeskBlock.getFacing(state)) {
			default -> TeachersDeskBlock.MIDDLE_SOUTH;
			case NORTH -> TeachersDeskBlock.MIDDLE_NORTH;
			case EAST -> TeachersDeskBlock.MIDDLE_EAST;
			case WEST -> TeachersDeskBlock.MIDDLE_WEST;
			};
		}
		};
	}

	public static Direction getFacing(final BlockState state) {
		return state.get(TeachersDeskBlock.FACING);
	}

	public static Type getType(final BlockState state) {
		return state.get(TeachersDeskBlock.TYPE);
	}

	@Override
	public BlockState rotate(final BlockState state, final BlockRotation rotation) {
		return state.with(TeachersDeskBlock.FACING, rotation.rotate(state.get(TeachersDeskBlock.FACING)));
	}

	@Override
	public BlockState mirror(final BlockState state, final BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(TeachersDeskBlock.FACING)));
	}

	@Override
	protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
		builder.add(TeachersDeskBlock.FACING);
		builder.add(TeachersDeskBlock.TYPE);
	}

	@Override
	public BlockState getPlacementState(final ItemPlacementContext ctx) {
		Type type = Type.LEFT;
		final Direction direction = ctx.getPlayerFacing();

		final BlockState right = this.updatePlace(ctx, 	direction.rotateCounterclockwise(Axis.Y), 	direction, 	TeachersDeskBlock::updatePlaceRight);
		final BlockState left =  this.updatePlace(ctx, 	direction.rotateClockwise(Axis.Y),			direction, 	TeachersDeskBlock::updatePlaceLeft);

		if (right != null && left != null) {
			type = Type.MIDDLE;
		}
		else if (left == null && right == null) {
			type = Type.SINGLE;
		}
		else if (left == null) {
			type = Type.RIGHT;
		}
		else {
			type = Type.LEFT;
		}

		return this.getDefaultState().with(TeachersDeskBlock.FACING, direction).with(TeachersDeskBlock.TYPE, type);
	}

	@Override
	public void onBroken(final WorldAccess world, final BlockPos pos, final BlockState state) {
		final Direction dir = state.get(TeachersDeskBlock.FACING);
		this.updateDestroy(world, pos, dir.rotateCounterclockwise(Axis.Y), 	dir, 	TeachersDeskBlock::updateRemoveRight);
		this.updateDestroy(world, pos, dir.rotateClockwise(Axis.Y),			dir, 	TeachersDeskBlock::updateRemoveLeft);
	}

	private BlockState updateDestroy(final WorldAccess world, BlockPos pos, final Direction dir, final Direction origDir, final UpdateListener listener) {
		pos = pos.offset(dir);
		final BlockState state = this.getSideBlock(world, pos);
		return listener.run(state, pos, world, origDir) ? state : null;
	}

	private BlockState updatePlace(final ItemPlacementContext ctx, final Direction dir, final Direction origDir, final UpdateListener listener) {
		return this.updateDestroy(ctx.getWorld(), ctx.getBlockPos(), dir, origDir, listener);
	}

	private static boolean updatePlaceRight(final BlockState state, final BlockPos pos, final WorldAccess world, final Direction axis) {
		if (state != null && state.get(TeachersDeskBlock.FACING) == axis) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.RIGHT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.MIDDLE), Block.NOTIFY_ALL);
			}
			else if (type == Type.SINGLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.LEFT), Block.NOTIFY_ALL);
			}
			return true;
		}
		return false;
	}

	private static boolean updatePlaceLeft(final BlockState state, final BlockPos pos, final WorldAccess world, final Direction axis) {
		if (state != null && state.get(TeachersDeskBlock.FACING) == axis) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.LEFT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.MIDDLE), Block.NOTIFY_ALL);
			}
			else if (type == Type.SINGLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.RIGHT), Block.NOTIFY_ALL);
			}
			return true;
		}
		return false;
	}

	private static boolean updateRemoveRight(final BlockState state, final BlockPos pos, final WorldAccess world, final Direction axis) {
		if (state != null && state.get(TeachersDeskBlock.FACING) == axis) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.MIDDLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.RIGHT), Block.NOTIFY_ALL);
			}
			else if (type == Type.LEFT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.SINGLE), Block.NOTIFY_ALL);
			}
			return true;
		}
		return false;
	}

	private static boolean updateRemoveLeft(final BlockState state, final BlockPos pos, final WorldAccess world, final Direction axis) {
		if (state != null && state.get(TeachersDeskBlock.FACING) == axis) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.MIDDLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.LEFT), Block.NOTIFY_ALL);
			}
			else if (type == Type.RIGHT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.SINGLE), Block.NOTIFY_ALL);
			}
			return true;
		}
		return false;
	}

	private BlockState getSideBlock(final WorldAccess world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		return state.getBlock() instanceof TeachersDeskBlock ? state : null;
	}

	private interface UpdateListener {
		boolean run(BlockState state, BlockPos pos, WorldAccess world, Direction axis);
	}

	public enum Type implements StringIdentifiable
	{
		SINGLE("single"),
		LEFT("left"),
		RIGHT("right"),
		MIDDLE("middle");

		private final String id;

		Type(final String id)
		{
			this.id = id;
		}

		@Override
		public String toString()
		{
			return this.id;
		}

		@Override
		public String asString() {
			return this.toString();
		}
	}

	public enum MaterialType
	{
		OAK,
		BIRCH,
		SPRUCE,
		JUNGLE,
		ACACIA,
		DARK_OAK,
		CRIMSON,
		WARPED,
		MANGROVE,
		STONE,
		GRANITE,
		DIORITE,
		ANDESITE,
		STRIPPED_OAK,
		STRIPPED_BIRCH,
		STRIPPED_SPRUCE,
		STRIPPED_JUNGLE,
		STRIPPED_ACACIA,
		STRIPPED_DARK_OAK,
		STRIPPED_CRIMSON,
		STRIPPED_WARPED,
		STRIPPED_MANGROVE
	}
}
