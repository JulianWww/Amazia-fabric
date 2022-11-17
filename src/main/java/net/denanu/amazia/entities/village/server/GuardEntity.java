package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.guard.GuardMeleeAttackGoal;
import net.denanu.amazia.entities.village.server.goal.guard.VillageGuardActiveTargetGoal;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.sceduling.opponents.CombatEvaluator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GuardEntity extends AmaziaVillagerEntity implements IAnimatable, InventoryChangedListener {
	private final AnimationFactory factory = new AnimationFactory(this);

	public GuardEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);

		this.getInventory().addListener(this);
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
		return AmaziaVillagerEntity.setAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 100);
	}

	private <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.walk", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.idle", true));
		return PlayState.CONTINUE;
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
		return null;
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public boolean canDepositItems() {
		return false;
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new GuardMeleeAttackGoal(this, 2.0, true));
		this.targetSelector.add(1, new VillageGuardActiveTargetGoal(this, 10));

		this.registerBaseGoals();
	}

	@Override
	public void setTarget(@Nullable final LivingEntity target) {
		super.setTarget(target);
		if (target != null) {
			this.equipWeapon(target);
		}
	}

	public void getToolIfNotPresant() {
		this.getSwordIfNotPresant();
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


	private void getSwordIfNotPresant() {
		for (final ItemStack stack : this.getInventory().stacks) {
			if (stack.getItem() instanceof final SwordItem sword) {
				this.requestSword(sword);
			}
		}
		this.requestItem(JJUtils.getRandomListElement(AmaziaData.MELEE_WEAPONS));
	}

	private void requestSword(final SwordItem current) {
		final SwordItem next = JJUtils.getRandomListElement(AmaziaData.MELEE_WEAPONS);
		if (CombatEvaluator.isBetter(current, next)) {
			this.requestItem(next);
		}
	}

	@Override
	public void onInventoryChanged(final Inventory inventory) {
		ItemStack current;
		for (int idx=0; idx < inventory.size(); idx++) {
			if (inventory.getStack(idx).getItem() instanceof final ArmorItem armor) {
				final EquipmentSlot slot = armor.getSlotType();
				current = this.getEquippedStack(slot);
				if (CombatEvaluator.isBetter(current.getItem(), armor)) {
					this.equipStack(slot, inventory.getStack(idx));
				}
			}
		}
	}

	private void equipWeapon(final LivingEntity target) {
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
		return 10;
	}
}
