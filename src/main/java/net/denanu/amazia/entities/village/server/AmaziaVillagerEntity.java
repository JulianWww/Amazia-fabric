package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.GUI.AmaziaVillagerUIScreenHandler;
import net.denanu.amazia.entities.AmaziaEntityAttributes;
import net.denanu.amazia.entities.village.server.goal.AmaziaLookAroundGoal;
import net.denanu.amazia.entities.village.server.goal.mechanics.hunger.EatGoal;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;
import net.denanu.amazia.entities.village.server.goal.storage.DepositItemGoal;
import net.denanu.amazia.entities.village.server.goal.storage.GetItemGoal;
import net.denanu.amazia.mechanics.AmaziaMechanicsGuiEntity;
import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.denanu.amazia.mechanics.education.IAmaziaEducatedEntity;
import net.denanu.amazia.mechanics.hunger.AmaziaFood;
import net.denanu.amazia.mechanics.hunger.AmaziaFoodData;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.mechanics.leveling.ProfessionLevelManager;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.denanu.amazia.utils.crafting.CraftingUtils;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;

public abstract class AmaziaVillagerEntity extends AmaziaEntity implements InventoryOwner, AmaziaMechanicsGuiEntity, InventoryChangedListener, IAmaziaDataProviderEntity, ExtendedScreenHandlerFactory {
	private final SimpleInventory inventory = new SimpleInventory(16);
	private List<Item> requestedItems;
	private CraftingRecipe wantsToCraft;
	private Map<Item, Integer> craftInput;
	private boolean isDeposeting = false;

	private float hunger;
	private float intelligence;
	private float education;
	private final ProfessionLevelManager professionLevelManager;
	private float levelBoost;

	private Optional<Integer> bestFoodItem;

	public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
		@Override
		public int get(final int index) {
			return switch (index) {
			case 0 -> (int) AmaziaVillagerEntity.this.getHealth();
			case 1 -> (int) AmaziaVillagerEntity.this.hunger;
			case 2 -> (int) AmaziaVillagerEntity.this.intelligence;
			case 3 -> (int) AmaziaVillagerEntity.this.education;
			default -> AmaziaVillagerEntity.this.professionLevelManager.getLevelById(index - 4);
			};
		}

		@Override
		public void set(final int index, final int value) {
			switch (index) {
			case 0 -> AmaziaVillagerEntity.this.setHealth(value);
			}
		}

