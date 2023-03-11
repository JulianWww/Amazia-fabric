package net.denanu.amazia.block.entity;

import net.denanu.amazia.GUI.TakeOnlyContainerScreenHandler;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VillageCoreBlockEntity extends LootableContainerBlockEntity {
	private Village village;
	private boolean initialized = false;
	public VillageCoreBlockEntity(final BlockPos pos, final BlockState state) {
		super(AmaziaBlockEntities.VILLAGE_CORE, pos, state);
		this.createVillage();
	}

	@Override
	protected void writeNbt(final NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("village", this.village.writeNbt());
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		super.readNbt(nbt);
		this.createVillage();
		this.village.readNbt(nbt.getCompound("village"));
	}

	@Override
	public int size() {
		return VillageCoreBlockEntity._size();
	}

	public static int _size() {
		return 9;
	}

	public Village getVillage() {
		return this.village;
	}

	private void createVillage() {
		this.village = new Village(this);
	}

	private void initialize() {
		if (!this.initialized) {
			this.village.setupVillage();
			this.village.getPathingGraph().seedVillage(this.pos.up());
			this.initialized = true;
		}
	}

	public static void tick(final World world, final BlockPos pos, final BlockState state, final VillageCoreBlockEntity e) {
		if (!world.isClient()) {
			e.initialize();
			e.village.tick((ServerWorld)world);
		}
	}

	public void _setChanged() {
		this.markDirty();
	}

	@Override
	public void markRemoved() {
		super.markRemoved();
		this.village.remove(this.getWorld());
	}

	@Override
	public void cancelRemoval() {
		super.cancelRemoval();
		this.village.register(this.getWorld());
	}


	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.village.getInventory();
	}

	@Override
	protected void setInvStackList(final DefaultedList<ItemStack> inv) {
		this.village.setInventory(inv);
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("amazia.village");
	}

	@Override
	protected ScreenHandler createScreenHandler(final int syncId, final PlayerInventory playerInventory) {
		return new TakeOnlyContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, playerInventory, this, 1);
	}
}
