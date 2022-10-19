package net.denanu.amazia.village.structures;

import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class MineStructure extends VillageStructure {
	private Direction direction;
	
	private boolean isValid;
	private boolean isEnd;
	private boolean hasVillager;
	
	public MineStructure(BlockPos _center, Village v, NbtCompound nbt, String name) {
		this(_center, v, null);
		this.readNbt(nbt, name);
	}
	
	public MineStructure(BlockPos _center, Village v, Direction _direction) {
		super(_center.down(2), v);
		this.direction = _direction;
		this.hasVillager = false;
		this.isValid = true;
		this.isEnd = false;
	}
	
	private String getName() {
		return this.getX() + "." + this.getY() + "." +this.getZ();
	}
	
	@Override
	public void writeNbt(NbtCompound nbt, String name) {
		nbt.putBoolean(name + ".Mine." + this.getName() + ".isEnd", this.isEnd);
	}

	@Override
	public void readNbt(NbtCompound nbt, String name) {
		this.isEnd = nbt.getBoolean(name + ".Mine." + this.getName() + ".isEnd");
	}
	
	private static boolean isInFront(int pos, int origin, int direction) {
		return direction * pos <= origin * direction;
	}
	
	private boolean isWithinHeightRange(int height) {
		return height >= this.getY() && height < this.getY() + 2;
	}

	@Override
	public boolean isIn(BlockPos pos) {
		if (this.direction.getOffsetX() != 0) {
			return isInFront(pos.getX(), this.getCenter().getX(), this.direction.getOffsetX()) && this.isWithinHeightRange(pos.getY()) && this.getZ() == pos.getZ() && this.isValid && !this.isEnd;
		}
		if (this.direction.getOffsetZ() != 0) {
			return isInFront(pos.getZ(), this.getCenter().getZ(), this.direction.getOffsetZ()) && this.isWithinHeightRange(pos.getY()) && this.getX() == pos.getX() && this.isValid && !this.isEnd;
		}
		return false;
	}
	
	public Direction getDirection() {
		return direction;
	}

	@Override
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
		return hasVillager;
	}
	
	protected static boolean isEndMineBlock(World world, BlockPos pos) {
		return world.getBlockState(pos).getMaterial().blocksMovement();
	}
	protected static boolean isInvalidFloorBlock(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return !state.getMaterial().blocksMovement();
	}
	protected static boolean isDangerous(World world, BlockPos pos) {
		FluidState state = world.getFluidState(pos);
		return !state.isEmpty();
	}
	
	public static boolean isSafe(LivingEntity entity, BlockPos pos) {
		World world = entity.getWorld();
		return pos.isWithinDistance(entity.getPos(), 5) && !(
				isDangerous(world, pos.up()) ||
				isDangerous(world, pos.east()) ||
				isDangerous(world, pos.west()) ||
				isDangerous(world, pos.north()) ||
				isDangerous(world, pos.south())
			);
	}

	public boolean isAtEnd(AmaziaEntity entity) {
		BlockPos pos = new BlockPos(entity.getPos()).offset(this.direction.getOpposite());
		if (!entity.getVillage().isInVillage(pos) && this.isIn(entity.getBlockPos())) {
			this.endMine();
		}
		return (
				isEndMineBlock(entity.getWorld(), pos) ||
				isEndMineBlock(entity.getWorld(), pos.up()) ||
				this.getToFixPos(entity) != null ||
				this.isEnd
			);
	}

	public BlockPos getToFixPos(LivingEntity entity) {
		BlockPos pos = new BlockPos(entity.getPos()).offset(this.direction.getOpposite());
		Direction rotated = this.direction.rotateClockwise(Axis.Y);
		
		if (isInvalidFloorBlock(entity.getWorld(), pos.down())) 								{ return pos.down(); }
		if (isInvalidFloorBlock(entity.getWorld(), pos.up(2))) 									{ return pos.up(2); }
		if (isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated))) 						{ return pos.offset(rotated); }
		if (isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated).up())) 					{ return pos.offset(rotated).up(); }
		rotated = rotated.getOpposite();
		if (isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated))) 						{ return pos.offset(rotated); }
		if (isInvalidFloorBlock(entity.getWorld(), pos.offset(rotated).up())) 					{ return pos.offset(rotated).up(); }
		
		if (isDangerous(entity.getWorld(), pos.offset(this.direction.getOpposite()).up())) 		{ return pos.offset(this.direction.getOpposite()).up(); }
		if (isDangerous(entity.getWorld(), pos.offset(this.direction.getOpposite()))) 			{ return pos.offset(this.direction.getOpposite()); }
		return null;
	}

	public BlockPos getBlockToExtend(MinerEntity entity) {
		BlockPos pos = new BlockPos(entity.getPos()).offset(this.direction.getOpposite());
		if (isEndMineBlock(entity.getWorld(), pos.up())) 	{ return pos.up(); }
		if (isEndMineBlock(entity.getWorld(), pos)) 		{ return pos; }
		return null;
	}

	public void setDirection(Direction d) {
		if (this.direction == null) {
			this.direction = d;
		}
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void destroy() {
		this.isValid = false;
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
}
