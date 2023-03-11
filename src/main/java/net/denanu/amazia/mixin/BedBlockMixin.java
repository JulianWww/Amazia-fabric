package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.denanu.amazia.block.AmaziaBlockProperties;
import net.denanu.amazia.item.AmaziaItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

	@Inject(method="onUse", at=@At("TAIL"), cancellable=true)
	public void onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit, final CallbackInfoReturnable<ActionResult> inf) {
		if (player.getMainHandStack().isOf(AmaziaItems.CHILD_SPANW_ITEM)) {
			inf.setReturnValue(ActionResult.PASS);
			inf.cancel();
		}
	}
}
