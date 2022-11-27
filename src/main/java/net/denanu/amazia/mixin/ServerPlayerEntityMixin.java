package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	public ServerPlayerEntityMixin() {
		super(null, null, 1.0f, null, null);
		throw new RuntimeException("invalidConstructor");
	}

	@Inject(method="damage", at = @At("RETURN"), cancellable = true)
	public void damageMixin(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			final ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
			final Village village = Amazia.getVillageManager().getVillage(player.getBlockPos());
			if (village != null) {
				village.getGuarding().addOpponent(source.getAttacker(), 5);
			}
		}
	}

	@Inject(method="attack", at = @At(value="TAIL"))
	public void attackMixin(final Entity target, final CallbackInfo info) {
		final ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		final Village village = Amazia.getVillageManager().getVillage(player.getBlockPos());
		if (village != null) {
			village.getGuarding().addOpponent(target, 1);
		}
	}
}
