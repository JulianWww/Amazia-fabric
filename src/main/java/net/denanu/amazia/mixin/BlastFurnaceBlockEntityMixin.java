package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@Mixin(BlastFurnaceBlockEntity.class)
public class BlastFurnaceBlockEntityMixin extends AbstractFurnaceBlockEntity{
	protected BlastFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state,
			RecipeType<? extends AbstractCookingRecipe> recipeType) {
		super(blockEntityType, pos, state, recipeType);
	}

	@Override
	@Inject(method="setStack", at=@At("TAIL"))
	public void setStack(int slot, ItemStack stack) {
		
	}

	@Override
	protected Text getContainerName() {
		return null;
	}

	@Override
	protected ScreenHandler createScreenHandler(int var1, PlayerInventory var2) {
		return null;
	}
}
