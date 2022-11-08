package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public interface MinecraftServerWorldAccessor {
	@Accessor
	public ServerEntityManager<Entity> getEntityManager();
}
