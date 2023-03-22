package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.goal.lumber.GoToLumberLocationGoal;
import net.denanu.amazia.entities.village.server.goal.lumber.HarvestTreeGoal;
import net.denanu.amazia.entities.village.server.goal.lumber.PlantSaplingGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.village.AmaziaData;
import net.denanu.amazia.village.sceduling.utils.LumberPathingData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class LumberjackEntity extends AmaziaVillagerEntity implements IAnimatable {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
	public static final ImmutableSet<Item> CRAFTABLES = ImmutableSet.of(Items.WOODEN_AXE, Items.STICK);
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.ACACIA_SAPLING, 64, Items.BIRCH_SAPLING, 64, Items.DARK_OAK_SAPLING, 64, Items.JUNGLE_SAPLING, 64, Items.OAK_SAPLING, 64, Items.SPRUCE_SAPLING, 64);
	private static final Vec3i ITEM_PICK_UP_RANGE_EXPANDER_WHILE_LUMBERING = new Vec3i(5, 5, 5);

	private final AnimationFactory factory = new AnimationFactory(this);
	private LumberPathingData lumberingLoc;
	private byte collectTimer;

	public LumberjackEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(48, new HarvestTreeGoal(this, 48));
		this.goalSelector.add(49, new PlantSaplingGoal(this, 49));
		this.goalSelector.add(50, new GoToLumberLocationGoal(this, 50));

		super.registerBaseGoals();
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
	public boolean canDepositItems() {
		return this.getEmptyInventorySlots() == 0 && this.getDepositableItems() != null;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(LumberjackEntity.USABLE_ITEMS, LumberjackEntity.REQUIRED_ITEMS);
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// LumberJacking

	@Override
	protected Vec3i getItemPickUpRangeExpander() {
		if (this.collectTimer > 0) {
			this.collectTimer--;
			return LumberjackEntity.ITEM_PICK_UP_RANGE_EXPANDER_WHILE_LUMBERING;
		}
		return super.getItemPickUpRangeExpander();
	}

	public void setCollectTimer() {
		this.collectTimer = 40;
	}

	public LumberPathingData getLumberingLoc() {
		return this.lumberingLoc;
	}

	public void setLumberingLoc(final LumberPathingData lumberingLoc) {
		this.lumberingLoc = lumberingLoc;
	}
	public boolean hasLumberLoc() {
		return this.lumberingLoc != null && this.lumberingLoc.getPos().isValid();
	}
	public boolean isInLumberLoc() {
		return this.lumberingLoc.getPos().isIn(this.getBlockPos());
	}

	public boolean hasSapling() {
		return this.getInventory().containsAny(AmaziaData.SAPLINGS);
	}
	public int getStackPosOfSapleing() {
		for (int idx=0; idx<this.getInventory().size(); idx++) {
			if (AmaziaData.SAPLINGS.contains(this.getInventory().getStack(idx).getItem())) {
				return idx;
			}
		}
		return -1;
	}

	public int getAxe() {
		ItemStack itm;
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			itm = this.getInventory().getStack(idx);
			if (itm.getItem() instanceof AxeItem) {
				return idx;
			}
		}
		return -1;
	}

	@Override
	public boolean canLumber() {
		return true;
	}

	@Override
	public boolean canCraft () {
		return false;
	}

	public void requestSapling() {
		if (!this.hasRequestedItems()) {
			for (final Item itm : AmaziaData.SAPLINGS) { this.requestItem(itm); }
		}
	}

	public void requestAxe() {
		if (!this.hasRequestedItems()) {
			for (final Item itm : LumberjackEntity.USABLE_ITEMS) { this.requestItem(itm); }
		}
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return Amazia.LUMBERJACK_CRAFTS;
	}

	@Override
	public Identifier getProfession() {
		return AmaziaProfessions.LUMBERJACK;
	}

}
