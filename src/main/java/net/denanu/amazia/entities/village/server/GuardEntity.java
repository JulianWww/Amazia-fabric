package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.combat.AttackSensor;
import net.denanu.amazia.entities.village.server.goal.guard.AmaziaBowUser;
import net.denanu.amazia.entities.village.server.goal.guard.GuardMeleeAttackGoal;
import net.denanu.amazia.entities.village.server.goal.guard.LeaveCombatGoal;
import net.denanu.amazia.entities.village.server.goal.guard.VillageGuardActiveTargetGoal;
import net.denanu.amazia.entities.village.server.goal.guard.VillageGuardBowAttackGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.sceduling.opponents.CombatEvaluator;
import net.denanu.amazia.village.sceduling.opponents.ProjectileTargeting;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GuardEntity extends AmaziaVillagerEntity implements IAnimatable, InventoryChangedListener, AmaziaBowUser, AttackSensor {
	public static final ImmutableSet<Item> CRAFTABLES = ImmutableSet.of(Items.WOODEN_SWORD, Items.STICK);

	private final AnimationFactory factory = new AnimationFactory(this);

	private Optional<Integer> bowLocation = Optional.empty();
	private Optional<Integer> swordLocation = Optional.empty();
	private boolean shouldFlee;

	public GuardEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);

		this.getInventory().addListener(this);
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
		return AmaziaVillagerEntity.setAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0);
	}

	private <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
		/*if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.walk", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.idle", true));*/
		return PlayState.CONTINUE;
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("ShouldFlee", this.shouldFlee);
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.shouldFlee = nbt.getBoolean("ShouldFlee");
	}

	@Override
	public void mobTick() {
		super.mobTick();
		if (this.age % 200 == 0) {
			this.getToolIfNotPresant();
		}
	}

	@Override
	public void registerControllers(final AnimationData data) {
		data.addAnimationController(new AnimationController<GuardEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		ItemStack stack;
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			stack = this.getInventory().getStack(idx);
			if (
					!this.wantToKeepItemInSlot(idx) &&
					(!this.hasBow() || this.bowLocation.get() != idx) &&
					(!this.hasSword() || this.swordLocation.get() != idx) &&
					!(stack.getItem() instanceof ArmorItem) &&
					!stack.isEmpty()) {
				return new Triplet<ItemStack, Integer, Integer>(this.getInventory().getStack(idx), idx, this.getInventory().getStack(idx).getCount());
			}
		}
		return null;
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return Amazia.GUARD_CRAFTABLES;
	}

	@Override
	public boolean canDepositItems() {
		return !this.hasFreeSlot();
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1,  new LeaveCombatGoal(this, 1, 100000, 3.0f));
		this.goalSelector.add(12, new VillageGuardBowAttackGoal(this, 2f, 25, 50.0f));
		this.goalSelector.add(13, new GuardMeleeAttackGoal(this, 2.0, true));

		this.targetSelector.add(0, new RevengeGoal(this, PassiveEntity.class));
		this.targetSelector.add(1, new VillageGuardActiveTargetGoal(this, 10));

		this.registerNonCombatBaseGoals();
	}

	@Override
	public void setTarget(@Nullable final LivingEntity target) {
		super.setTarget(target);
		if (target == null) {
			this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
		}
	}

	public void getToolIfNotPresant() {
		this.getSwordIfNotPresant();
		this.getBowIfNotPresant();
		this.getRequestArmorIfNotPresant();
	}

	private void getRequestArmorIfNotPresant() {
		this.requestBetterHelmet();
		this.requestBetterChestplate();
		this.requestBetterLeggins();
		this.requestBetterBoots();
	}

	private void requestBetterChestplate() {
		this.requestBetterArmor(AmaziaData.CHEST_ARMOR, EquipmentSlot.CHEST);
	}
	private void requestBetterLeggins() {
		this.requestBetterArmor(AmaziaData.LEG_ARMOR, EquipmentSlot.LEGS);
	}
	private void requestBetterHelmet() {
		this.requestBetterArmor(AmaziaData.HEAD_ARMOR, EquipmentSlot.HEAD);
	}
	private void requestBetterBoots() {
		this.requestBetterArmor(AmaziaData.FOOT_ARMOR, EquipmentSlot.FEET);
	}
	private void requestBetterArmor(final ArrayList<ArmorItem> options, final EquipmentSlot slot) {
		final ArmorItem item = JJUtils.getRandomListElement(options);
		if (CombatEvaluator.isBetter(this.getEquippedStack(slot).getItem(), item)) {
			this.requestItem(item);
		}
	}

	private void getBowIfNotPresant() {
		if (!this.bowLocation.isPresent()) {
			this.requestItem(Items.BOW);
		}
	}

	private void getSwordIfNotPresant() {
		if (this.swordLocation.isPresent() && this.getInventory().getStack(this.swordLocation.get()).getItem() instanceof final SwordItem sword) {
			this.requestBetterSword(sword);
		}
		this.requestItem(JJUtils.getRandomListElement(AmaziaData.MELEE_WEAPONS));
	}

	private void requestBetterSword(final SwordItem current) {
		final SwordItem next = JJUtils.getRandomListElement(AmaziaData.MELEE_WEAPONS);
		if (CombatEvaluator.isBetter(current, next)) {
			this.requestItem(next);
		}
	}

	@Override
	public void onInventoryChanged(final Inventory inventory) {
		this.bowLocation = Optional.empty();
		this.swordLocation = Optional.empty();

		float swordPriority = -1;
		float temp;
		ItemStack current;
		for (int idx=0; idx < inventory.size(); idx++) {
			current = inventory.getStack(idx);
			if (current.getItem() instanceof final ArmorItem armor) {
				final EquipmentSlot slot = armor.getSlotType();
				current = this.getEquippedStack(slot);
				if (CombatEvaluator.isBetter(current.getItem(), armor)) {
					this.equipStack(slot, inventory.getStack(idx));
				}
			}
			else if (current.isOf(Items.BOW)) {
				this.bowLocation = Optional.of(idx);
			}
			else if ((temp = CombatEvaluator.getWeaponPriority(current, EntityGroup.ILLAGER)) > swordPriority) {
				this.swordLocation = Optional.of(idx);
				swordPriority = temp;
			}
		}
	}
	public void equipWeapon(final LivingEntity target) {
		ItemStack stack = null;
		float priority = -1;
		float tmp;

		for (int idx=0; idx < this.getInventory().size(); idx++) {
			tmp = CombatEvaluator.getWeaponPriority(this.getInventory().getStack(idx), target);
			if (tmp > priority) {
				priority = tmp;
				stack = this.getInventory().getStack(idx);
			}
		}

		this.equipStack(EquipmentSlot.MAINHAND, stack);
	}

	public int getAttackTime() {
		return 5;
	}

	public boolean hasBow() {
		return this.bowLocation.isPresent();
	}

	public boolean hasSword() {
		return this.swordLocation.isPresent();
	}

	public int getBow() {
		return this.bowLocation.get();
	}

	@Override
	public boolean canCraft () {
		return true;
	}

	@Override
	public void shootBow(final LivingEntity target, final float pullProgress) {
		final ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
		final PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);

		final Vec3d v = ProjectileTargeting.getTargeting(target, persistentProjectileEntity, 3.0f);
		if (v==null) {
			return;
		}
		persistentProjectileEntity.setVelocity(v);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
		this.world.spawnEntity(persistentProjectileEntity);
	}
	protected PersistentProjectileEntity createArrowProjectile(final ItemStack arrow, final float damageModifier) {
		return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
	}

	// dodge an attack
	@Override
	public void senceRangedAttack(final LivingEntity entity) {
		this.getMoveControl().doge(entity, 5.0f);
	}

	public void shootBow(final BlockPos pos, final float pullProgress) {
		final ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
		final PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);

		final Vec3d v = ProjectileTargeting.getTargeting(pos, persistentProjectileEntity, 3.0f);
		if (v==null) {
			return;
		}
		persistentProjectileEntity.setOnFireFor(10);
		persistentProjectileEntity.setVelocity(v);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
		this.world.spawnEntity(persistentProjectileEntity);
	}

	@Override
	public boolean damage(final DamageSource source, final float amount) {
		final boolean out = super.damage(source, amount);
		if (!this.world.isClient) {
			this.shouldFlee = this.isLowHp();
		}
		return out;
	}

	public boolean shouldFlee() {
		return this.shouldFlee;
	}

	public void endShouldFlee() {
		this.shouldFlee = false;
	}

	@Override
	public void remove(final RemovalReason reason) {
		super.remove(reason);
		this.exitCombat();
	}

	@Override
	public void enterCombat() {
		if (this.hasVillage()) {
			this.village.getGuarding().addCombatant(this);
		}
	}
	public void exitCombat() {
		if (this.hasVillage()) {
			this.village.getGuarding().removeCombatant(this);
		}
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.GUARD;
	}
}
