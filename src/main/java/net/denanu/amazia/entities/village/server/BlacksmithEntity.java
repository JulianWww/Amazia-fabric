package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.village.AmaziaData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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

public class BlacksmithEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.COAL, 64);
	public static final ImmutableSet<Item> CRAFTABLE_ITEMS = ImmutableSet.of();

	private Optional<Integer> blastingItem;

	private AnimationFactory factory = new AnimationFactory(this);

	public BlacksmithEntity(EntityType<? extends PassiveEntity> entityType, World world)  {
		super(entityType, world);
		this.blastingItem = Optional.empty();
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.walk", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<BlacksmithEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// Blacksmith

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (this.blastingItem.isPresent()) {
			nbt.putInt("blastingItem", this.blastingItem.get());
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.blastingItem = Optional.ofNullable(nbt.getInt("blastingItem"));
	}

	@Override
	protected void initGoals() {
		super.registerBaseGoals();
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return null;
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	public void requestCoal() {
		this.requestItem(Items.COAL);
	}

	@Override
	public boolean canDepositItems() {
		return false;
	}

	public boolean canBlast() {
		return this.blastingItem.isPresent();
	}

	public boolean canOrFindBlast() {
		if (!this.canBlast()) {
			this.findBlastingItem();
		}
		return this.canBlast();
	}

	public Optional<Integer> getBlatingItem() {
		return this.blastingItem;
	}

	public void findBlastingItem() {
		this.blastingItem = Optional.empty();
		for (int i = 0; i<this.getInventory().size(); i++) {
			if (AmaziaData.BLASTABLES.contains(this.getInventory().getStack(i).getItem())) {
				this.blastingItem = Optional.of(i);
				return;
			}
		}
		this.requestBlastable();
	}

	private void requestBlastable() {
		this.requestItem(JJUtils.getRandomListElement(AmaziaData.BLASTABLES));
	}
}
