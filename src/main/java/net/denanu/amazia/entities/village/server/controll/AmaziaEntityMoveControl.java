package net.denanu.amazia.entities.village.server.controll;

import net.denanu.amazia.JJUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class AmaziaEntityMoveControl extends MoveControl {
	private int dogeCounter = 0;

	public AmaziaEntityMoveControl(final MobEntity entity) {
		super(entity);
	}

	public void strafeTo(final float forward, final float sideways, final float speed) {
		if (!this.isDodging()) {
			this.state = MoveControl.State.STRAFE;
			this.forwardMovement = forward;
			this.sidewaysMovement = sideways;
			this.speed = speed;
		}
	}
	@Override
	public void moveTo(final double x, final double y, final double z, final double speed) {
		if (!this.isDodging()) {
			super.moveTo(x, y, z, speed);
		}
	}

	public void stop() {
		this.forwardMovement = 0;
		this.sidewaysMovement = 0;

		this.entity.setForwardSpeed(0f);
		this.entity.setSidewaysSpeed(0f);
	}

	public boolean canDodge() {
		return this.dogeCounter == 0;
	}

	public boolean isDodging() {
		return this.dogeCounter > 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isDodging()) {
			this.dogeCounter--;
		}
	}

	public void doge(final LivingEntity entity, final float speed) {
		Vec3d delta = entity.getPos().subtract(this.entity.getPos());
		delta = delta.crossProduct(JJUtils.UP).normalize().multiply(this.entity.getRandom().nextBoolean() ? 2 : -2);

		this.moveTo(delta.x, delta.y, delta.z, speed);

		this.dogeCounter = 2;
	}


	public void emergencyMoveTo(final double x, final double y, final double z, final double speed) {
		super.moveTo(x, y, z, speed);
		this.dogeCounter = 10;
	}
}
