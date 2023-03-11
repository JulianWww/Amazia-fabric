package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.GUI.AmaziaVillagerUIScreenHandler;
import net.denanu.amazia.block.AmaziaBlockProperties;
import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.denanu.amazia.entities.AmaziaEntityAttributes;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.denanu.amazia.entities.village.both.VillagerData;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal.PathingAmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaLookAroundGoal;
import net.denanu.amazia.entities.village.server.goal.mechanics.education.GoToLibraryGoal;
import net.denanu.amazia.entities.village.server.goal.mechanics.education.LearnFromReadingBooksGoal;
import net.denanu.amazia.entities.village.server.goal.mechanics.hunger.EatGoal;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;
import net.denanu.amazia.entities.village.server.goal.storage.DepositItemGoal;
import net.denanu.amazia.entities.village.server.goal.storage.GetItemGoal;
import net.denanu.amazia.entities.village.server.goal.utils.SequenceGoal;
import net.denanu.amazia.entities.village.server.goal.utils.combat.AmaziaEscapeDangerGoal;
import net.denanu.amazia.entities.village.server.goal.utils.sleep.GoToBedGoal;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.item.custom.VillagerTransformationTokenItem;
import net.denanu.amazia.mechanics.AmaziaMechanicsGuiEntity;
import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.denanu.amazia.mechanics.VillagerTypes;
import net.denanu.amazia.mechanics.education.IAmaziaEducatedEntity;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.mechanics.happyness.IAmaziaHappynessEntity;
import net.denanu.amazia.mechanics.hunger.AmaziaFood;
import net.denanu.amazia.mechanics.hunger.AmaziaFoodData;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.mechanics.leveling.ProfessionLevelManager;
import net.denanu.amazia.mechanics.title.Titles;
import net.denanu.amazia.particles.AmaziaItemParticleMap;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.denanu.amazia.utils.crafting.CraftingUtils;
import net.denanu.amazia.village.events.EventData;
import net.denanu.amazia.village.events.VillageDieEventData;
import net.denanu.amazia.village.events.VillageEvents;
import net.denanu.amazia.village.scedule.VillageActivityGroups;
import net.denanu.amazia.village.scedule.VillagerScedule;
import net.denanu.blockhighlighting.utils.NbtUtils;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
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
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;

