package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(BarrelBlockEntity.class)
public interface BarrelInventoryAccessMixin {
	@Accessor
	DefaultedList<ItemStack> getInventory();
}
