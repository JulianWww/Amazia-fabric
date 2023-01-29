package net.denanu.amazia.block.entity;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.custom.TeachersDeskBlockCabinet;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DeskCabinetBlockEntity extends LootableContainerBlockEntity {
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

	public DeskCabinetBlockEntity(final BlockPos pos, final BlockState state) {
		super(AmaziaBlockEntities.CABINET_BLOCK, pos, state);
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	@Override
	protected void setInvStackList(final DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable(Amazia.MOD_ID + ".desk_cabinet");
	}

	@Override
	protected ScreenHandler createScreenHandler(final int syncId, final PlayerInventory playerInventory) {
		return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, playerInventory, this, 1);
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
	}

	@Override
	protected void writeNbt(final NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
	}

	@Override
	public boolean checkUnlocked(final PlayerEntity player) {
		return !this.getCachedState().get(TeachersDeskBlockCabinet.LOCKED) || player.isSpectator();
	}

	@Override
	public void onOpen(final PlayerEntity player)
	{
		this.setDoorState(this.getCachedState(), true);
	}

	@Override
	public void onClose(final PlayerEntity player)
	{
		this.setDoorState(this.getCachedState(), false);
	}

	private void setDoorState(final BlockState state, final boolean open)
	{
		final World level = this.getWorld();
		if(level != null)
		{
			level.setBlockState(this.getPos(), state.with(TeachersDeskBlockCabinet.OPEN, open), 3);
		}
	}
}
