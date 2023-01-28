package net.denanu.amazia.village;

import java.util.Collection;
import java.util.HashSet;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.GUI.renderers.VillageBorderRenderer;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaIds;
import net.denanu.amazia.pathing.PathingGraph;
import net.denanu.amazia.village.events.EventData;
import net.denanu.amazia.village.events.IVillageEventListener;
import net.denanu.amazia.village.events.VillageEvents;
import net.denanu.amazia.village.sceduling.AbstractFurnaceSceduler;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
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
	private final PathingNoHeightSceduler enchanting;
	private final PathingNoHeightSceduler desks;
	private final AbstractFurnaceSceduler smelting;
	private final AbstractFurnaceSceduler blasting;
	private final AbstractFurnaceSceduler smoking;
	private final PathingNoHeightSceduler blacksmithing;
	private final GuardSceduler guarding;
	private final PathingNoHeightSceduler library;

	private PathingGraph pathingGraph;
	private final HashSet<IVillageEventListener> listeners = new HashSet<>();

	@Nullable
	private final VillageCoreBlockEntity coreBlock;

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
		this.smelting	 	= new AbstractFurnaceSceduler	(this, Blocks.FURNACE.getClass(),				BlockHighlightingAmaziaIds.NORMAL_FURNACES);
		this.blasting	 	= new AbstractFurnaceSceduler	(this, Blocks.BLAST_FURNACE.getClass(), 		BlockHighlightingAmaziaIds.BLAST_FURNACES);
		this.smoking	 	= new AbstractFurnaceSceduler	(this, Blocks.SMOKER.getClass(), 				BlockHighlightingAmaziaIds.SMOKER_FURNACES);
		this.blacksmithing	= new PathingNoHeightSceduler	(this, ScedulingPredicates::isAnvil, 			BlockHighlightingAmaziaIds.FORGE);
		this.guarding		= new GuardSceduler				(this);
		this.library	 	= new PathingNoHeightSceduler	(this, ScedulingPredicates::isBookShelf, 		BlockHighlightingAmaziaIds.LIBRARY);

		this.valid = true;

		this.boundingBox = new Box(
				core.getPos().getX() + Village.SIZE,
				400,
				core.getPos().getZ() + Village.SIZE,
				core.getPos().getX() - Village.SIZE,
				-400,
				core.getPos().getZ() - Village.SIZE
				);
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

		this.register(this.coreBlock.getWorld());
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

	public static int getSize() {
		return Village.SIZE;
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
}
