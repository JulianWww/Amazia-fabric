package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.blacksmithing.CraftAtAnvilGoal;
import net.denanu.amazia.entities.village.server.goal.blacksmithing.GoToAnvilGoal;
import net.denanu.amazia.entities.village.server.goal.blacksmithing.GoToBlastFurnaceGoal;
import net.denanu.amazia.entities.village.server.goal.blacksmithing.PutItemsInFurnaceGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.village.AmaziaData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.CraftingRecipe;
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

public class BlacksmithEntity extends AmaziaSmelterVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.COAL, 64);

	public static boolean isCraftable(final ArmorItem armor) {
		return armor.getMaterial() != ArmorMaterials.NETHERITE && armor.getMaterial() != ArmorMaterials.CHAIN && armor != Items.TURTLE_HELMET;
	}
	public static boolean isCraftable(final RangedWeaponItem armor) {
		return false; //TODO add ranged weapon for guards
	}
	public static boolean isCraftable(final ToolItem tool) {
		return tool.getMaterial() != ToolMaterials.WOOD && tool.getMaterial() != ToolMaterials.NETHERITE;
	}

	// entity

	private final AnimationFactory factory = new AnimationFactory(this);
	@Nullable
	private BlockPos targetAnvilPos;
	public BlacksmithEntity(final EntityType<? extends PassiveEntity> entityType, final World world)  {
		super(entityType, world);
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
		data.addAnimationController(new AnimationController<BlacksmithEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// Blacksmith
	@Override
	public void mobTick() {
		super.mobTick();
		if (this.age % 200 == 0) {
			this.requestCraftable();
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(50, new SequenceGoal<BlacksmithEntity>(this, ImmutableList.of(
				new GoToBlastFurnaceGoal(this, 50),
				new PutItemsInFurnaceGoal(this, 50)
				)));

		this.goalSelector.add(51, new SequenceGoal<BlacksmithEntity>(this, ImmutableList.of(
				new GoToAnvilGoal(this, 51),
				new CraftAtAnvilGoal(this, 51)
				)));

		super.registerBaseGoals(this::scanForCoal, this::scanForCoal, false);
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(BlacksmithEntity.USABLE_ITEMS, BlacksmithEntity.REQUIRED_ITEMS);
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return Amazia.BLACKSMITH_CRAFTABLES;
	}

	public void requestCraftable() {
		if (!this.wantsToCraft() && this.hasVillage()) {
			this.tryCraftingStart(JJUtils.getRandomListElement(AmaziaData.BLACKSMITH_CRAFTING_ITEMS));
		}
	}

	public BlockPos getTargetAnvilPos() {
		return this.targetAnvilPos;
	}

	public void setTargetAnvilPos(final BlockPos targetAnvilPos) {
		this.targetAnvilPos = targetAnvilPos;
	}

	@Override
	public boolean canCraft () {
		return false;
	}
	@Override
	public void findSmeltingItem() {
		this.findSmeltingItem(AmaziaData.BLASTABLES);
	}
	@Override
	public void requestSmeltable() {
		this.requestItem(JJUtils.getRandomListElement(AmaziaData.BLASTABLES));
	}
}
