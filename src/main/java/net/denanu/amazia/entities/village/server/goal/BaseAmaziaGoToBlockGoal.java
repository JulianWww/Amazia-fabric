package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.pathing.PathingPath;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class BaseAmaziaGoToBlockGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaVillageGoal<E> {
	private BlockPos targetPos;
	private boolean isRunning;
	protected boolean reached;
	private PathingPath path;
	private EntityNavigation nav;
	private int ticksStanding;
	private boolean directMovement;

	public BaseAmaziaGoToBlockGoal(final E e, final int priority) {
		super(e, priority);
	}

	@Override
	public void start() {
		super.start();
		this.isRunning = true;
		this.reached = false;
		this.ticksStanding = 0;
		this.directMovement = false;
		this.recalcPath();
	}

	@Override
	public void stop() {
		super.stop();
		this.isRunning = false;
		this.targetPos = null;
		this.entity.getNavigation().stop();
	}

	@Override
	public boolean canStart() {
		if (super.canStart()) {
			this.getNewTargetPos();
			return this.targetPos != null;
		}
		return false;
	}

	@Override
	public boolean shouldContinue() {
		return super.shouldContinue() && (this.path != null || this.directMovement) && this.targetPos != null && !this.reached && this.path.getLength() > 1;
	}

	@Override
	public void tick() {
		final Vec3d targetPos = new Vec3d(this.targetPos.getX() + 0.5, this.targetPos.getY(), this.targetPos.getZ() + 0.5);
		if (this.nav.isIdle()) {
			this.ticksStanding++;
		}
		else {
			this.ticksStanding = 0;
		}
		final double distance = targetPos.squaredDistanceTo(this.entity.getPos());
		if (distance > this.getDesiredDistanceToTarget()) {
			this.reached = false;
			this.directMovement = false;
			if (distance < 4) {
				this.runBackupMotion();
				this.directMovement = true;
				if (this.shouldResetPath()) {
					this.recalcPath();
				}
			} else if (this.shouldResetPath()) {
				this.recalcPath();
			}
		}
		else {
			this.reached = true;
		}
	}

	private void runBackupMotion() {
		this.entity.getNavigation().stop();
		this.entity.getMoveControl().moveTo( this.targetPos.getX() + 0.5,  this.targetPos.getY() , this.targetPos.getZ() + 0.5, 1);
	}

	public boolean shouldResetPath() {
		return this.ticksStanding > 40;
	}

	public double getDesiredDistanceToTarget() {
		return 0.2; // note distance squared
	}

	protected void recalcPath() {
		this.nav =  this.entity.getNavigation();
		this.path = (PathingPath) this.nav.findPathTo(this.targetPos, 0);
		if (this.path != null && this.path.getLength() == 1) {
			this.entity.getMoveControl().moveTo( this.targetPos.getX() + 0.5,  this.targetPos.getY() + 1, this.targetPos.getZ() + 0.5, 1);
		}
		if (this.path == null && !this.directMovement) {
			this.fail();
		}
		this.nav.startMovingAlong(this.path, 1);
		this.ticksStanding = 0;
	}

	protected void fail() {};

	private void getNewTargetPos() {
		if (this.targetPos == null) {
			this.targetPos = this.getTargetBlock();
			return;
		}
	}

	protected abstract BlockPos getTargetBlock();

	public boolean isRunning() {
		return this.isRunning;
	}
}
