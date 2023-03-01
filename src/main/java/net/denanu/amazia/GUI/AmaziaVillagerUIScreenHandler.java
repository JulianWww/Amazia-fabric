package net.denanu.amazia.GUI;

import java.util.ArrayList;
import java.util.Collection;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.mechanics.AmaziaMechanicsGuiEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AmaziaVillagerUIScreenHandler extends ScreenHandler {
	Collection<StatusEffectInstance> activeStatusEffects = new ArrayList<>();
	PropertyDelegate deleget;
	AmaziaMechanicsGuiEntity entity;

	public AmaziaVillagerUIScreenHandler(final ScreenHandlerType<?> type, final int syncId, final PacketByteBuf buf) {
		super(type, syncId);
		buf.readUuid();
	}

	@SuppressWarnings("resource")
	public AmaziaVillagerUIScreenHandler(final int syncId, final PlayerInventory playerInventory,
			final PacketByteBuf buf) {
		this(
				syncId,
				playerInventory,
				(AmaziaMechanicsGuiEntity)MinecraftClient.getInstance().player.getWorld().getEntityById(buf.readInt()), new ArrayPropertyDelegate(AmaziaVillagerEntity.propertiCount()),
				buf.readCollection(ArrayList<StatusEffectInstance>::new,
						buf2 -> StatusEffectInstance.fromNbt(buf2.readNbt())));
	}

	public AmaziaVillagerUIScreenHandler(final int syncId, final PlayerInventory playerInventory,
			final AmaziaMechanicsGuiEntity entity, final PropertyDelegate delegate,
			final Collection<StatusEffectInstance> effects) {
		super(AmaziaScreens.VILLAGER_SCREEN_HANDLER, syncId);
		this.deleget = delegate;
		this.addProperties(delegate);
		this.entity = entity;
		this.activeStatusEffects = effects;
	}

	@Override
	public ItemStack transferSlot(final PlayerEntity var1, final int var2) {
		return ItemStack.EMPTY;
	}

	public int getHealth() {
		return this.deleget.get(0);
	}

	public float getMaxHealth() {
		return this.entity.getMaxHealth();
	}

	public int getHunger() {
		return this.deleget.get(1);
	}

	public float getMaxHunger() {
		return this.entity.getMaxHunger();
	}

	public int getHappyness() {
		return this.deleget.get(3);
	}

	public float getMaxHappyness() {
		return this.entity.getMaxHappyness();
	}

	public int getIntelligence() {
		return (int) this.entity.getIntelligence();
	}

	public float getMaxIntelligence() {
		return this.entity.maxIntelligence();
	}

	public int getEducation() {
		return this.deleget.get(2);
	}

	public float getMaxEducation() {
		return this.entity.maxEducation();
	}

	public Identifier getProfessionId() {
		return this.entity.getProfession();
	}

	public Identifier getProfessionTokenId() {
		final Identifier profession = this.getProfessionId();
		return Identifier.of(profession.getNamespace(), new StringBuilder().append("textures/item/tokens/").append(profession.getPath()).append(".png").toString());
	}

	@Override
	public boolean canUse(final PlayerEntity var1) {
		return true;
	}

	public int getProfessionLevel(final int idx) {
		return this.deleget.get(idx + AmaziaVillagerEntity.nonProfessionPropertyCounts());
	}

	public static Text getProfessionText(final int idx) {
		return Text.translatable(AmaziaProfessions.PROFESSIONS.get(idx).toTranslationKey());
	}

	public void tick() {

	}

	public Text getCustomName() {
		return this.entity.getCustomName();
	}
}
