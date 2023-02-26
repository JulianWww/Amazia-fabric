package net.denanu.amazia.mixin;

import java.util.List;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.advancement.Advancement;
import net.minecraft.data.server.AdvancementProvider;

@Mixin(AdvancementProvider.class)
public interface AdvancementProviderAccessor {
	@Accessor List<Consumer<Consumer<Advancement>>> getTabGenerators();
}
