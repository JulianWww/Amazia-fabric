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
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TeachersDeskBlock extends HorizontalFacingBlock {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

	public TeachersDeskBlock(final Settings settings) {
		super(settings);
	}

	private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

	@Override
	public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos, final ShapeContext context) {
		return TeachersDeskBlock.SHAPE;
	}

	/*@Nullable
	@Override
	public BlockState getPlacementState(final ItemPlacementContext ctx) {
		return this.getDefaultState().with(TeachersDeskBlock.FACING, ctx.getPlayerFacing().getOpposite());
	}*/

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
		Type type = Type.SINGLE;
		final Direction direction = ctx.getPlayerFacing();

		final BlockState right = this.updatePlace(ctx, 	direction.rotateCounterclockwise(Axis.Y), 	TeachersDeskBlock::updatePlaceRight);
		final BlockState left = this.updatePlace(ctx, 	direction.rotateClockwise(Axis.Y), 			TeachersDeskBlock::updatePlaceLeft);

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
		this.updateDestroy(world, pos, dir.rotateCounterclockwise(Axis.Y), 	TeachersDeskBlock::updateRemoveRight);
		this.updateDestroy(world, pos, dir.rotateClockwise(Axis.Y), 		TeachersDeskBlock::updateRemoveLeft);
	}

	private BlockState updateDestroy(final WorldAccess world, BlockPos pos, final Direction dir, final UpdateListener listener) {
		pos = pos.offset(dir);
		final BlockState state = this.getSideBlock(world, pos);
		listener.run(state, pos, world);
		return state;
	}

	private BlockState updatePlace(final ItemPlacementContext ctx, final Direction dir, final UpdateListener listener) {
		return this.updateDestroy(ctx.getWorld(), ctx.getBlockPos(), dir, listener);
	}

	private static void updatePlaceRight(final BlockState state, final BlockPos pos, final WorldAccess world) {
		if (state != null) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.RIGHT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.MIDDLE), Block.NOTIFY_ALL);
			}
			else if (type == Type.SINGLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.LEFT), Block.NOTIFY_ALL);
			}
		}
	}

	private static void updatePlaceLeft(final BlockState state, final BlockPos pos, final WorldAccess world) {
		if (state != null) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.LEFT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.MIDDLE), Block.NOTIFY_ALL);
			}
			else if (type == Type.SINGLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.RIGHT), Block.NOTIFY_ALL);
			}
		}
	}

	private static void updateRemoveRight(final BlockState state, final BlockPos pos, final WorldAccess world) {
		if (state != null) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.MIDDLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.RIGHT), Block.NOTIFY_ALL);
			}
			else if (type == Type.LEFT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.SINGLE), Block.NOTIFY_ALL);
			}
		}
	}

	private static void updateRemoveLeft(final BlockState state, final BlockPos pos, final WorldAccess world) {
		if (state != null) {
			final Type type = state.get(TeachersDeskBlock.TYPE);
			if (type == Type.MIDDLE) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.LEFT), Block.NOTIFY_ALL);
			}
			else if (type == Type.RIGHT) {
				world.setBlockState(pos, state.with(TeachersDeskBlock.TYPE, Type.SINGLE), Block.NOTIFY_ALL);
			}
		}
	}

	private BlockState getSideBlock(final WorldAccess world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		return state.isOf(this.asBlock()) ? state : null;
	}

	private interface UpdateListener {
		void run(BlockState state, BlockPos pos, WorldAccess world);
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
