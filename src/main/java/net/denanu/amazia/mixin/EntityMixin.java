package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.denanu.amazia.components.AmaziaEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityLike;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable,
EntityLike,
CommandOutput,
ComponentAccess {
	@Inject(method="adjustMovementForCollisions", at=@At("HEAD"), cancellable=true)
	public void adjustMovementForCollisions(final Vec3d movement, final CallbackInfoReturnable<Vec3d> cir) {
		if (!AmaziaEntityComponents.getCanCollide((Entity)(Object)this)) {
			cir.setReturnValue(movement);
		};
	}

	@Invoker("movementInputToVelocity")
	public static Vec3d movementInputToVelocity(final Vec3d movementInput, final float speed, final float yaw) {
		throw new AssertionError();
	}
}
