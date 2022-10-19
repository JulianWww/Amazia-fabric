package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(ChestBlockEntity.class)
public interface ChestInventoryAccessMixin {
	@Accessor
	DefaultedList<ItemStack> getInventory();
}
