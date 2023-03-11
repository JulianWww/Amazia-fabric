package net.denanu.amazia.entities.village.server.goal;

import javax.annotation.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.controll.AmaziaEntityMoveControl;
import net.denanu.amazia.entities.village.server.goal.BaseAmaziaGoToBlockGoal.BasePathingAmaziaVillagerEntity;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.hunger.IAmaziaFoodConsumerEntity;
import net.denanu.amazia.pathing.PathingPath;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class BaseAmaziaGoToBlockGoal<E extends BasePathingAmaziaVillagerEntity> extends BaseAmaziaVillageGoal<E> {
	public static final int BASE_FOOD_USAGE = 200;
	protected BlockPos targetPos;
	private boolean isRunning;
	protected boolean reached;
	private PathingPath path;
	private EntityNavigation nav;
	private float speed = 1.0f;
	private int ticksStanding;
	private boolean directMovement;
	private final int foodUsage;
	private int currentX, currentZ = 0;

	public BaseAmaziaGoToBlockGoal(final E e, final int priority) {
		this(e, priority, BaseAmaziaGoToBlockGoal.BASE_FOOD_USAGE, 1.0f);
	}

	public BaseAmaziaGoToBlockGoal(final E e, final int priority, final int foodUsage) {
		this(e, priority, foodUsage, 1.0f);
	}
	public BaseAmaziaGoToBlockGoal(final E e, final int priority, final int foodUsage, final float speed) {
		super(e, priority);
		this.speed = speed;
		this.foodUsage = foodUsage;
	}

	@Override
	public void start() {
		Amazia.LOGGER.info("pathing for" + this.toString());
		super.start();
		this.isRunning = true;
		this.reached = false;
		this.ticksStanding = 0;
		this.directMovement = false;
		this.recalcPath();
		this.entity.getNavigation().setSpeed(this.speed);
	}

	@Override
	public void stop() {
		super.stop();
		this.isRunning = false;
		this.targetPos = null;
		this.entity.getNavigation().setSpeed(1.0f);
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

	protected boolean subShouldContinue() {
		return this.targetPos != null && !this.reached && (this.path != null && this.path.getLength() > 1 || this.directMovement) && super.shouldContinue();
	}

	@Override
	public boolean shouldContinue() {
		return super.shouldContinue() && this.subShouldContinue();
	}

	@Override
	public void tick() {
		this.entity.getNavigation().setSpeed(this.speed);
		this.entity.getMoveControl().setSpeed(this.speed);
		final Vec3d targetPos = new Vec3d(this.targetPos.getX() + 0.5, this.targetPos.getY(), this.targetPos.getZ() + 0.5);
		final BlockPos currentBlockPos = this.entity.getBlockPos();
		if (currentBlockPos.getX() == this.currentX && currentBlockPos.getZ() == this.currentZ) {
			this.ticksStanding++;
			if (this.nav.isIdle()) {
				this.nav.startMovingAlong(this.path, this.speed);
			}
			if (this.ticksStanding > 30 && this.entity.isOnGround()) {
				this.entity.jump();
			}
		}
		else {
			this.currentZ = currentBlockPos.getZ();
			this.currentX = currentBlockPos.getX();
			this.ticksStanding = 0;
		}
		final double distance = BaseAmaziaGoToBlockGoal.getSquareDistance(targetPos, this.entity.getPos());
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
		ActivityFoodConsumerMap.walkUseFood(this.entity, this.foodUsage);
	}

	private static double getSquareDistance(final Vec3d pos, final Vec3d loc) {
		return Math.max(
				Math.max(
						Math.abs(pos.getX() - loc.getX()),
						Math.abs(pos.getZ() - loc.getZ())
						),
				Math.floor(Math.abs(pos.getY() - loc.getY())));
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
			return;
		}

		/*for (int i = 0; i < this.path.getLength(); i++) {
			final PathNode node = this.path.getNode(i);
			final var stand = new ArmorStandEntity(this.entity.getWorld(), node.x + 0.5, node.y + 0.5, node.z + 0.5);
			((ServerWorld)this.entity.getWorld()).spawnEntity(stand);
		}*/

		this.nav.startMovingAlong(this.path, 1);
		this.ticksStanding = 0;
	}

	protected void fail() {}

	private void getNewTargetPos() {
		if (this.targetPos == null) {
			this.targetPos = this.getTargetBlock();
		}
	}

	@Nullable
	protected abstract BlockPos getTargetBlock();

	public boolean isRunning() {
		return this.isRunning;
	}

	public interface BasePathingAmaziaVillagerEntity extends BaseAmaziaVillagerEntity, IAmaziaFoodConsumerEntity {

		EntityNavigation getNavigation();

		boolean isOnGround();

		void jump();

		Vec3d getPos();
		BlockPos getBlockPos();

		AmaziaEntityMoveControl getMoveControl();
		World getWorld();

	}
}
