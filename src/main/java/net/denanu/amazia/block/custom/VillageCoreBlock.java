package net.denanu.amazia.block.custom;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.block.entity.AmaziaBlockEntities;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class VillageCoreBlock extends BlockWithEntity implements BlockEntityProvider {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

	public VillageCoreBlock(final Settings settings) {
		super(settings);
	}

	private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 10, 16);

	@Override
	public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos, final ShapeContext context) {
		return VillageCoreBlock.SHAPE;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(final ItemPlacementContext ctx) {
		return this.getDefaultState().with(VillageCoreBlock.FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public BlockState rotate(final BlockState state, final BlockRotation rotation) {
		return state.with(VillageCoreBlock.FACING, rotation.rotate(state.get(VillageCoreBlock.FACING)));
	}

	@Override
	public BlockState mirror(final BlockState state, final BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(VillageCoreBlock.FACING)));
	}

	@Override
	protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
		builder.add(VillageCoreBlock.FACING);
	}

	// BLOCK ENTITY

	@Override
	public BlockRenderType getRenderType(final BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockEntity createBlockEntity(final BlockPos pos, final BlockState state) {
		return new VillageCoreBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final World world, final BlockState state, final BlockEntityType<T> type) {
		return BlockWithEntity.checkType(type, AmaziaBlockEntities.VILLAGE_CORE, VillageCoreBlockEntity::tick);
	}

	@Override
	public ActionResult onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		final BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof final VillageCoreBlockEntity core) {
			player.openHandledScreen(core);
		}
		return ActionResult.CONSUME;
	}
}
