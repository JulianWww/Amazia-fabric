package net.denanu.amazia.entities.village.merchant;

import javax.annotation.Nullable;

import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.entities.merchants.AmaziaMerchant;
import net.denanu.amazia.entities.village.merchant.goal.GoToVillageCore;
import net.denanu.amazia.entities.village.server.controll.AmaziaEntityMoveControl;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal.PathingAmaziaVillagerEntity;
import net.denanu.amazia.pathing.PathFinder;
import net.denanu.amazia.pathing.PathFinder.AmaziaPathfinderEntity;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.scedule.BaseVillagerScedule;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmaziaVillageMerchant extends AmaziaMerchant implements PathingAmaziaVillagerEntity, AmaziaPathfinderEntity {
	@Nullable
	private Village village;
	private MovementPhases move_phase = MovementPhases.ARRIVING;
	private BlockPos origin;

	public AmaziaVillageMerchant(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		this.village = null;
		this.init();
	}

	public static AmaziaVillageMerchant of(final World world, final Village village, final BlockPos pos) {
		final AmaziaVillageMerchant merchant = new AmaziaVillageMerchant(AmaziaEntities.VILLAGE_MERCHANT, world);
		merchant.setVillage(village);
		merchant.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		merchant.origin = pos;
		return merchant;
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		this.goalSelector.add(6, new GoToVillageCore(this, 6));
		this.setCustomName(Text.literal(NamingLanguageOptions.generateName(null)));
		this.setCustomNameVisible(false);
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("MovementPhase", this.move_phase.ordinal());
		nbt.put("Origin", NbtUtils.toNbt(this.origin));
		if (this.hasVillage()) {
			nbt.put("Village", NbtUtils.toNbt(this.village.getOrigin()));
		}
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.move_phase = MovementPhases.values()[nbt.getInt("MovementPhase")];
		this.origin = NbtUtils.toBlockPos(nbt.getList("Origin", NbtElement.INT_TYPE));
		if (nbt.contains("Village") && this.world.getBlockEntity( NbtUtils.toBlockPos(nbt.getList("Village", NbtElement.INT_TYPE))) instanceof final VillageCoreBlockEntity core) {
			this.setVillage(core.getVillage());
		}
	}

	private void setVillage(final Village village) {
		this.village = village;
		village.setMerchant(this);
	}

	@Override
	public boolean canWander() {
		return this.move_phase == MovementPhases.WANDERING;
	}

	@Override
	public boolean cannotDespawn() {
		return this.hasVillage() && this.village.getMerchant() == this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init() {
		this.moveControl = new AmaziaEntityMoveControl(this);
		this.navigation = new PathFinder(this);
	}

	@Override
	public Village getVillage() {
		return this.village;
	}

	@Override
	public AmaziaEntityMoveControl getMoveControl() {
		return (AmaziaEntityMoveControl)this.moveControl;
	}

	@Override
	public void setCurrentlyRunnginGoal(final int priority) {
	}

	@Override
	public boolean canStartGoal(final int priority) {
		return true;
	}

	@Override
	public boolean hasVillage() {
		return this.village != null;
	}

	@Override
	public BaseVillagerScedule getActivityScedule() {
		return new BaseVillagerScedule();
	}

	@Override
	public void endCurrentlyRunningGoal(final int priority) {
	}

	@Override
	public void reduceFood(final float amount) {
	}

	@Override
	public void eatFood(final float amount) {
	}

	@Override
	public float getHunger() {
		return 100;
	}

	@Override
	public void setHunger(final float value) {
	}

	@Override
	public void consumeNutrishousItem() {
	}

	@Override
	public boolean hasOrRequestFood() {
		return false;
	}

	@Override
	public boolean isDeposeting() {
		return false;
	}

	@Override
	public void remove(final RemovalReason reason) {
		super.remove(reason);
		if (this.hasVillage()) {
			this.village.setMerchant(null);
		}
	}

	public void reachCore(final MovementPhases action) {
		if (action == this.move_phase) {
			if (this.move_phase == MovementPhases.ARRIVING) {
				this.move_phase = MovementPhases.WANDERING;
			}
			else {
				this.discard();
			}
		}
	}

	public BlockPos getOrigin() {
		return this.origin;
	}

	public void leave() {
		this.move_phase = MovementPhases.LEAVING;
	}

	public MovementPhases getMove_phase() {
		return this.move_phase;
	}

	public enum MovementPhases {
		ARRIVING,
		WANDERING,
		LEAVING;
	}
}
