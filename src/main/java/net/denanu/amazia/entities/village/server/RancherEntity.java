package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.components.AmaziaEntityComponents;
import net.denanu.amazia.entities.village.server.goal.rancher.BringAnimalsToPen;
import net.denanu.amazia.entities.village.server.goal.rancher.FeedAnimalGoal;
import net.denanu.amazia.entities.village.server.goal.rancher.FetchAnimal;
import net.denanu.amazia.entities.village.server.goal.rancher.LeashAnimal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RancherEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.SHEARS, Items.AIR);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.WHEAT, 32, Items.CARROT, 32, Items.POTATO, 32, Items.BEETROOT, 32, Items.WHEAT_SEEDS, 32);
	public static final ImmutableSet<Item> CRAFTABLE_ITEMS = ImmutableSet.of();

	private final AnimationFactory factory = new AnimationFactory(this);

	@Nullable
	public AnimalEntity targetAnimal;
	public int animalInteractionAge;

	public RancherEntity(final EntityType<? extends PassiveEntity> entityType, final World world)  {
		super(entityType, world);
		AmaziaEntityComponents.setCanCollide(this, false);
		this.animalInteractionAge = 0;
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
	public void registerControllers(final AnimationData data) {
		data.addAnimationController(new AnimationController<RancherEntity>(this, "controller", 0, this::predicate));
	}

	// Rancher

	public void searchForTargetAnimal() {
		this.targetAnimal = this.village.getRanching().getUnboundAnimal();
	}

	@Override
	protected void initGoals() {

		this.goalSelector.add(47, new FeedAnimalGoal(this, 47));
		/*this.goalSelector.add(50, new SequenceGoal<RancherEntity>(this, ImmutableList.of(
				new FetchAnimal(this, 50),
				new LeashAnimal(this, 50),
				new BringAnimalsToPen(this, 50)
				)));*/
		this.goalSelector.add(48, new BringAnimalsToPen(this, 48));
		this.goalSelector.add(49, new LeashAnimal(this, 49));
		this.goalSelector.add(50, new FetchAnimal(this, 50));

		super.registerBaseGoals();
	}

	@Override
	public void mobTick() {
		super.mobTick();
		this.releaseTargetEntityIfDead();
		if (this.animalInteractionAge > 0) {
			this.animalInteractionAge--;
		}
	}

	public void releaseTargetEntity() {
		if (this.targetAnimal != null && this.targetAnimal.isLeashed()) {
			this.targetAnimal.detachLeash(true, false);
		}
		this.targetAnimal = null;
	}

	private void releaseTargetEntityIfDead() {
		if (this.targetAnimal != null && this.targetAnimal.isDead()) {
			this.releaseTargetEntity();
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	public boolean hasTargetAnimal() {
		return this.targetAnimal != null && this.targetAnimal.isAlive();
	}

	public boolean canInteractWithEntity() {
		return this.animalInteractionAge == 0;
	}

	public void setInteractionAge() {
		this.animalInteractionAge = 60;
	}

	@Override
	public boolean canDepositItems() {
		return this.getEmptyInventorySlots() == 0 && this.getDepositableItems() != null;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(RancherEntity.USABLE_ITEMS, RancherEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	public int getLeashTime() {
		return 20;
	}

	public int getFeedTime() {
		return 20;
	}

	public int getEntityInteractTime() {
		return 20;
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("animalInteractionAge", this.animalInteractionAge);
		if (this.targetAnimal!=null) {
			nbt.putUuid("targetAnimal", this.targetAnimal.getUuid());
		}
		return;
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.animalInteractionAge = nbt.getInt("animalInteractionAge");
		if (!this.world.isClient && nbt.contains("targetAnimal")) {
			if (JJUtils.getEntityByUniqueId(nbt.getUuid("targetAnimal"), (ServerWorld)this.world) instanceof final AnimalEntity animal) {
				this.targetAnimal = animal;
			}
		}

	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.RANCHER;
	}
}
