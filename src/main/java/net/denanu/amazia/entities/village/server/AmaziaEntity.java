package net.denanu.amazia.entities.village.server;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.InitializationException;

import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.entities.AmaziaEntityAttributes;
import net.denanu.amazia.entities.moods.ServerMoodEmiter;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.denanu.amazia.entities.village.server.controll.AmaziaEntityMoveControl;
import net.denanu.amazia.pathing.PathFinder;
import net.denanu.amazia.utils.CuboidSampler;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmaziaEntity extends PassiveEntity {
	protected Village village;
	protected BlockPos villageCorePos;
	private CuboidSampler villageSampler;
	private final int SCAN_ATTEMTS = 10;

	private int currentlyRunnginGoal = -1;

	protected AmaziaEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		this.cannotDespawn();
		this.moveControl = new AmaziaEntityMoveControl(this);
	}

	@Override
	public AmaziaEntityMoveControl getMoveControl() {
		return (AmaziaEntityMoveControl)this.moveControl;
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
		return MobEntity.createMobAttributes()
				.add(AmaziaEntityAttributes.MAX_HUNGER, 20f);
	}

	@Deprecated
	public boolean getCanUpdate() {
		return true;
	}

	@Override
	public boolean shouldSwimInFluids() { return true; }

	private void setup() {
		this.villageSampler = new CuboidSampler(this.getPos(), 4, 4, 4);
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("villageLoc", NbtUtils.toNbt(this.villageCorePos));
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.villageCorePos = NbtUtils.toBlockPos(nbt.getList("villageLoc", NbtElement.INT_TYPE));
		this.setup();
	}

	@Override
	public PassiveEntity createChild(final ServerWorld var1, final PassiveEntity var2) {
		return null;
	}

	//Server only
	public void emmitMood(final VillagerMoods mood) {
		ServerMoodEmiter.sendMood(this, mood);
	}

	protected void setVillage() {};

	private void scanForVillage(final BlockPos pos) {
		final BlockState state =  this.world.getBlockState(pos);
		if (state.getBlock().equals(AmaziaBlocks.VILLAGE_CORE)) {
			this.villageCorePos = pos;
			final BlockEntity entity = this.world.getBlockEntity(pos);
			if (!(entity instanceof final VillageCoreBlockEntity core)) {
				throw new InitializationException("Village Core Block at " + pos.toString() + " missing block entity");
			}
			this.village = core.getVillage();
			this.setVillage();
			this.navigation = new PathFinder(this);
			//this.moveControl = new AmaziaMoveControll(this);
			this.world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
		}
	}
	private void scanForVillage() {
		this.villageSampler.setPos(this.getPos());
		for (int idx = 0; idx < this.SCAN_ATTEMTS; idx++) {
			this.scanForVillage(this.villageSampler.getPos());
		}
	}

	private void discoverVillage() {
		if (this.villageCorePos!= null)  {
			this.scanForVillage(this.villageCorePos);
		}
		this.scanForVillage();
	}


	public boolean hasVillage() {
		return this.village != null;
	}
	public Village getVillage() {
		return this.village;
	}

	@Override
	public void mobTick() {
		this.update();
		super.mobTick();
	}

	@Override
	public void tickMovement() {
		if (this.hasVillage() && !this.getVillage().isInVillage(this)) {
			final BlockPos pos = this.getBlockPos();
			if (!this.subReneterVillage(pos.west(2)) && !this.subReneterVillage(pos.east(2)) && !this.subReneterVillage(pos.north(2)) && !this.subReneterVillage(pos.south(2))) {
				this.teleport(
						this.getVillage().getOrigin().getX(),
						this.getVillage().getOrigin().getY() + 1,
						this.getVillage().getOrigin().getZ());
			};
		}
		super.tickMovement();
	}

	private boolean subReneterVillage(final BlockPos pos) {
		if (this.getVillage().isInVillage(pos)) {
			this.getMoveControl().emergencyMoveTo(pos.getX(), pos.getY(), pos.getX(), Math.max(this.getMoveControl().getSpeed(), 1.0));
			return true;
		}
		return false;
	}

	protected void update() {
		if (this.village == null) { this.discoverVillage(); return; }
	}

	@Override
	public void handleStatus(final byte status) {
		if (status == EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES) {
			this.produceParticles(ParticleTypes.HAPPY_VILLAGER);
		} else {
			super.handleStatus(status);
		}
	}

	protected void produceParticles(final ParticleEffect parameters) {
		for (int i = 0; i < 5; ++i) {
			final double d = this.random.nextGaussian() * 0.02;
			final double e = this.random.nextGaussian() * 0.02;
			final double f = this.random.nextGaussian() * 0.02;
			this.world.addParticle(parameters, this.getParticleX(1.0), this.getRandomBodyY() + 1.0, this.getParticleZ(1.0), d, e, f);
		}
	}

	public int getCurrentlyRunnginGoal() {
		return this.currentlyRunnginGoal;
	}

	public boolean canStartGoal(final int priority) {
		return this.currentlyRunnginGoal == -1 || this.currentlyRunnginGoal >= priority;
	}

	public void endCurrentlyRunningGoal(final int priority) {
		if (this.currentlyRunnginGoal == priority) {
			this.currentlyRunnginGoal = -1;
		}
	}

	public void setCurrentlyRunnginGoal(final int newGoal) {
		if ((this.currentlyRunnginGoal == -1 || this.currentlyRunnginGoal > newGoal) && newGoal != -2) {
			this.currentlyRunnginGoal = newGoal;
		}
	}

	@Override
	public boolean damage(final DamageSource source, final float amount) {
		if (this.hasVillage()) {
			this.getVillage().getGuarding().addOpponent(source.getAttacker(), 10);
		}
		return super.damage(source, amount);
	}
}
