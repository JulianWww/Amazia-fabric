package net.denanu.amazia.entities.furnature;

import java.util.List;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.AmaziaEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SeatEntity extends Entity {

	public SeatEntity(final EntityType<?> type, final World world) {
		super(type, world);
		this.setNoGravity(true);
	}

	private SeatEntity(final World world, final BlockPos source, final double yOffset, final Direction direction) {
		super(AmaziaEntities.SEAT, world);
		this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
		this.setRotation(direction.getOpposite().asRotation(), 0F);
	}

	public static ActionResult create(final World world, final BlockPos pos, final double yOffset, final LivingEntity entity, final Direction direction)
	{
		if(!world.isClient)
		{
			final List<SeatEntity> seats = world.getEntitiesByClass(SeatEntity.class, new Box(pos.getX() - 5, pos.getY() - 5, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5), a -> {
				final BlockPos p = pos.up();
				Amazia.LOGGER.info(a.getBlockPos().toString() + " : " + p.toString() + " : " + Boolean.toString(a.getBlockPos().equals(p)));
				return a.getBlockPos().equals(p);
			});
			if(seats.isEmpty())
			{
				entity.dismountVehicle();
				final SeatEntity seat = new SeatEntity(world, pos, yOffset, direction);
				world.spawnEntity(seat);
				entity.startRiding(seat);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void readCustomDataFromNbt(final NbtCompound var1) {
	}

	@Override
	protected void writeCustomDataToNbt(final NbtCompound var1) {
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	protected void addPassenger(final Entity entity)
	{
		super.addPassenger(entity);
		entity.setYaw(this.getYaw());
		this.clampYaw(entity);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isClient && this.getPassengerList().isEmpty()) {
			this.discard();
		}
	}

	private void clampYaw(final Entity passenger)
	{
		passenger.setBodyYaw(this.getYaw());
		final float wrappedYaw = MathHelper.wrapDegrees(passenger.getYaw() - this.getYaw());
		final float clampedYaw = MathHelper.clamp(wrappedYaw, -120.0F, 120.0F);
		passenger.prevYaw += clampedYaw - wrappedYaw;
		passenger.setYaw(passenger.getYaw() + clampedYaw - wrappedYaw);
		passenger.setHeadYaw(passenger.getYaw());
	}

	@Override
	public void removePassenger(final Entity passenger) {
		super.removePassenger(passenger);
		if (this.getPassengerList().isEmpty()) {
			//this.discard();
		}
	}

	@Override
	public Vec3d updatePassengerForDismount(final LivingEntity passenger) {
		final Direction direction = this.getHorizontalFacing();
		return this.getPos().add(direction.getOffsetX() * 0.1, -0.2, direction.getOffsetY() * 0.1);
	}
}
