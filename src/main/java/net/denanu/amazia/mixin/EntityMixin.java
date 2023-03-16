package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
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
	@Invoker("movementInputToVelocity")
	public static Vec3d movementInputToVelocity(final Vec3d movementInput, final float speed, final float yaw) {
		throw new AssertionError();
	}
}
