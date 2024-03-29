package net.denanu.amazia.village;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.GUI.renderers.VillageBorderRenderer;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.entities.village.merchant.AmaziaVillageMerchant;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaIds;
import net.denanu.amazia.item.AmaziaItems;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.village.events.EventData;
import net.denanu.amazia.village.events.IVillageEventListener;
import net.denanu.amazia.village.events.VillageEvents;
import net.denanu.amazia.village.sceduling.AbstractFurnaceSceduler;
import net.denanu.amazia.village.sceduling.BedSceduler;
import net.denanu.amazia.village.sceduling.FarmingSceduler;
import net.denanu.amazia.village.sceduling.GuardSceduler;
import net.denanu.amazia.village.sceduling.LumberSceduler;
import net.denanu.amazia.village.sceduling.MineingSceduler;
import net.denanu.amazia.village.sceduling.PathingNoHeightSceduler;
import net.denanu.amazia.village.sceduling.RancherSceduler;
import net.denanu.amazia.village.sceduling.ScedulingPredicates;
import net.denanu.amazia.village.sceduling.StorageSceduler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Village {
	private static int SIZE = 120;
	private BlockPos origin;
	private boolean valid;

	private final FarmingSceduler farming;
	private final StorageSceduler storage;
	private final MineingSceduler mineing;
	private final LumberSceduler  lumber;
	private final RancherSceduler ranching;
	private final PathingNoHeightSceduler enchanting, desks, chair;
	private final BedSceduler beds;
	private final AbstractFurnaceSceduler smelting;
	private final AbstractFurnaceSceduler blasting;
	private final AbstractFurnaceSceduler smoking;
	private final PathingNoHeightSceduler blacksmithing;
	private final GuardSceduler guarding;
	private final PathingNoHeightSceduler library;

	private long init_time;

	private UUID mayor;

	private PathingGraph pathingGraph;
	private final HashSet<IVillageEventListener> listeners = new HashSet<>();

	private AmaziaVillageMerchant merchant;

	@Nullable
	private final VillageCoreBlockEntity coreBlock;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(VillageCoreBlockEntity._size(), ItemStack.EMPTY);

	private final Box boundingBox;

	public Village(final VillageCoreBlockEntity core) {
		this.coreBlock = core;
		this.farming 		= new FarmingSceduler  			(this);
		this.storage 		= new StorageSceduler   		(this);
		this.mineing 		= new MineingSceduler   		(this);
		this.lumber 		= new LumberSceduler    		(this);
		this.ranching		= new RancherSceduler   		(this);
		this.enchanting 	= new PathingNoHeightSceduler	(this, ScedulingPredicates::isEnchantingTable, 	BlockHighlightingAmaziaIds.ENCHANTING);
		this.desks		 	= new PathingNoHeightSceduler	(this, ScedulingPredicates::isDesk, 			BlockHighlightingAmaziaIds.DESK);
		this.chair			= new PathingNoHeightSceduler	(this, ScedulingPredicates::isChair, 			BlockHighlightingAmaziaIds.CHAIR);
		this.beds			= new BedSceduler				(this, ScedulingPredicates::isBed, 				BlockHighlightingAmaziaIds.BEDS);
		this.smelting	 	= new AbstractFurnaceSceduler	(this, Blocks.FURNACE.getClass(),				BlockHighlightingAmaziaIds.NORMAL_FURNACES);
		this.blasting	 	= new AbstractFurnaceSceduler	(this, Blocks.BLAST_FURNACE.getClass(), 		BlockHighlightingAmaziaIds.BLAST_FURNACES);
		this.smoking	 	= new AbstractFurnaceSceduler	(this, Blocks.SMOKER.getClass(), 				BlockHighlightingAmaziaIds.SMOKER_FURNACES);
		this.blacksmithing	= new PathingNoHeightSceduler	(this, ScedulingPredicates::isAnvil, 			BlockHighlightingAmaziaIds.FORGE);
		this.guarding		= new GuardSceduler				(this);
		this.library	 	= new PathingNoHeightSceduler	(this, ScedulingPredicates::isBookShelf, 		BlockHighlightingAmaziaIds.LIBRARY);

		this.valid = true;

		this.boundingBox = Village.getBox(core.getPos());
	}

	public static Box getBox(final BlockPos pos) {
		return new Box(
				pos.getX() + Village.SIZE,
				400,
				pos.getZ() + Village.SIZE,
				pos.getX() - Village.SIZE,
				-400,
				pos.getZ() - Village.SIZE
				);
	}

	public void setMayor(final UUID usr) {
		this.mayor = usr;
		this.setChanged();
	}

	public UUID getMayorUUID() {
		return this.mayor;
	}

	@SuppressWarnings("resource")
	public ServerPlayerEntity getMayor() {
		if (this.pathingGraph != null) {
			for (final ServerPlayerEntity player : this.getWorld().getServer().getPlayerManager().getPlayerList()) {
				if (player.getGameProfile().getId().equals(this.mayor)) {
					return player;
				}
			}
		}
		return null;
	}

	public void setupVillage() {
		this.origin = this.coreBlock.getPos();

		this.pathingGraph = new PathingGraph((ServerWorld)this.coreBlock.getWorld(), this);

		this.farming.initialize();
		this.storage.initialize();
		this.mineing.initialize();
		this.lumber.initialize();
		this.ranching.initialize();
		this.enchanting.initialize();
		this.smelting.initialize();
		this.blasting.initialize();
		this.smoking.initialize();
		this.blacksmithing.initialize();
		this.library.initialize();
		this.desks.initialize();
		this.chair.initialize();
		this.beds.initialize();

		this.register(this.coreBlock.getWorld());

		if (this.mayor == null) {
			final PlayerEntity newMayor = this.getWorld().getClosestPlayer(
					this.origin.getX(),
					this.origin.getY(),
					this.origin.getZ(),
					10,
					false);
			if (newMayor != null) {
				this.mayor = newMayor.getGameProfile().getId();
			}
		}

		this.init_time = this.coreBlock.getWorld().getTime();
	}

	public void remove(final World world) {
		if (!world.isClient()) {
			Amazia.getVillageManager().removeVillage(this);
		}
		else {
			VillageBorderRenderer.villageBoxes.remove(new VillageBoundingBox(this.getBox()));
		}
	}
	public void register(final World world) {
		if (!world.isClient()) {
			Amazia.getVillageManager().addVillage(this);
		}
		else {
			VillageBorderRenderer.villageBoxes.add(new VillageBoundingBox(this.getBox()));
		}
	}

	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.putBoolean("isValid", 	this.valid);
		nbt.put("farming",   		this.farming.   	writeNbt());
		nbt.put("storage",   		this.storage.  		writeNbt());
		nbt.put("mineing",			this.mineing.   	writeNbt());
		nbt.put("lumber",     		this.lumber.    	writeNbt());
		nbt.put("ranching",  		this.ranching.  	writeNbt());
		nbt.put("enchanting",		this.enchanting.	writeNbt());
		nbt.put("smelting", 		this.smelting.		writeNbt());
		nbt.put("blasting", 		this.blasting.		writeNbt());
		nbt.put("smoking", 			this.smoking.		writeNbt());
		nbt.put("blacksmithing",	this.blacksmithing.	writeNbt());
		nbt.put("guarding",			this.guarding.		writeNbt());
		nbt.put("library",			this.library.		writeNbt());
		nbt.put("desk", 			this.desks.			writeNbt());
		nbt.put("chair", 			this.chair.			writeNbt());
		nbt.put("bed",				this.beds.			writeNbt());
		nbt.putLong("init_time", 	this.init_time);

		Inventories.writeNbt(nbt, this.inventory);

		if (this.mayor != null) {
			nbt.putUuid("mayor",	this.mayor);
		}
		return nbt;
	}
	public void readNbt(final NbtCompound nbt) {
		this.valid = 		nbt.getBoolean("isValid");
		this.farming.   	readNbt(nbt.getCompound("farming"));
		this.storage.   	readNbt(nbt.getCompound("storage"));
		this.mineing.   	readNbt(nbt.getCompound("mineing"));
		this.lumber.    	readNbt(nbt.getCompound("lumber"));
		this.ranching. 		readNbt(nbt.getCompound("ranching"));
		this.enchanting.	readNbt(nbt.getCompound("enchanting"));
		this.smelting.		readNbt(nbt.getCompound("smelting"));
		this.blasting.		readNbt(nbt.getCompound("blasting"));
		this.smoking.		readNbt(nbt.getCompound("smoking"));
		this.blacksmithing.	readNbt(nbt.getCompound("blacksmithing"));
		this.guarding.		readNbt(nbt.getCompound("guarding"));
		this.library.		readNbt(nbt.getCompound("library"));
		this.desks.			readNbt(nbt.getCompound("desk"));
		this.chair.			readNbt(nbt.getCompound("chair"));
		this.beds.			readNbt(nbt.getCompound("bed"));
		this.mayor			= nbt.contains("mayor") ? nbt.getUuid("mayor") : null;
		this.init_time		= nbt.contains("init_time") ? nbt.getLong("init_time") : this.getWorld().getTime();

		Inventories.readNbt(nbt, this.inventory);
	}

	public boolean isValid() {
		return this.valid;
	}

	public void destroy() {
		this.valid = false;
	}

	public void tick(final ServerWorld world) {
		this.update();
	}

	private void update() {
		this.pathingGraph.update();

		if (this.getWorld().isDay() && this.merchant == null) {
			this.spawnMerchant();
		}
		else if (this.getWorld().isNight() && this.merchant != null) {
			this.merchant.leave();
			this.merchant = null;
		}
	}

	@Debug
	private void spawnMerchant() {
		if (this.getWorld().getTime() > this.init_time + 100) {
			final BlockPos pos = this.pathingGraph.getRandomVillageEnterNode();
			if (pos != null) {
				final AmaziaVillageMerchant merchant = AmaziaVillageMerchant.of(this.getWorld(), this, pos);
				this.getWorld().spawnEntity(merchant);

				final ServerPlayerEntity mayor = this.getMayor();
				if (mayor != null) {
					mayor.teleport(merchant.getX(), merchant.getY(), merchant.getZ());
				}
			}
		}
	}

	private void discover(final ServerWorld world, final BlockPos blockPos) {
		this.farming.   	discover(world, blockPos);
		this.storage.   	discover(world, blockPos);
		this.mineing.   	discover(world, blockPos);
		this.lumber.    	discover(world, blockPos);
		this.ranching.  	discover(world, blockPos);
		this.enchanting.	discover(world, blockPos);
		this.smelting.		discover(world, blockPos);
		this.blasting.		discover(world, blockPos);
		this.smoking.		discover(world, blockPos);
		this.blacksmithing.	discover(world, blockPos);
		this.library.		discover(world, blockPos);
		this.desks.			discover(world, blockPos);
		this.chair.			discover(world, blockPos);
		this.beds.			discover(world, blockPos);
	}


	public void setChanged() {
		if (this.coreBlock != null) {
			this.coreBlock._setChanged();
		}
	}

	public BlockPos getOrigin() {
		return this.origin;
	}
	public PathingGraph getPathingGraph() {
		return this.pathingGraph;
	}
	public FarmingSceduler getFarming() {
		return this.farming;
	}
	public StorageSceduler getStorage() {
		return this.storage;
	}
	public MineingSceduler getMineing() {
		return this.mineing;
	}
	public LumberSceduler getLumber() {
		return this.lumber;
	}
	public RancherSceduler getRanching() {
		return this.ranching;
	}
	public PathingNoHeightSceduler getEnchanting() {
		return this.enchanting;
	}
	public AbstractFurnaceSceduler getSmelting() {
		return this.smelting;
	}
	public AbstractFurnaceSceduler getBlasting() {
		return this.blasting;
	}
	public AbstractFurnaceSceduler getSmoking() {
		return this.smoking;
	}
	public PathingNoHeightSceduler getBlacksmithing() {
		return this.blacksmithing;
	}
	public GuardSceduler getGuarding() {
		return this.guarding;
	}
	public PathingNoHeightSceduler getLibrary() {
		return this.library;
	}
	public PathingNoHeightSceduler getDesk() {
		return this.desks;
	}
	public PathingNoHeightSceduler getChairs() {
		return this.chair;
	}
	public PathingNoHeightSceduler getBeds() {
		return this.beds;
	}

	public static int getSize() {
		return Village.SIZE;
	}

	public AmaziaVillageMerchant getMerchant() {
		return this.merchant;
	}

	public void setMerchant(final AmaziaVillageMerchant merchant) {
		this.merchant = merchant;
	}

	public DefaultedList<ItemStack> getInventory() {
		return this.inventory;
	}

	public void setInventory(final DefaultedList<ItemStack> inventory) {
		this.inventory = inventory;
	}

	public boolean isInVillage(final BlockPos pos) {
		if (this.getOrigin() == null) {
			return false;
		}
		return this.boundingBox.contains(pos.getX(), pos.getY(), pos.getZ());
	}

	public boolean isInVillage(final Vec3d pos) {
		if (this.getOrigin() == null) {
			return false;
		}
		return this.boundingBox.contains(pos);
	}

	public boolean isInVillage(final Entity entity) {
		return this.isInVillage(entity.getBlockPos());
	}

	public void onPathingBlockUpdate(final BlockPos pos) {
		if (this.getPathingGraph() != null && this.isInVillage(pos)) {
			final ServerWorld world = this.getWorld();
			this.pathingGraph.onBlockUpdate(world, pos);
		}
	}

	public ServerWorld getWorld() {
		return this.pathingGraph.getWorld();
	}

	public Box getBox() {
		return this.boundingBox;
	}

	public void onVillageBlockUpdate(final BlockPos pos) {
		if (this.isInVillage(pos)) {
			this.discover(this.getWorld(), pos);
		}
	}

	public void addThreat(final MobEntity enemy) {
		this.guarding.addOpponent(enemy, 1);
	}



	// Event Handeling

	public void emmitEvent(final VillageEvents event, final Entity emmiter) {
		this.emmitEvent(event, new EventData(emmiter));
	}

	public void emmitEvent(final VillageEvents event, final EventData eventData) {
		for (final IVillageEventListener listener : this.listeners) {
			listener.receiveEvent(event, eventData);
		}
	}

	public void removeListener(final IVillageEventListener listener) {
		this.listeners.remove(listener);
	}

	public void registerListener(final IVillageEventListener listener) {
		this.listeners.add(listener);
	}

	public Collection<IVillageEventListener> getListeners() {
		return this.listeners;
	}

	public void addItem(final ItemStack stack) {
		for (int idx = 0; idx < this.inventory.size(); idx++) {
			final ItemStack slot = this.inventory.get(idx);
			if (slot.isEmpty()) {
				this.inventory.set(idx, stack);
				break;
			}
			if (ItemStack.areNbtEqual(slot, stack)) {
				slot.setCount(slot.getCount() + stack.getCount());
				if (slot.getCount() <= slot.getMaxCount()) {
					slot.setCount(0);
					break;
				}
				stack.setCount(slot.getCount() - slot.getMaxCount());
				slot.setCount(slot.getMaxCount());
			}
		}
		this.setChanged();
	}

	public void addChild(final AmaziaVillagerEntity villager) {
		final NbtCompound nbt = new NbtCompound();

		nbt.putString("last_name", villager.getLastName());
		nbt.putFloat("Intelligence", villager.getChildIntelligence());


		final ItemStack stack = new ItemStack(AmaziaItems.CHILD_SPANW_ITEM, 1);
		stack.setNbt(nbt);
		this.addItem(stack);
	}
}
