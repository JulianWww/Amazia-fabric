package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.EnchanterEntity;
import net.denanu.amazia.utils.Predicates;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(EnchantingTableBlockEntity.class)
public abstract class EnchantingTableBlockEntityMixin extends BlockEntity {
	public EnchantingTableBlockEntityMixin(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
		super(type, pos, state);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private static void tick(final World world, final BlockPos pos, final BlockState state,
			final EnchantingTableBlockEntity blockEntity, final CallbackInfo inf) {
		float g;
		blockEntity.pageTurningSpeed = blockEntity.nextPageTurningSpeed;
		blockEntity.lastBookRotation = blockEntity.bookRotation;

		final Box bbox = new Box(pos.add(3, 3, 3), pos.add(-3, -3, -3));

		LivingEntity playerEntity = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0,
				false);
		final LivingEntity enchanter = world.getClosestEntity(EnchanterEntity.class, Predicates.ALWAYS_TRUE, null,
				pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, bbox);

		if (playerEntity == null || enchanter != null
				&& playerEntity.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > enchanter
						.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) {
			playerEntity = enchanter;
		}

		if (playerEntity != null) {
			final double d = playerEntity.getX() - (pos.getX() + 0.5);
			final double e = playerEntity.getZ() - (pos.getZ() + 0.5);
			blockEntity.targetBookRotation = (float) MathHelper.atan2(e, d);
			blockEntity.nextPageTurningSpeed += 0.1f;
			if (blockEntity.nextPageTurningSpeed < 0.5f || JJUtils.rand.nextInt(40) == 0) {
				final float f = blockEntity.flipRandom;
				do {
					blockEntity.flipRandom += JJUtils.rand.nextInt(4) - JJUtils.rand.nextInt(4);
				} while (f == blockEntity.flipRandom);
			}
		} else {
			blockEntity.targetBookRotation += 0.02f;
			blockEntity.nextPageTurningSpeed -= 0.1f;
		}
		while (blockEntity.bookRotation >= (float) Math.PI) {
			blockEntity.bookRotation -= (float) Math.PI * 2;
		}
		while (blockEntity.bookRotation < (float) -Math.PI) {
			blockEntity.bookRotation += (float) Math.PI * 2;
		}
		while (blockEntity.targetBookRotation >= (float) Math.PI) {
			blockEntity.targetBookRotation -= (float) Math.PI * 2;
		}
		while (blockEntity.targetBookRotation < (float) -Math.PI) {
			blockEntity.targetBookRotation += (float) Math.PI * 2;
		}
		for (g = blockEntity.targetBookRotation - blockEntity.bookRotation; g >= (float) Math.PI; g -= (float) Math.PI
				* 2) {
		}
		while (g < (float) -Math.PI) {
			g += (float) Math.PI * 2;
		}
		blockEntity.bookRotation += g * 0.4f;
		blockEntity.nextPageTurningSpeed = MathHelper.clamp(blockEntity.nextPageTurningSpeed, 0.0f, 1.0f);
		++blockEntity.ticks;
		blockEntity.pageAngle = blockEntity.nextPageAngle;
		float h = (blockEntity.flipRandom - blockEntity.nextPageAngle) * 0.4f;
		h = MathHelper.clamp(h, -0.2f, 0.2f);
		blockEntity.flipTurn += (h - blockEntity.flipTurn) * 0.9f;
		blockEntity.nextPageAngle += blockEntity.flipTurn;
		inf.cancel();
	}

}
