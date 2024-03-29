package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.enchanting.EnchantGoal;
import net.denanu.amazia.entities.village.server.goal.enchanting.GoToEnchantingTable;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.denanu.amazia.village.AmaziaData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EnchanterEntity extends AmaziaVillagerEntity implements IAnimatable, VoidToVoidCallback {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.LAPIS_LAZULI, 64);
	private final AnimationFactory factory = new AnimationFactory(this);
	private Optional<Integer> enchantableItem;
	private BlockPos targetPos;
	private boolean shouldReturn;
	private int amountOfLapis;

	public EnchanterEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		this.enchantableItem = Optional.empty();
		this.shouldReturn = false;
	}

	private <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.generic.walk", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.generic.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(final AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("shouldReturnItem", this.shouldReturn);
		nbt.putInt("amountOfLapis", this.amountOfLapis);
		if (this.enchantableItem.isPresent()) {
			nbt.putInt("enchantableItemIdx", this.enchantableItem.get());
		}
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.shouldReturn = nbt.getBoolean("shouldReturnItem");
		this.amountOfLapis = nbt.getInt("amountOfLapis");
		if (nbt.contains("enchantableItemIdx", NbtElement.INT_TYPE)) {
			this.enchantableItem = Optional.of(nbt.getInt("enchantableItemIdx"));
		}
	}

	@Override
	protected void initGoals() {

		this.goalSelector.add(50, new SequenceGoal<>(this, ImmutableList.of(
				new GoToEnchantingTable(this, 50),
				new EnchantGoal(this, 50)
				)));

		super.registerBaseGoals(this, this::endReurnItem);
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(EnchanterEntity.USABLE_ITEMS, EnchanterEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public boolean canDepositItems() {
		if (!this.hasEnchantItem()) {
			this.shouldReturn = false;
		}
		return this.shouldReturn;
	}

	public boolean hasEnchantItem() {
		return this.enchantableItem.isPresent();
	}

	public Optional<Integer> getEnchantableItem() {
		return this.enchantableItem;
	}

	public void findEnchantableItem() {
		this.removeEnchantable();
		ItemStack stack;
		for (int i =0; i<this.getInventory().size(); i++) {
			stack = this.getInventory().getStack(i);
			if (stack.getItem().isEnchantable(stack)) {
				this.enchantableItem = Optional.of(i);
				return;
			}
		}
	}

	public void looseEnchantItem() {
		this.enchantableItem = Optional.empty();
	}

	public void removeEnchantable() {
		this.enchantableItem = Optional.empty();
	}

	@Override
	public void call() {
		this.findEnchantableItem();
		this.createHasLapis();
	}

	public void requestEnchantableItem() {
		this.requestItem(JJUtils.getRandomListElement(AmaziaData.ENCHANTABLES));

	}

	public int getEnchantTime() {
		return this.professionLevelManager.getEnchantingTime(this.isDepressed());
	}

	public BlockPos getTargetPos() {
		return this.targetPos;
	}

	public void setTargetPos(final BlockPos pos) {
		this.targetPos = pos;
	}

	public void returnItem() {
		this.shouldReturn = true;
	}

	public void endReurnItem() {
		this.shouldReturn = false;
		this.looseEnchantItem();
	}

	@Override
	public void setDeposeting(final boolean isDeposeting) {
		super.setDeposeting(isDeposeting);
		if (!isDeposeting) {
			this.endReurnItem();
		}
	}

	public int getEnchantinAbility() {
		return this.professionLevelManager.getEnchantAbility(this.getProfession());
	}

	public boolean canEnchant() {
		return this.amountOfLapis > 5;
	}

	public void enchantUseLapis()  {
		this.getInventory().removeItem(Items.LAPIS_LAZULI, 5);
		this.amountOfLapis = this.amountOfLapis - 5;
		if (!this.canEnchant()) {
			this.requestItem(Items.LAPIS_LAZULI);
		}
	}

	public boolean requestLapisOrCanEnchant() {
		if (!this.canEnchant()) {
			if (this.age%1 == 0) {
				this.requestItem(Items.LAPIS_LAZULI);
			}
			return false;
		}
		return true;
	}

	public void createHasLapis() {
		this.amountOfLapis = this.countItems(Items.LAPIS_LAZULI);
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.ENCHANTER;
	}
}
