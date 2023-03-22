package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;

@Mixin(PathAwareEntity.class)
public class PathAwareEntityMixin extends MobEntity {
	protected PathAwareEntityMixin() {
		super(null, null);
	}

	@Inject(method="updateLeash", at=@At(value="INVOKE", target="Lnet/minecraft/entity/mob/PathAwareEntity;detachLeash(ZZ)V"), cancellable=true)
	public void updateLeashInject(final CallbackInfo callinfo) {
		final PathAwareEntity entity = (PathAwareEntity)(Object)this;
		if (entity.getHoldingEntity() instanceof final AmaziaEntity holder) {
			entity.teleport(
					holder.getX(),
					holder.getY(),
					holder.getZ()
					);
			callinfo.cancel();
		}
	}
}
