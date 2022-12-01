package net.denanu.amazia.village.structures;

import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.mixin.MinecraftServerWorldAccessor;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class MineStructure extends VillageStructure {
	private Direction direction;

	private boolean isEnd;
	private boolean hasVillager;
	private Box box;

	public MineStructure(final BlockPos _center, final Village v, final NbtCompound nbt) {
		this(_center, v, (Direction)null);
		this.readNbt(nbt);
	}

	public MineStructure(final BlockPos _center, final Village v, final Direction _direction) {
		super(_center.down(2), v);
		this.direction = _direction;
		this.hasVillager = false;
		this.isEnd = false;
		this.genBox();
	}

	private void genBox() {
		if (this.direction != null) {
			this.box = new Box(this.getMainEntrance().offset(this.direction, -1), this.getEndPos());
		}
	}

	private BlockPos getEndPos() {
		return switch (this.direction) {
		case NORTH -> {
			yield new BlockPos(this.getX(), this.getY() + 1, this.getVillage().getOrigin().getZ() - Village.getSize());
		}
		case SOUTH -> {
			yield new BlockPos(this.getX(), this.getY() + 1, this.getVillage().getOrigin().getZ() + Village.getSize());
		}
		case EAST -> {
			yield new BlockPos(this.getVillage().getOrigin().getX() - Village.getSize(), this.getY() + 1, this.getZ());
		}
		case WEST -> {
			yield new BlockPos(this.getVillage().getOrigin().getX() + Village.getSize(), this.getY() + 1, this.getZ());
		}
		default -> {
			throw new RuntimeException("Illedgal direction " + this.direction);
		}
		};
	}

	public Box getBox() {
		return this.box;
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.putBoolean("isEnd", this.isEnd);
		return nbt;
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		this.isEnd = nbt.getBoolean("isEnd");
	}

	private static boolean isInFront(final int pos, final int origin, final int direction) {
		return direction * pos <= origin * direction;
	}

	private boolean isWithinHeightRange(final int height) {
		return height >= this.getY() && height < this.getY() + 2;
	}

	@Override
	public boolean isIn(final BlockPos pos) {
		if (this.direction.getOffsetX() != 0) {
			return MineStructure.isInFront(pos.getX(), this.getCenter().getX(), this.direction.getOffsetX()) && this.isWithinHeightRange(pos.getY()) && this.getZ() == pos.getZ() && this.isValid() && !this.isEnd;
		}
		if (this.direction.getOffsetZ() != 0) {
			return MineStructure.isInFront(pos.getZ(), this.getCenter().getZ(), this.direction.getOffsetZ()) && this.isWithinHeightRange(pos.getY()) && this.getX() == pos.getX() && this.isValid() && !this.isEnd;
		}
		return false;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public BlockPos getMainEntrance() {
		return this.getCenter();
	}

	public void releaseVillager() {
		this.hasVillager = false;
	}

	public void registerVillager() {
		this.hasVillager = true;
	}

	public boolean hasVillager() {
		return this.hasVillager;
	}

	protected static boolean isEndMineBlock(final World world, final BlockPos pos) {
		return world.getBlockState(pos).getMaterial().blocksMovement();
	}
	protected static boolean isInvalidFloorBlock(final World world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		return !state.getMaterial().blocksMovement();
	}
	protected static boolean isDangerous(final World world, final BlockPos pos) {
		final FluidState state = world.getFluidState(pos);
		return !state.isEmpty();
	}

	public static boolean isSafe(final LivingEntity entity, final BlockPos pos) {
		final World world = entity.getWorld();
		return pos.isWithinDistance(entity.getPos(), 5) && !MineStructure.isDangerous(world, pos.up()) && !MineStructure.isDangerous(world, pos.east()) && !MineStructure.isDangerous(world, pos.west()) && !MineStructure.isDangerous(world, pos.north()) && !MineStructure.isDangerous(world, pos.south());
	}

	public boolean isAtEnd(final AmaziaEntity entity) {
		final BlockPos pos = new BlockPos(entity.getPos()).offset(this.direction.getOpposite());
		if (!entity.getVillage().isInVillage(pos) && this.isIn(entity.getBlockPos())) {
			this.endMine();
		}
		return MineStructure.isEndMineBlock(entity.getWorld(), pos) ||
				MineStructure.isEndMineBlock(entity.getWorld(), pos.up()) ||
				this.getToFixPos(entity) != null ||
				this.isEnd;
	}

	public BlockPos getToFixPos(final LivingEntity entity) {
		final BlockPos pos = new BlockPos(entity.getPos()).offset(this.direction.getOpposite());
		Direction rotated = this.direction.rotateClockwise(Axis.Y);

		if (MineStructure.isInvalidFloorBlock(entity.getWorld(), pos.down())) 								{ return pos.down(); }
		if (MineStructure.isInvalidFloorBlock(entity.getWorld(), pos.up(2))) 								{ return pos.up(2); }
		if (MineStructure.isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated))) 						{ return pos.offset(rotated); }
		if (MineStructure.isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated).up())) 				{ return pos.offset(rotated).up(); }
		rotated = rotated.getOpposite();
		if (MineStructure.isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated))) 						{ return pos.offset(rotated); }
		if (MineStructure.isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated).up())) 				{ return pos.offset(rotated).up(); }

		if (MineStructure.isDangerous(entity.getWorld(), pos.offset(this.direction.getOpposite()).up())) 	{ return pos.offset(this.direction.getOpposite()).up(); }
		if (MineStructure.isDangerous(entity.getWorld(), pos.offset(this.direction.getOpposite()))) 		{ return pos.offset(this.direction.getOpposite()); }
		return null;
	}

	public BlockPos getBlockToExtend(final MinerEntity entity) {
		final BlockPos pos = new BlockPos(entity.getPos()).offset(this.direction.getOpposite());
		if (MineStructure.isEndMineBlock(entity.getWorld(), pos.up())) 	{ return pos.up(); }
		if (MineStructure.isEndMineBlock(entity.getWorld(), pos)) 		{ return pos; }
		return null;
	}

	public void setDirection(final Direction d) {
		if (this.direction == null) {
			this.direction = d;
			this.genBox();
		}
	}

	public boolean getIsEnd() {
		return this.isEnd;
	}
	public void endMine () {
		this.isEnd = true;
	}
	public void resetMine() {
		this.isEnd = false;
	}

	public boolean isEmpty(final ServerWorld world, final Entity exception) {
		final BooleanContainer out = new BooleanContainer();
		((MinecraftServerWorldAccessor)world).getEntityManager().getLookup().forEachIntersects(this.box.expand(1, 1, 1), val -> {
			if ((val instanceof AmaziaEntity || val instanceof PlayerEntity) && val != exception) {
				out.set(false);
			}
		});
		return out.get();
	}

	protected class BooleanContainer{
		boolean val;
		public BooleanContainer() {
			this.val = true;
		}
		public boolean get() {
			return this.val;
		}
		public void set(final boolean val) {
			this.val = val;
		}
	}
}
