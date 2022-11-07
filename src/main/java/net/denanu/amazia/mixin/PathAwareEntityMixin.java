package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;

@Mixin(PathAwareEntity.class)
public class PathAwareEntityMixin extends MobEntity {
	protected PathAwareEntityMixin() {
		super(null, null);
	}

	@Inject(method="updateLeash", at=@At(value="INVOKE", target="detachLeash(ZZ)V"), cancellable=true)
	public void updateLeashInject(CallbackInfo callinfo) {
		PathAwareEntity entity = ((PathAwareEntity)(Object)this);
		if (entity.getHoldingEntity() instanceof AmaziaEntity holder) {
			Amazia.LOGGER.info("teleported entity");
			entity.teleport(
					holder.getX(),
					holder.getY(),
					holder.getZ()
				);
			callinfo.cancel();
		}
	}
}
