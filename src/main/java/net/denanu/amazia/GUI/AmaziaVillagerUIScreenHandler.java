package net.denanu.amazia.GUI;

import java.util.ArrayList;
import java.util.Collection;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.mechanics.AmaziaMechanicsGuiEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class AmaziaVillagerUIScreenHandler extends ScreenHandler {
	AmaziaMechanicsGuiEntity entity;
	Collection<StatusEffectInstance> activeStatusEffects = new ArrayList<>();
	PropertyDelegate deleget;

	public AmaziaVillagerUIScreenHandler(final ScreenHandlerType<?> type, final int syncId, final PacketByteBuf buf) {
		super(type, syncId);
		buf.readUuid();
	}

	public AmaziaVillagerUIScreenHandler(final int syncId, final PlayerInventory playerInventory,
			final PacketByteBuf buf) {
		this(syncId, playerInventory, null, new ArrayPropertyDelegate(AmaziaVillagerEntity.propertiCount()),
				buf.readCollection(ArrayList<StatusEffectInstance>::new,
						buf2 -> StatusEffectInstance.fromNbt(buf2.readNbt())));
	}

	public AmaziaVillagerUIScreenHandler(final int syncId, final PlayerInventory playerInventory,
			final AmaziaMechanicsGuiEntity entity, final PropertyDelegate delegate,
			final Collection<StatusEffectInstance> effects) {
		super(AmaziaScreens.VILLAGER_SCREEN_HANDLER, syncId);
		this.entity = entity;
		this.deleget = delegate;
		this.addProperties(delegate);
		this.activeStatusEffects = effects;
	}

	@Override
	public ItemStack transferSlot(final PlayerEntity var1, final int var2) {
		return ItemStack.EMPTY;
	}

	public int getHealth() {
		return this.deleget.get(0);
	}

	public int getHunger() {
		return this.deleget.get(1);
	}

	public int getIntelligence() {
		return this.deleget.get(2);
	}

	public int getEducation() {
		return this.deleget.get(3);
	}

	@Override
	public boolean canUse(final PlayerEntity var1) {
		return true;
	}

	public int getProfessionLevel(final int idx) {
		return this.deleget.get(idx + AmaziaVillagerEntity.nonProfessionPropertyCounts());
	}

	public void tick() {

	}
}