		@Override
		public int size() {
			return AmaziaVillagerEntity.propertiCount();
		}
	};

	public static int nonProfessionPropertyCounts( ) {
		return 4;
	}

	public static int propertiCount() {
		return AmaziaVillagerEntity.nonProfessionPropertyCounts() + AmaziaProfessions.PROFESSIONS.size();
	}

	protected AmaziaVillagerEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		this.setCanPickUpLoot(true);
		this.requestedItems = new ArrayList<Item>();
		this.craftInput = null;
		this.hunger = (float) this.getAttributeValue(AmaziaEntityAttributes.MAX_HUNGER);
		this.intelligence = 0f;
		this.bestFoodItem = Optional.empty();
		this.professionLevelManager = new ProfessionLevelManager();

		this.inventory.addListener(this);
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
		return AmaziaEntity.setAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.17f);
	}

	public void registerBaseGoals() {
		this.registerBaseGoals(null, null, true);
	}
	public void registerBaseGoals(final VoidToVoidCallback getItemCallback, final VoidToVoidCallback depositItemCallback) {
		this.registerBaseGoals(getItemCallback, depositItemCallback, true);
	}
	public void registerBaseGoals(final boolean addCrafter) {
		this.registerBaseGoals(null, null, addCrafter);
	}
	public void registerBaseGoals(final VoidToVoidCallback getItemCallback, final VoidToVoidCallback depositItemCallback, final boolean addCrafter) {
		this.goalSelector.add(0, new EscapeDangerGoal(this, 3.0f));
		this.registerNonCombatBaseGoals(getItemCallback, depositItemCallback, addCrafter);
	}
	public void registerNonCombatBaseGoals() {
		this.registerNonCombatBaseGoals(null, null, true);
	}
	public void registerNonCombatBaseGoals(final VoidToVoidCallback getItemCallback, final VoidToVoidCallback depositItemCallback, final boolean addCrafter) {
		if (addCrafter) {
			this.goalSelector.add(24, new CraftGoal<AmaziaVillagerEntity>(this, 24));
		}
		this.goalSelector.add(10, new EatGoal(this));
		this.goalSelector.add(25, new GetItemGoal(this, 25, getItemCallback));
		this.goalSelector.add(99, new DepositItemGoal(this, 99, depositItemCallback));
		this.goalSelector.add(100, new AmaziaLookAroundGoal(this));
	}

	@Override
	protected void dropInventory() {
		super.dropInventory();
		if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			this.vanishCursedItems();
			this.dropAll();
		}
	}
	protected void dropAll() {
		for (int i = 0; i < this.inventory.size(); ++i) {
			final ItemStack itemStack = this.inventory.getStack(i);
			if (itemStack.isEmpty()) {
				continue;
			}
			this.dropItem(itemStack, true, false);
			this.inventory.setStack(i, ItemStack.EMPTY);
		}
	}
	@Nullable
	protected ItemEntity dropItem(final ItemStack stack, final boolean throwRandomly, final boolean retainOwnership) {
		if (stack.isEmpty()) {
			return null;
		}
		if (this.world.isClient) {
			this.swingHand(Hand.MAIN_HAND);
		}
		final double d = this.getEyeY() - 0.3f;
		final ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), d, this.getZ(), stack);
		itemEntity.setPickupDelay(40);
		if (retainOwnership) {
			itemEntity.setThrower(this.getUuid());
		}
		if (throwRandomly) {
			final float f = this.random.nextFloat() * 0.5f;
			final float g = this.random.nextFloat() * ((float)Math.PI * 2);
			itemEntity.setVelocity(-MathHelper.sin(g) * f, 0.2f, MathHelper.cos(g) * f);
		} else {
			final float g = MathHelper.sin(this.getPitch() * ((float)Math.PI / 180));
			final float h = MathHelper.cos(this.getPitch() * ((float)Math.PI / 180));
			final float i = MathHelper.sin(this.getYaw() * ((float)Math.PI / 180));
			final float j = MathHelper.cos(this.getYaw() * ((float)Math.PI / 180));
			final float k = this.random.nextFloat() * ((float)Math.PI * 2);
			final float l = 0.02f * this.random.nextFloat();
			itemEntity.setVelocity(-i * h * 0.3f + Math.cos(k) * l, -g * 0.3f + 0.1f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f, j * h * 0.3f + Math.sin(k) * l);
		}
		return itemEntity;
	}
	protected void vanishCursedItems() {
		for (int i = 0; i < this.inventory.size(); ++i) {
			final ItemStack itemStack = this.inventory.getStack(i);
			if (itemStack.isEmpty() || !EnchantmentHelper.hasVanishingCurse(itemStack)) {
				continue;
			}
			this.inventory.removeStack(i);
		}
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("Inventory", this.inventory.toNbtList());
		nbt.putFloat("Hunger", this.hunger);
		nbt.putFloat("Intelligence", this.intelligence);
		nbt.putFloat("Education", this.education);
		nbt.put("professions", this.professionLevelManager.save());
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.inventory.clear();
		this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
		this.hunger = nbt.contains("Hunger") ? nbt.getFloat("Hunger") : (float) this.getAttributeValue(AmaziaEntityAttributes.MAX_HUNGER);
		this.intelligence = nbt.contains("Intelligence") ? nbt.getFloat("Intelligence") : IAmaziaIntelligenceEntity.getInitalIntelligence();
		this.education = nbt.contains("Education") ? nbt.getFloat("Education") : IAmaziaEducatedEntity.baseEducation(this);
		this.professionLevelManager.load(nbt.getCompound("professions"));
	}

	@Override
	public PassiveEntity createChild(final ServerWorld arg0, final PassiveEntity arg1) {
		return null;
	}

	@Override
	public SimpleInventory getInventory() {
		return this.inventory;
	}
	protected int getEmptyInventorySlots() {
		int count = 0;
		for (int idx=0; idx<this.inventory.size(); idx++) {
			if (this.inventory.getStack(idx).isEmpty()) {
				count++;
			}
		}
		return count;
	}

	@Nullable
	public abstract Triplet<ItemStack, Integer, Integer> getDepositableItems();
	@Nullable
	public abstract HashMap<Item,ArrayList<CraftingRecipe>> getCraftables();

	public boolean wantToKeepItemInSlot(final int idx) {
		return this.bestFoodItem.isPresent() && this.bestFoodItem.get() == idx;
	}

	protected Map<Item, Integer> getItemCounts(final Map<Item, Integer> minItems) {
		final Map<Item, Integer> count = new HashMap<Item, Integer>();
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			final ItemStack itm = this.getInventory().getStack(idx);
			if (count.containsKey(itm.getItem())) {
				count.put(itm.getItem(), count.get(itm.getItem()) + itm.getCount());
			}
			else {
				count.put(itm.getItem(), itm.getCount() - (minItems.containsKey(itm.getItem()) ? minItems.get(itm.getItem()) : 0));
			}
		}
		return count;
	}

	public boolean isDeposeting() {
		return this.isDeposeting || !this.hasFreeSlot();
	}

	public void setDeposeting(final boolean isDeposeting) {
		this.isDeposeting = isDeposeting;
	}

	protected int countItems(final Item itm) {
		int counter = 0;
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			final ItemStack stack = this.getInventory().getStack(idx);
			if (stack.getItem().equals(itm)) {
				counter = counter + stack.getCount();
			}
		}
		return counter;
	}

	protected Triplet<ItemStack, Integer, Integer> getDepositableItems(final Set<Item> toIgnore, final Map<Item, Integer> minItems) {
		final Map<Item, Integer> itemCounts = this.getItemCounts(minItems);
		final int counter = 0;
		Triplet<ItemStack, Integer, Integer> out = null;

		for (int idx=0; idx < this.getInventory().size(); idx++) {
			final ItemStack itm = this.getInventory().getStack(idx);
			if (!this.wantToKeepItemInSlot(idx) && !toIgnore.contains(itm.getItem()) && !itm.isOf(Items.AIR)) {
				if (itm.getCount() > counter && (itemCounts.containsKey(itm.getItem()) ? itemCounts.get(itm.getItem()) : 0) > 0) {
					out = new Triplet<>(itm, idx, Math.min(itm.getCount(), itemCounts.containsKey(itm.getItem()) ? itemCounts.get(itm.getItem()) : 0));
				}
			}
		}
		return out;
	}

	public Item getRandomRequiredItem() {
		if (this.requestedItems.size() > 0) {
			return this.requestedItems.get(0);
		}
		return null;
	}
	public void requestItem(final Item itm) {
		if (!this.requestedItems.contains(itm) && !this.wantsToCraft()) {
			this.requestedItems.add(itm);
		}
	}
	public void removeRequestedItem(final Item itm) {
		this.requestedItems.remove(itm);
	}
	public void clearRequestItemQueue() {
		this.requestedItems.clear();
	}
	public boolean hasRequestedItems() {
		return this.requestedItems.size() > 0 && this.getEmptyInventorySlots() > 0;
	}

	public CraftingRecipe getWantsToCraft() {
		return this.wantsToCraft;
	}

	public void tryCraftingStart(final Item itm) {
		final HashMap<Item, ArrayList<CraftingRecipe>> craftables = this.getCraftables();
		if (craftables.containsKey(itm)) {
			Amazia.LOGGER.info(itm.toString());
			final CraftingRecipe recipy = JJUtils.getRandomListElement(craftables.get(itm));
			final ArrayList<Item> ingredients = new ArrayList<Item>();
			this.craftInput = CraftingUtils.getRecipyInput(recipy);
			boolean canCraft = true;
			for (final Entry<Item, Integer> ingredient : this.craftInput.entrySet()) {
				if (this.hasItem(ingredient.getKey(), ingredient.getValue())) {
					continue;
				}
				if (this.getVillage().getStorage().itemInStorage((ServerWorld)this.getWorld(), ingredient.getKey())) {
					ingredients.add(ingredient.getKey());
					continue;
				}
				this.requestItem(ingredient.getKey());
				canCraft = false;
			}
			if (canCraft) {
				this.wantsToCraft = recipy;
				this.requestedItems = ingredients;
			}
		}
	}
	public boolean wantsToCraft() {
		return this.wantsToCraft != null;
	}

	public Map<Item, Integer> getCraftInput() {
		return this.craftInput;
	}

	public void endCraft() {
		this.craftInput = null;
		this.wantsToCraft = null;
	}

	public void damage(final int idx) {
		if (this.getInventory().getStack(idx).damage(1, this.random, null)) {
			this.getInventory().setStack(idx, ItemStack.EMPTY);
		}
	}

	public int removeItemFromInventory(final Item itm, int count) {
		for (int idx = this.inventory.size() - 1; idx >= 0 && count > 0; idx--) {
			final ItemStack stack = this.inventory.getStack(idx);
			if (stack.isOf(itm) && !stack.isEmpty()) {
				final int delta = Math.min(stack.getCount(), count);
				stack.decrement(delta);
				count = count - delta;
				this.inventory.setStack(idx, stack);
			}
			if (stack.isEmpty()) {
				this.inventory.setStack(idx, ItemStack.EMPTY);
			}
		}
		return count;
	}
	public boolean hasItem(final Item itm, final int count) {
		return this.inventory.count(itm) >= count;
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
	}

	protected boolean shouldPickUp(final ItemStack stack) {
		return true;
	}

	@Override
	protected void loot(final ItemEntity item) {
		if (this.shouldPickUp(item.getStack())) { InventoryOwner.pickUpItem(this, this, item); }
	}

	public boolean hasFreeSlot() {
		for (int i = 0; i < this.getInventory().size(); ++i) {
			final ItemStack itemStack = this.getInventory().getStack(i);
			if (itemStack.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public int getItemStack(final Item irm) {
		for (int i = 0; i < this.getInventory().size(); ++i) {
			final ItemStack itemStack = this.getInventory().getStack(i);
			if (itemStack.isOf(irm)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void reduceFood(final float amount) {
		this.hunger = this.hunger - amount;
		if (this.hunger < 0) {
			this.damage(DamageSource.STARVE, 1);
			this.hunger = 0;
		}
	}

	@Override
	public void eatFood(final float amount) {
		this.hunger = (float) Math.min(
				this.hunger + amount,
				this.getAttributeValue(AmaziaEntityAttributes.MAX_HUNGER)
				);
	}

	@Override
	public float getHunger() {
		return this.hunger;
	}

	@Override
	public void setHunger(final float value) {
		this.hunger = value;
	}

	@Override
	public ActionResult interactMob(final PlayerEntity player, final Hand hand) {
		if (this.isAlive()) {
			if (hand == Hand.MAIN_HAND) {
				player.incrementStat(Stats.TALKED_TO_VILLAGER);
			}
			/*if (this.getOffers().isEmpty()) {
                return ActionResult.success(this.world.isClient);
            }*/
			if (!this.world.isClient) {
				this.sendVillagerData(player, this.getName());
			}
			return ActionResult.success(this.world.isClient);
		}
		return super.interactMob(player, hand);
	}

	@Override
	public ScreenHandler createMenu(final int syncId, final PlayerInventory inventory, final PlayerEntity var3) {
		return new AmaziaVillagerUIScreenHandler(syncId, inventory, this, this.propertyDelegate, this.getStatusEffects());
	}

	@Override
	public void consumeNutrishousItem() {
		this.consumeFood(this.bestFoodItem);
	}

	private void consumeFood(final Optional<Integer> idx) {
		if (idx.isPresent()) {
			final ItemStack stack = this.getInventory().getStack(idx.get());
			final AmaziaFood food = AmaziaFoodData.TO_FOOD_MAP.get(stack.getItem());
			if (food != null) {
				this.eatFood(food.getFoodValue());
				stack.decrement(1);
				if (stack.isEmpty()) {
					this.getInventory().setStack(idx.get(), ItemStack.EMPTY);
				}
				else {
					this.getInventory().setStack(idx.get(), stack);
				}
			}
		}
	}

	@Override
	public void onInventoryChanged(final Inventory inventory) {
		final float maxFood = -1f;
		this.bestFoodItem = Optional.empty();
		for (int idx = 0; idx < inventory.size(); idx++) {
			final AmaziaFood food = AmaziaFoodData.TO_FOOD_MAP.get(inventory.getStack(idx).getItem());
			if (food != null && maxFood < food.getFoodValue()) {
				this.bestFoodItem = Optional.of(idx);
			}
		}
	}

	@Override
	public boolean hasOrRequestFood() {
		if (this.bestFoodItem.isEmpty()) {
			this.endCraft();
			if (!this.hasRequestedItems()) {
				for (final AmaziaFood food : AmaziaFoodData.NUTRISCHOUS_FOODS) {
					this.requestItem(food.getItem());
				}
			}
		}
		return this.bestFoodItem.isPresent();
	}

	@Override
	public float getIntelligence() {
		return this.intelligence;
	}

	@Override
	public float getEducation() {
		return this.education;
	}

	public abstract Identifier getProfession();

	@Override
	public void gainXp(final float xpVal) {
		this.gainXp(this.getProfession(), xpVal);
	}
	@Override
	public void gainXp(final Identifier profession, final float xpVal) {
		this.professionLevelManager.gainXp(profession, xpVal, this.intelligence);
	}
	@Override
	public float getLevel() {
		return this.getLevel(this.getProfession());
	}
	@Override
	public float getLevel(final Identifier profession) {
		return this.professionLevelManager.getLevel(profession) + this.levelBoost;
	}

	@Override
	public void learn(final float baseAmount) {
		this.education = this.education + (this.intelligence - this.education) * baseAmount;
	}

	protected boolean isLowHp() {
		return AmaziaVillagerEntity.isLowHpPredicate(this);
	}

	public static boolean isLowHpPredicate(final LivingEntity e) {
		return e.getHealth() < e.getMaxHealth() * 0.25;
	}

	public static boolean isNotFullHealth(final LivingEntity e) {
		return e.getHealth() < e.getMaxHealth();
	}

	@Override
	public boolean canGather(final ItemStack stack) {
		return this.getInventory().canInsert(stack);
	}

	public boolean canPlant() {
		return false;
	}
	public boolean canHarvest() {
		return false;
	}
	public boolean canHoe() {
		return false;
	}
	public boolean canCraft () {
		return false;
	}
	public boolean canMine() {
		return false;
	}
	public boolean canLumber() {
		return false;
	}
	public abstract boolean canDepositItems();

	public int getHoeingTime() {
		return 20;
	}
	public int getPlantTime() {
		return 20;
	}
	public int getHarvestTime() {
		return 20;
	}
	public int getBlockPlaceTime() {
		return 20;
	}
	public int getMineTime() {
		return 2;
	}
	public int getCraftingTime() {
		return 20;
	}

	public float getLevelBoost() {
		return this.levelBoost;
	}

	public void setLevelBoost(final float levelBoost) {
		this.levelBoost = levelBoost;
	}
	
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeCollection(this.getStatusEffects(), (buf2, data) -> {
        	buf2.writeNbt(data.writeNbt(new NbtCompound()));
        });
    }
}
