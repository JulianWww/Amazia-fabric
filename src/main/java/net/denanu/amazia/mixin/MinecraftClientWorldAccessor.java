package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.world.ClientEntityManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

@Mixin(ClientWorld.class)
public interface MinecraftClientWorldAccessor {
	@Accessor
	public ClientEntityManager<Entity> getEntityManager();
}
