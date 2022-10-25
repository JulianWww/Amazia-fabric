package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;
import net.denanu.amazia.entities.village.server.goal.storage.DepositItemGoal;
import net.denanu.amazia.entities.village.server.goal.storage.GetItemGoal;
import net.denanu.amazia.utils.crafting.CraftingUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;

public abstract class AmaziaVillagerEntity extends AmaziaEntity implements InventoryOwner {
	private final SimpleInventory inventory = new SimpleInventory(8);
	private List<Item> requestedItems;
	private CraftingRecipe wantsToCraft;
	private Map<Item, Integer> craftInput;
	private boolean isDeposeting = false;
	
	protected AmaziaVillagerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
		this.setCanPickUpLoot(true);
		this.requestedItems = new ArrayList<Item>();
		this.craftInput = null;
	}
	
	public static DefaultAttributeContainer.Builder setAttributes() {
        return PassiveEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }
	
	public void registerBaseGoals() {
		this.goalSelector.add(24, new CraftGoal(this, 24));
		this.goalSelector.add(25, new GetItemGoal(this, 25));
		this.goalSelector.add(99, new DepositItemGoal(this, 99));
        this.goalSelector.add(100, new LookAroundGoal(this));
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
        	ItemStack itemStack = (ItemStack)this.inventory.getStack(i);
        	if (itemStack.isEmpty()) continue;
        	this.dropItem(itemStack, true, false);
        	this.inventory.setStack(i, ItemStack.EMPTY);
        }
    }
	@Nullable
    protected ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership) {
        if (stack.isEmpty()) {
            return null;
        }
        if (this.world.isClient) {
            this.swingHand(Hand.MAIN_HAND);
        }
        double d = this.getEyeY() - (double)0.3f;
        ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), d, this.getZ(), stack);
        itemEntity.setPickupDelay(40);
        if (retainOwnership) {
            itemEntity.setThrower(this.getUuid());
        }
        if (throwRandomly) {
            float f = this.random.nextFloat() * 0.5f;
            float g = this.random.nextFloat() * ((float)Math.PI * 2);
            itemEntity.setVelocity(-MathHelper.sin(g) * f, 0.2f, MathHelper.cos(g) * f);
        } else {
            float g = MathHelper.sin(this.getPitch() * ((float)Math.PI / 180));
            float h = MathHelper.cos(this.getPitch() * ((float)Math.PI / 180));
            float i = MathHelper.sin(this.getYaw() * ((float)Math.PI / 180));
            float j = MathHelper.cos(this.getYaw() * ((float)Math.PI / 180));
            float k = this.random.nextFloat() * ((float)Math.PI * 2);
            float l = 0.02f * this.random.nextFloat();
            itemEntity.setVelocity((double)(-i * h * 0.3f) + Math.cos(k) * (double)l, -g * 0.3f + 0.1f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f, (double)(j * h * 0.3f) + Math.sin(k) * (double)l);
        }
        return itemEntity;
    }
    protected void vanishCursedItems() {
        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (itemStack.isEmpty() || !EnchantmentHelper.hasVanishingCurse(itemStack)) continue;
            this.inventory.removeStack(i);
        }
    }
    
    @Override
    public boolean cannotDespawn() {
    	return true;
    }
	
	@Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Inventory", this.inventory.toNbtList());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
    }

	@Override
	public PassiveEntity createChild(ServerWorld arg0, PassiveEntity arg1) {
		return null;
	}
	
	public SimpleInventory getInventory() {
		return inventory;
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
	
	
	public abstract Triplet<ItemStack, Integer, Integer> getDepositableItems();
	public HashMap<Item,ArrayList<CraftingRecipe>> getCraftables() { return null; };
	
	protected Map<Item, Integer> getItemCounts(final Map<Item, Integer> minItems) {
		Map<Item, Integer> count = new HashMap<Item, Integer>();
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			ItemStack itm = this.getInventory().getStack(idx);
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
		//boolean f = !this.hasFreeSlot();
		return isDeposeting || !this.hasFreeSlot();
	}

	public void setDeposeting(boolean isDeposeting) {
		this.isDeposeting = isDeposeting;
	}

	protected int countItems(Item itm) {
		int counter = 0;
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			ItemStack stack = this.getInventory().getStack(idx);
			if (stack.getItem().equals(itm)) {
				counter = counter + stack.getCount();
			}
		}
		return counter;
	}
	
	protected Triplet<ItemStack, Integer, Integer> getDepositableItems(final Set<Item> toIgnore, final Map<Item, Integer> minItems) {
		Map<Item, Integer> itemCounts = getItemCounts(minItems);
		int counter = 0;
		Triplet<ItemStack, Integer, Integer> out = null;
		
		for (int idx=0; idx < this.getInventory().size(); idx++) {
			ItemStack itm = this.getInventory().getStack(idx);
			if (!toIgnore.contains(itm.getItem()) && !itm.isOf(Items.AIR)) {
				if (itm.getCount() > counter && (itemCounts.containsKey(itm.getItem()) ? itemCounts.get(itm.getItem()) : 0) > 0) {
					out = new Triplet<>(itm, idx, Math.min(itm.getCount(), (itemCounts.containsKey(itm.getItem()) ? itemCounts.get(itm.getItem()) : 0)));
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
	public void requestItem(Item itm) {
		if (!this.requestedItems.contains(itm) && !this.wantsToCraft()) {
			this.requestedItems.add(itm);
		}
	}
	public void removeRequestedItem(Item itm) {
		this.requestedItems.remove(itm);
	}
	public void clearRequestItemQueue() {
		this.requestedItems.clear();
	}
	public boolean hasRequestedItems() {
		return this.requestedItems.size() > 0 && this.getEmptyInventorySlots() > 0;
	}
	
	public CraftingRecipe getWantsToCraft() {
		return wantsToCraft;
	}
	
	public void tryCraftingStart(Item itm) {
		if (this.getCraftables().containsKey(itm)) {
			CraftingRecipe recipy = JJUtils.getRandomListElement(this.getCraftables().get(itm));
			ArrayList<Item> ingredients = new ArrayList<Item>();
			this.craftInput = CraftingUtils.getRecipyInput(recipy);
			boolean canCraft = true;
			for (Entry<Item, Integer> ingredient : this.craftInput.entrySet()) {
				if (this.hasItem(ingredient.getKey(), ingredient.getValue())) {
					continue;
				}
				if (this.getVillage().getStorage().itemInStorage((ServerWorld)this.getWorld(), ingredient.getKey())) {
					ingredients.add(ingredient.getKey());
					continue;
				}
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
		return craftInput;
	}

	public void endCraft() {
		this.craftInput = null;
		this.wantsToCraft = null;
	}
	
	public void damage(int idx) {
		if (this.getInventory().getStack(idx).damage(1, random, null)) {
			this.getInventory().setStack(idx, ItemStack.EMPTY);
		}
	}

	public int removeItemFromInventory(Item itm, int count) {
		for (int idx = this.inventory.size() - 1; idx >= 0 && count > 0; idx--) {
			ItemStack stack = this.inventory.getStack(idx);
			if (stack.isOf(itm) && !stack.isEmpty()) {
				int delta = Math.min(stack.getCount(), count);
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
	public boolean hasItem(Item itm, int count) {
		return this.inventory.count(itm) >= count;
	}
	
	@Override
    public void tickMovement() {
        super.tickMovement();
    }
	
	protected boolean shouldPickUp(ItemStack stack) {
		return true;
	}
	
	@Override
    protected void loot(ItemEntity item) {
        if (this.shouldPickUp(item.getStack())) { InventoryOwner.pickUpItem(this, this, item); }
    }
	
	protected boolean hasFreeSlot() {
		for (int i = 0; i < this.getInventory().size(); ++i) {
            ItemStack itemStack = this.getInventory().getStack(i);
            if (itemStack.isEmpty()) {
            	return true;
            }
		}
		return false;
	}
	
	@Override
    public boolean canGather(ItemStack stack) {
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
		return 20;
	}
	public int getCraftingTime() {
		return 20;
	}
}
