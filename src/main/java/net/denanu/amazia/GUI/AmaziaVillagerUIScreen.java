package net.denanu.amazia.GUI;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AmaziaVillagerUIScreen extends HandledScreen<AmaziaVillagerUIScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(Amazia.MOD_ID, "textures/gui/villager2.png");

	public AmaziaVillagerUIScreen(final AmaziaVillagerUIScreenHandler handler, final PlayerInventory inventory, final Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 276;
	}

	@Override
	protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, AmaziaVillagerUIScreen.TEXTURE);
		final int i = (this.width - this.backgroundWidth) / 2;
		final int j = (this.height - this.backgroundHeight) / 2;
		DrawableHelper.drawTexture(matrices, i, j, this.getZOffset(), 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 512, 256);
	}

	@Override
	protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY) {
		this.textRenderer.draw(
				matrices,
				Text.literal("Health: ").append(Integer.toString(this.getScreenHandler().getHealth())),
				this.playerInventoryTitleX,
				this.titleY,
				0x404040);

		this.textRenderer.draw(
				matrices,
				Text.literal("Food: ").append(Integer.toString(this.getScreenHandler().getHunger())),
				this.playerInventoryTitleX,
				this.titleY+10,
				0x404040);

		this.textRenderer.draw(
				matrices,
				Text.literal("IQ: ").append(Integer.toString(this.getScreenHandler().getIntelligence())),
				this.playerInventoryTitleX,
				this.titleY+20,
				0x404040);

		this.textRenderer.draw(
				matrices,
				Text.literal("Education: ").append(Integer.toString(this.getScreenHandler().getEducation())),
				this.playerInventoryTitleX,
				this.titleY+30,
				0x404040);

		this.renderProfessions(matrices);
	}

	private void renderProfessions(final MatrixStack matrices) {
		for (int idx=0; idx < AmaziaProfessions.PROFESSIONS.size(); idx++) {
			final int lvl = this.getScreenHandler().getProfessionLevel(idx);
			if (lvl > 0) {
				this.textRenderer.draw(
						matrices,
						Text.translatable(AmaziaProfessions.PROFESSIONS.get(idx).toTranslationKey())
						.append(": ")
						.append(Integer.toString(lvl)),
						this.playerInventoryTitleX,
						this.titleY + 40 + 10*idx,
						0x404040);
			}
		}
	}

	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
		this.renderBackground(matrices);
		this.drawStatusEffects(matrices, mouseX, mouseY);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		super.init();
		// Center the title
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}


	// status effect rendering
	public boolean hideStatusEffectHud() {
		final int i = this.x + this.backgroundWidth + 2;
		final int j = this.width - i;
		return j >= 32;
	}

	private void drawStatusEffects(final MatrixStack matrices, final int mouseX, final int mouseY) {
		final int i = this.x + this.backgroundWidth + 2;
		final int j = this.width - i;
		final Collection<StatusEffectInstance> collection = this.entity.getStatusEffects();
		if (collection.isEmpty() || j < 32) {
			return;
		}
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		final boolean bl = j >= 120;
		int k = 33;
		if (collection.size() > 5) {
			k = 132 / (collection.size() - 1);
		}
		final List<StatusEffectInstance> iterable = Ordering.natural().sortedCopy(collection);
		this.drawStatusEffectBackgrounds(matrices, i, k, iterable, bl);
		this.drawStatusEffectSprites(matrices, i, k, iterable, bl);
		if (bl) {
			this.drawStatusEffectDescriptions(matrices, i, k, iterable);
		} else if (mouseX >= i && mouseX <= i + 33) {
			int l = this.y;
			StatusEffectInstance statusEffectInstance = null;
			for (final StatusEffectInstance statusEffectInstance2 : iterable) {
				if (mouseY >= l && mouseY <= l + k) {
					statusEffectInstance = statusEffectInstance2;
				}
				l += k;
			}
			if (statusEffectInstance != null) {
				final List<Text> list = List.of(this.getStatusEffectDescription(statusEffectInstance), Text.literal(StatusEffectUtil.durationToString(statusEffectInstance, 1.0f)));
				this.renderTooltip(matrices, list, Optional.empty(), mouseX, mouseY);
			}
		}
	}

	private void drawStatusEffectBackgrounds(final MatrixStack matrices, final int x, final int height, final Iterable<StatusEffectInstance> statusEffects, final boolean wide) {
		RenderSystem.setShaderTexture(0, HandledScreen.BACKGROUND_TEXTURE);
		int i = this.y;
		for (@SuppressWarnings("unused") final StatusEffectInstance statusEffectInstance : statusEffects) {
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			if (wide) {
				this.drawTexture(matrices, x, i, 0, 166, 120, 32);
			} else {
				this.drawTexture(matrices, x, i, 0, 198, 32, 32);
			}
			i += height;
		}
	}

	private void drawStatusEffectSprites(final MatrixStack matrices, final int x, final int height, final Iterable<StatusEffectInstance> statusEffects, final boolean wide) {
		final StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
		int i = this.y;
		for (final StatusEffectInstance statusEffectInstance : statusEffects) {
			final StatusEffect statusEffect = statusEffectInstance.getEffectType();
			final Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
			RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
			DrawableHelper.drawSprite(matrices, x + (wide ? 6 : 7), i + 7, this.getZOffset(), 18, 18, sprite);
			i += height;
		}
	}

	private void drawStatusEffectDescriptions(final MatrixStack matrices, final int x, final int height, final Iterable<StatusEffectInstance> statusEffects) {
		int i = this.y;
		for (final StatusEffectInstance statusEffectInstance : statusEffects) {
			final Text text = this.getStatusEffectDescription(statusEffectInstance);
			this.textRenderer.drawWithShadow(matrices, text, x + 10 + 18, i + 6, 0xFFFFFF);
			final String string = StatusEffectUtil.durationToString(statusEffectInstance, 1.0f);
			this.textRenderer.drawWithShadow(matrices, string, x + 10 + 18, i + 6 + 10, 0x7F7F7F);
			i += height;
		}
	}

	private Text getStatusEffectDescription(final StatusEffectInstance statusEffect) {
		final MutableText mutableText = statusEffect.getEffectType().getName().copy();
		if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
			mutableText.append(" ").append(Text.translatable("enchantment.level." + (statusEffect.getAmplifier() + 1)));
		}
		return mutableText;
	}
}
