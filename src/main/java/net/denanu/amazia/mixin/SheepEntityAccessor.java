package net.denanu.amazia.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.DyeColor;

@Mixin(SheepEntity.class)
public interface SheepEntityAccessor {
	@Accessor("DROPS")
	public static Map<DyeColor, ItemConvertible> getDrops() {
		throw new AssertionError();
	};
}