public abstract class AmaziaVillagerEntity extends AmaziaEntity implements InventoryOwner, AmaziaMechanicsGuiEntity,
InventoryChangedListener, IAmaziaDataProviderEntity, ExtendedScreenHandlerFactory, PathingAmaziaVillagerEntity {
	private static final TrackedData<Float> INTELLIGENCE = DataTracker.registerData(AmaziaVillagerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<VillagerData> VILLAGER_DATA = DataTracker.registerData(VillagerEntity.class, VillagerData.VILLAGER_DATA);

	private final SimpleInventory inventory = new SimpleInventory(18);
	private List<Item> requestedItems;
	private CraftingRecipe wantsToCraft;
	private Map<Item, Integer> craftInput;
	private boolean isDeposeting = false;

	private final VillagerScedule activityScedule = new VillagerScedule();

	private float hunger;
	private float education;
	protected final ProfessionLevelManager professionLevelManager;
	private float happyness;

	private Optional<Integer> bestFoodItem;

	BlockPos bedLocation = null;


	public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
		@Override
		public int get(final int index) {
			return switch (index) {
			case 0 -> (int) AmaziaVillagerEntity.this.getHealth();
			case 1 -> (int) AmaziaVillagerEntity.this.hunger;
			case 2 -> (int) AmaziaVillagerEntity.this.education;
			case 3 -> (int) AmaziaVillagerEntity.this.happyness;
			default -> AmaziaVillagerEntity.this.professionLevelManager.getLevelById(index - AmaziaVillagerEntity.nonProfessionPropertyCounts());
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

	public static int nonProfessionPropertyCounts() {
		return 4;
	}

	public static int propertiCount() {
		return AmaziaVillagerEntity.nonProfessionPropertyCounts() + AmaziaProfessions.PROFESSIONS.size();
	}

	protected AmaziaVillagerEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		this(entityType, world, null, 0f);
	}

	protected AmaziaVillagerEntity(final EntityType<? extends PassiveEntity> entityType, final World world, final String lastName, final float intelligence) {
		super(entityType, world);
		this.setCanPickUpLoot(true);
		this.requestedItems = new ArrayList<>();
		this.craftInput = null;
		this.hunger = (float) this.getAttributeValue(AmaziaEntityAttributes.MAX_HUNGER);
		this.dataTracker.set(AmaziaVillagerEntity.INTELLIGENCE, intelligence);
		this.education = 0;
		this.bestFoodItem = Optional.empty();
		this.professionLevelManager = new ProfessionLevelManager();

		this.inventory.addListener(this);
		this.activityScedule.generate();

		this.setCustomName(Text.literal(NamingLanguageOptions.generateName(lastName)));
		this.setCustomNameVisible(false);
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
		return AmaziaEntity.setAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(AmaziaVillagerEntity.INTELLIGENCE, 0f);
		this.dataTracker.startTracking(AmaziaVillagerEntity.VILLAGER_DATA, new VillagerData(VillagerTypes.SNOW));
	}

	public void registerBaseGoals() {
		this.registerBaseGoals(null, null, true);
	}

	public void registerBaseGoals(final VoidToVoidCallback getItemCallback,
			final VoidToVoidCallback depositItemCallback) {
		this.registerBaseGoals(getItemCallback, depositItemCallback, true);
	}

	public void registerBaseGoals(final boolean addCrafter) {
		this.registerBaseGoals(null, null, addCrafter);
	}

	public void registerBaseGoals(final VoidToVoidCallback getItemCallback,
			final VoidToVoidCallback depositItemCallback, final boolean addCrafter) {
		this.goalSelector.add(0, new AmaziaEscapeDangerGoal(this, 3.0f));
		this.registerNonCombatBaseGoals(getItemCallback, depositItemCallback, addCrafter);
	}

	public void registerNonCombatBaseGoals() {
		this.registerNonCombatBaseGoals(null, null, true);
	}

	public void registerNonCombatBaseGoals(final VoidToVoidCallback getItemCallback,
			final VoidToVoidCallback depositItemCallback, final boolean addCrafter) {
		if (addCrafter) {
			this.goalSelector.add(24, new CraftGoal<>(this, 24));
		}
		this.goalSelector.add(10, new EatGoal(this));

		this.goalSelector.add(11, new GoToBedGoal(this, 11));


		this.goalSelector.add(25, new GetItemGoal(this, 25, getItemCallback));

		this.goalSelector.add(80, new SequenceGoal<>(this, ImmutableList.of(
				new GoToLibraryGoal(this, 80),
				new LearnFromReadingBooksGoal(this, 80)
				)));

		this.goalSelector.add(98, new DepositItemGoal(this, 98, depositItemCallback));
		//this.goalSelector.add(99, new NitwitRandomWanderAroundGoal<>(this, 99));
		this.goalSelector.add(100, new AmaziaLookAroundGoal(this));

	}

	@Override
	public void onDeath(final DamageSource source) {
		super.onDeath(source);
		if (this.hasVillage()) {

		}
	}

	@Override
	protected void dropInventory() {
		super.dropInventory();
		this.vanishCursedItems();
		this.dropAll();
	}

	protected void dropAll() {
		for (int i = 0; i < this.inventory.size(); ++i) {
			final ItemStack itemStack = this.inventory.getStack(i);
			if (itemStack.isEmpty()) {
				continue;
			}
			this.world.spawnEntity(
					this.dropItem(itemStack, true, false)
					);
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
		final double d = this.getEyeY() - .3f;
		final ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), d, this.getZ(), stack);
		itemEntity.setPickupDelay(40);
		if (retainOwnership) {
			itemEntity.setThrower(this.getUuid());
		}
		if (throwRandomly) {
			final float f = this.random.nextFloat() * 0.3f + 0.1f;
			final float g = this.random.nextFloat() * ((float) Math.PI * 2);
			itemEntity.setVelocity(MathHelper.sin(g) * f, 0.2f, MathHelper.cos(g) * f);
		} else {
			final float g = MathHelper.sin(this.getPitch() * ((float) Math.PI / 180));
			final float h = MathHelper.cos(this.getPitch() * ((float) Math.PI / 180));
			final float i = MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
			final float j = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
			final float k = this.random.nextFloat() * ((float) Math.PI * 2);
			final float l = 0.02f * this.random.nextFloat();
			itemEntity.setVelocity(
					-i * h * 0.3f + Math.cos(k) * l,
					-g * 0.3f + 0.1f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f,
					j * h * 0.3f + Math.sin(k) * l);
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
	protected void update() {
		super.update();
		this.activityScedule.update(this.world);
	}

	@Override
	public void mobTick() {
		super.mobTick();
		if (this.isSleeping()) {
			if (this.getActivityScedule().getPerformActionGroup() != VillageActivityGroups.SLEEP) {
				this.wakeUpAndChild();
			}
			if (!(this.world.getBlockState(this.getBlockPos()).getBlock() instanceof BedBlock)) {
				this.wakeUp();
			}
		}
	}

	public void wakeUpAndChild() {
		super.wakeUp();
		if (this.hasVillage()) {
			this.village.addChild(this);
		}

	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("Inventory", this.inventory.toNbtList());
		nbt.putFloat("Hunger", this.hunger);
		nbt.putFloat("Intelligence", this.dataTracker.get(AmaziaVillagerEntity.INTELLIGENCE));
		nbt.putFloat("Education", this.education);
		nbt.putFloat("Happyness", this.happyness);
		nbt.put("professions", this.professionLevelManager.save());
		nbt.put("Scedule", this.activityScedule.writeCustomNbt(new NbtCompound()));
		nbt.put("type", this.dataTracker.get(AmaziaVillagerEntity.VILLAGER_DATA).toNbt());
		nbt.put("bed", NbtUtils.toNbt(this.bedLocation));
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.inventory.clear();
		this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
		this.hunger = nbt.contains("Hunger") ? nbt.getFloat("Hunger") : (float) this.getAttributeValue(AmaziaEntityAttributes.MAX_HUNGER);
		this.dataTracker.set(AmaziaVillagerEntity.INTELLIGENCE, nbt.contains("Intelligence") ? nbt.getFloat("Intelligence") : IAmaziaIntelligenceEntity.getInitalIntelligence());
		this.education = nbt.contains("Education") ? nbt.getFloat("Education") : IAmaziaEducatedEntity.baseEducation(this);
		this.happyness = nbt.contains("Happyness") ? nbt.getFloat("Happyness") : IAmaziaHappynessEntity.getDefaultHappyness();
		this.professionLevelManager.load(nbt.getCompound("professions"), this.getProfession(), this.getVillage());
		this.activityScedule.readCustomNbt(nbt.getCompound("Scedule"));
		this.bedLocation = NbtUtils.toBlockPos(nbt.getList("bed", NbtElement.INT_TYPE));

		if (nbt.contains("type")) {
			this.dataTracker.set(AmaziaVillagerEntity.VILLAGER_DATA, new VillagerData(nbt.getCompound("type")));
		}

		this.setPositionInBed();
		this.updateName();
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
		for (int idx = 0; idx < this.inventory.size(); idx++) {
			if (this.inventory.getStack(idx).isEmpty()) {
				count++;
			}
		}
		return count;
	}

	@Nullable
	public abstract Triplet<ItemStack, Integer, Integer> getDepositableItems();

	@Nullable
	public abstract HashMap<Item, ArrayList<CraftingRecipe>> getCraftables();

	public boolean wantToKeepItemInSlot(final int idx) {
		return this.bestFoodItem.isPresent() && this.bestFoodItem.get() == idx;
	}

	protected Map<Item, Integer> getItemCounts(final Map<Item, Integer> minItems) {
		final Map<Item, Integer> count = new HashMap<>();
		for (int idx = 0; idx < this.getInventory().size(); idx++) {
			final ItemStack itm = this.getInventory().getStack(idx);
			if (count.containsKey(itm.getItem())) {
				count.put(itm.getItem(), count.get(itm.getItem()) + itm.getCount());
			} else {
				count.put(itm.getItem(),
						itm.getCount() - (minItems.containsKey(itm.getItem()) ? minItems.get(itm.getItem()) : 0));
			}
		}
		return count;
	}

	@Override
	public boolean isDeposeting() {
		return this.isDeposeting || !this.hasFreeSlot();
	}

	public void setDeposeting(final boolean isDeposeting) {
		this.isDeposeting = isDeposeting;
	}

	protected int countItems(final Item itm) {
		int counter = 0;
		for (int idx = 0; idx < this.getInventory().size(); idx++) {
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

		for (int idx = 0; idx < this.getInventory().size(); idx++) {
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
			final ArrayList<Item> ingredients = new ArrayList<>();
			this.craftInput = CraftingUtils.getRecipyInput(recipy);
			boolean canCraft = true;
			for (final Entry<Item, Integer> ingredient : this.craftInput.entrySet()) {
				if (this.hasItem(ingredient.getKey(), ingredient.getValue())) {
					continue;
				}
				if (this.getVillage().getStorage().itemInStorage((ServerWorld) this.getWorld(), ingredient.getKey())) {
					ingredients.add(ingredient.getKey());
					continue;
				}
				this.requestItem(ingredient.getKey());
				canCraft = false;
			}
			if (canCraft) {
				this.wantsToCraft = recipy;
				this.requestedItems = ingredients;
				return;
			}
		}
		this.failCraft(itm);
	}

	public void failCraft(final Item item) {
		final DefaultParticleType particle = AmaziaItemParticleMap.get(item);
		if (particle != null) {
			((ServerWorld)this.world).spawnParticles(particle, this.getX(), this.getY() + 2, this.getZ(), 1, 0, 0, 0, 0);
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

	protected boolean shouldPickUp(final ItemStack stack) {
		return true;
	}

	@Override
	protected void loot(final ItemEntity item) {
		if (this.shouldPickUp(item.getStack())) {
			InventoryOwner.pickUpItem(this, this, item);
		}
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
	public Text getDefaultNameAcessor() {
		return this.getDefaultName();
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
		this.hunger = Math.min(this.hunger + amount, this.getMaxHunger());
	}

	@Override
	public float getMaxHunger() {
		return (float) this.getAttributeValue(AmaziaEntityAttributes.MAX_HUNGER);
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
		if (this.isAlive() && !player.getEquippedStack(EquipmentSlot.MAINHAND).isOf(AmaziaItems.BOOK_OF_INTELLIGENCE) && !(player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() instanceof VillagerTransformationTokenItem)) {
			if (hand == Hand.MAIN_HAND) {
				player.incrementStat(Stats.TALKED_TO_VILLAGER);
			}
			if (!this.world.isClient) {
				this.sendVillagerData(player, this.getDefaultName());
				this.professionLevelManager.update(this.village, this.getProfession());
			}
			return ActionResult.success(this.world.isClient);
		}
		return super.interactMob(player, hand);
	}

	@Override
	public ScreenHandler createMenu(final int syncId, final PlayerInventory inventory, final PlayerEntity var3) {
		return new AmaziaVillagerUIScreenHandler(syncId, inventory, this, this.propertyDelegate,
				this.getStatusEffects());
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
				this.applyHappiness(food.getHappinessValue());
				stack.decrement(1);
				if (stack.isEmpty()) {
					this.getInventory().setStack(idx.get(), ItemStack.EMPTY);
				} else {
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
		return this.dataTracker.get(AmaziaVillagerEntity.INTELLIGENCE);
	}

	public VillagerData getData() {
		return this.dataTracker.get(AmaziaVillagerEntity.VILLAGER_DATA);
	}

	@Override
	public boolean modifyIntelligence(final float amount) {
		float intelligence = this.getIntelligence();
		if (intelligence == 100f) {
			return false;
		}
		intelligence += amount;
		if (intelligence > this.maxIntelligence()) {
			intelligence = this.maxIntelligence();
		}
		this.dataTracker.set(AmaziaVillagerEntity.INTELLIGENCE, intelligence);
		return true;
	}

	@Override
	public float getEducation() {
		return this.education;
	}

	@Override
	public void gainHappyness(final float amount) {
		this.happyness = this.happyness + amount;
		if (this.happyness > this.getMaxHappyness()) {
			this.happyness = this.getMaxHappyness();
		}

		this.emmitMood(VillagerMoods.HAPPY);
	}
	@Override
	public void looseHappyness(final float amount) {
		this.happyness = this.happyness - amount;
		if (this.happyness < 0f) {
			this.happyness = 0f;
		}
	}

	@Override
	public float getHappyness() {
		return this.happyness;
	}

	@Override
	public abstract Identifier getProfession();

	@Override
	public void gainXp(final float xpVal) {
		this.gainXp(this.getProfession(), xpVal);
	}

	@Override
	public void gainXp(final Identifier profession, final float xpVal) {
		this.professionLevelManager.gainXp(profession, xpVal, this.education, this.getVillage());
	}

	@Override
	public float getLevel() {
		return this.getLevel(this.getProfession());
	}

	@Override
	public float getLevel(final Identifier profession) {
		return this.professionLevelManager.getLevel(profession);
	}

	@Override
	public void learn(final float baseAmount) {
		this.education = this.education + (this.dataTracker.get(AmaziaVillagerEntity.INTELLIGENCE) - this.education) * baseAmount;
		this.updateName();
	}

	private void updateName() {
		super.setCustomName(Text.of(Titles.updateTitles(this.getCustomName().getString(), this, this.hasVillage() ? this.getVillage().getMayor() : null)));
	}

	@Override
	public void setCustomName(final Text txt) {
		super.setCustomName(txt);
		this.updateName();
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

	public boolean canCraft() {
		return false;
	}

	public boolean canMine() {
		return false;
	}

	public boolean canLumber() {
		return false;
	}

	public boolean canDepositItems() {
		return !this.hasFreeSlot();
	}

	public int getHoeingTime() {
		return this.professionLevelManager.getHoingTime(this.isDepressed());
	}

	public int getPlantTime() {
		return this.professionLevelManager.getPlantingTime(this.isDepressed());
	}

	public int getHarvestTime() {
		return this.professionLevelManager.getHarvestingTime(this.isDepressed());
	}

	public int getBlockPlaceTime() {
		return this.professionLevelManager.getBlockPlaceTime(this.isDepressed());
	}

	public int getMineTime() {
		return this.professionLevelManager.getMineTime(this.isDepressed());
	}

	public int getCraftingTime() {
		return this.professionLevelManager.getCraftingTime(this.isDepressed());
	}

	@Override
	public float getLevelBoost() {
		return this.professionLevelManager.getLevelBoost();
	}

	@Override
	public void setLevelBoost(final float levelBoost) {
		this.professionLevelManager.setLevelBoost(levelBoost, this.getProfession());
	}

	@Override
	public void writeScreenOpeningData(final ServerPlayerEntity serverPlayerEntity, final PacketByteBuf buf) {
		buf.writeInt(this.getId());
		buf.writeCollection(this.getStatusEffects(), (buf2, data) -> {
			buf2.writeNbt(data.writeNbt(new NbtCompound()));
		});
	}

	@Override
	public VillagerScedule getActivityScedule() {
		return this.activityScedule;
	}

	@Override
	public boolean damage(final DamageSource source, final float amount) {
		if (!this.world.isClient) {
			HappynessMap.looseTakeDamageHappyness(this);
		}
		return super.damage(source, amount);
	}

	@Override
	public void receiveEvent(final VillageEvents event, final EventData data) {
		super.receiveEvent(event, data);
		switch (event) {
		case VILLAGER_DIE -> this.onSeeVillagerDie(data);
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (!this.world.isClient && this.hasVillage()) {
			this.village.emmitEvent(VillageEvents.VILLAGER_DIE, new VillageDieEventData(this, this.isBaby()));
		}
		this.releaseBed();
	}

	private void onSeeVillagerDie(final EventData data) {
		if (this.squaredDistanceTo(data.getEmmiter()) < 10000 && this.canSee(data.getEmmiter())) {
			HappynessMap.loseHappynessSeeVillagerDie(this, ((VillageDieEventData)data).getIsBaby());
		}
	}

	@Override
	public void sleep(final BlockPos pos) {
		BlockState blockState;
		if (this.hasVehicle()) {
			this.stopRiding();
		}
		if ((blockState = this.world.getBlockState(pos)).getBlock() instanceof BedBlock) {
			if (blockState.get(BedBlock.OCCUPIED)) {
				this.releaseBed();
				return;
			}
			this.world.setBlockState(pos, blockState.with(BedBlock.OCCUPIED, true), Block.NOTIFY_ALL);
			this.setPositionInBed(pos, blockState.get(HorizontalFacingBlock.FACING));

			this.setPose(EntityPose.SLEEPING);
			this.setSleepingPosition(pos);
			this.setVelocity(Vec3d.ZERO);
			this.velocityDirty = true;
		}
		else {
			this.releaseBed();
		}
	}

	private void setPositionInBed() {
		if (this.isSleeping() && this.getSleepingPosition().isPresent()) {
			this.setPositionInBed(this.getSleepingPosition().get(), this.getSleepingDirection());
		}
	}

	protected void setPositionInBed(final BlockPos pos, final Direction direction) {
		this.setPosition(
				pos.getX() + 0.5,
				pos.getY() + 0.6875,
				pos.getZ() + 0.5);
	}

	public void reserveBed(final BlockPos pos) {
		this.releaseBed();
		if (AmaziaBlockProperties.setBedReservation(this.world, pos, true)) {
			this.bedLocation = pos;
		}
	}

	public void releaseBed() {
		if (this.bedLocation != null) {
			AmaziaBlockProperties.setBedReservation(this.world, this.bedLocation, false);
			this.bedLocation = null;
		}
	}

	public boolean hasBedLoc() {
		return this.bedLocation != null;
	}

	public BlockPos getBedAccessPoint() {
		return this.village.getPathingGraph().getAccessPoint(this.bedLocation);
	}

	public BlockPos getBedLocation() {
		return this.bedLocation;
	}


	// Forwarders
	@Override
	public float getMaxVillagerHealth() {
		return super.getMaxHealth();
	}

	@Override
	public Text getCustomName() {
		return super.getCustomName();
	}

	@Override
	public void jump() {
		super.jump();
	}

	public String getLastName() {
		return Titles.getLastName(this.getCustomName().getString());
	}
}
