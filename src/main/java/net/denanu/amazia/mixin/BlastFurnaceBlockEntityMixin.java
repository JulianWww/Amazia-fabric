package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.components.AmaziaBlockComponents;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.VillageManager;
import net.denanu.amazia.village.sceduling.AbstractFurnaceSceduler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class BlastFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {

	protected BlastFurnaceBlockEntityMixin(final BlockEntityType<?> blockEntityType, final BlockPos blockPos,
			final BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(method="setStack", at=@At("TAIL"))
	public void setStack(final int slot, final ItemStack stack, final CallbackInfo info) {
		if (slot == 0) {
			if (stack.isEmpty()) {
				for (final BlockPos villageLoc : AmaziaBlockComponents.getVillages(this)) {
					this.getHandler(this, villageLoc).makeAvailableFurnace(this.pos);
				}
			}
			else {
				for (final BlockPos villageLoc : AmaziaBlockComponents.getVillages(this)) {
					this.getHandler(this, villageLoc).removeAvailableFurnace(this.pos);
				}
			}
		}
	}
	private AbstractFurnaceSceduler getHandler(final BlastFurnaceBlockEntityMixin t, final BlockPos pos) {
		final Village village = VillageManager.getVillage(pos, (ServerWorld) this.world);
		return switch (BlockEntityType.getId(t.getType()).toString()) {
		case "minecraft:furnace" -> village.getSmelting();
		case "minecraft:blast_furnace" -> village.getBlasting();
		case "minecraft:smoker" -> village.getSmoking();
		default -> null;
		};
	}
}