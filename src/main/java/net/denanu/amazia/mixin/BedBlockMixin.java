package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.block.AmaziaBlockProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.state.StateManager;
import net.minecraft.util.DyeColor;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin extends HorizontalFacingBlock {
	protected BedBlockMixin(final Settings settings) {
		super(settings);
	}

	@Inject(method="appendProperties", at=@At("TAIL"))
	public void appendProperties(final StateManager.Builder<Block, BlockState> builder, final CallbackInfo info) {
		builder.add(AmaziaBlockProperties.RESERVED);
	}

	@Inject(method="<init>", at=@At("TAIL"))
	public void setup(final DyeColor color, final AbstractBlock.Settings settings, final CallbackInfo info) {
		this.setDefaultState(this.getDefaultState().with(AmaziaBlockProperties.RESERVED, false));
	}
}
