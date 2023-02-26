/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.GUI;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.c2s.AmaziaMerchantTradeSelectC2SPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillagerData;

@Environment(value=EnvType.CLIENT)
public class TradingScreen
extends HandledScreen<TradingScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/villager2.png");
	private static final Text TRADES_TEXT = Text.translatable("merchant.trades");
	private static final Text SEPARATOR_TEXT = Text.literal(" - ");
	private static final Text DEPRECATED_TEXT = Text.translatable("merchant.deprecated");
	private int selectedIndex;
	private final WidgetButtonPage[] offers = new WidgetButtonPage[7];
	int indexStartOffset;
	private boolean scrolling;

	public TradingScreen(final TradingScreenHandler handler, final PlayerInventory inventory, final Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 276;
		this.playerInventoryTitleX = 107;
	}

	private void syncRecipeIndex() {
		this.handler.setRecipeIndex(this.selectedIndex);
		this.handler.switchTo(this.selectedIndex);
		ClientPlayNetworking.send(AmaziaNetworking.C2S.MERCHANT_TRADE_SELECT, AmaziaMerchantTradeSelectC2SPacket.toBuf(this.selectedIndex));
	}

	@Override
	protected void init() {
		super.init();
		final int i = (this.width - this.backgroundWidth) / 2;
		final int j = (this.height - this.backgroundHeight) / 2;
		int k = j + 16 + 2;
		for (int l = 0; l < 7; ++l) {
			this.offers[l] = this.addDrawableChild(new WidgetButtonPage(i + 5, k, l, button -> {
				if (button instanceof WidgetButtonPage) {
					this.selectedIndex = ((WidgetButtonPage)button).getIndex() + this.indexStartOffset;
					this.syncRecipeIndex();
				}
			}));
			k += 20;
		}
	}

	@Override
	protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY) {
		final int i = this.handler.getLevelProgress();
		if (i > 0 && i <= 5 && this.handler.isLeveled()) {
			final MutableText text = this.title.copy().append(TradingScreen.SEPARATOR_TEXT).append(Text.translatable("merchant.level." + i));
			final int j = this.textRenderer.getWidth(text);
			final int k = 49 + this.backgroundWidth / 2 - j / 2;
			this.textRenderer.draw(matrices, text, k, 6.0f, 0x404040);
		} else {
			this.textRenderer.draw(matrices, this.title, 49 + this.backgroundWidth / 2 - this.textRenderer.getWidth(this.title) / 2, 6.0f, 0x404040);
		}
		this.textRenderer.draw(matrices, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0x404040);
		final int l = this.textRenderer.getWidth(TradingScreen.TRADES_TEXT);
		this.textRenderer.draw(matrices, TradingScreen.TRADES_TEXT, 5 - l / 2 + 48, 6.0f, 0x404040);
	}

	@Override
	protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TradingScreen.TEXTURE);
		final int i = (this.width - this.backgroundWidth) / 2;
		final int j = (this.height - this.backgroundHeight) / 2;
		DrawableHelper.drawTexture(matrices, i, j, this.getZOffset(), 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 512, 256);
		final AmaziaTradeOfferList tradeOfferList = this.handler.getRecipes();
		if (!tradeOfferList.isEmpty()) {
			final int k = this.selectedIndex;
			if (k < 0 || k >= tradeOfferList.size()) {
				return;
			}
			final AmaziaTradeOffer tradeOffer = tradeOfferList.get(k);
			if (tradeOffer.isDisabled()) {
				RenderSystem.setShaderTexture(0, TradingScreen.TEXTURE);
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
				DrawableHelper.drawTexture(matrices, this.x + 83 + 99, this.y + 35, this.getZOffset(), 311.0f, 0.0f, 28, 21, 512, 256);
			}
		}
	}

	private void drawLevelInfo(final MatrixStack matrices, final int x, final int y, final AmaziaTradeOffer tradeOffer) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TradingScreen.TEXTURE);
		final int i = this.handler.getLevelProgress();
		final int j = this.handler.getExperience();
		if (i >= 5) {
			return;
		}
		DrawableHelper.drawTexture(matrices, x + 136, y + 16, this.getZOffset(), 0.0f, 186.0f, 102, 5, 512, 256);
		final int k = VillagerData.getLowerLevelExperience(i);
		if (j < k || !VillagerData.canLevelUp(i)) {
			return;
		}
		final float f = 100.0f / (VillagerData.getUpperLevelExperience(i) - k);
		final int m = Math.min(MathHelper.floor(f * (j - k)), 100);
		DrawableHelper.drawTexture(matrices, x + 136, y + 16, this.getZOffset(), 0.0f, 191.0f, m + 1, 5, 512, 256);
		final int n = this.handler.getMerchantRewardedExperience();
		if (n > 0) {
			final int o = Math.min(MathHelper.floor(n * f), 100 - m);
			DrawableHelper.drawTexture(matrices, x + 136 + m + 1, y + 16 + 1, this.getZOffset(), 2.0f, 182.0f, o, 3, 512, 256);
		}
	}

	private void renderScrollbar(final MatrixStack matrices, final int x, final int y, final AmaziaTradeOfferList tradeOfferList) {
		final int i = tradeOfferList.size() + 1 - 7;
		if (i > 1) {
			final int j = 139 - (27 + (i - 1) * 139 / i);
			final int k = 1 + j / i + 139 / i;
			int m = Math.min(113, this.indexStartOffset * k);
			if (this.indexStartOffset == i - 1) {
				m = 113;
			}
			DrawableHelper.drawTexture(matrices, x + 94, y + 18 + m, this.getZOffset(), 0.0f, 199.0f, 6, 27, 512, 256);
		} else {
			DrawableHelper.drawTexture(matrices, x + 94, y + 18, this.getZOffset(), 6.0f, 199.0f, 6, 27, 512, 256);
		}
	}

	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		final AmaziaTradeOfferList tradeOfferList = this.handler.getRecipes();
		if (!tradeOfferList.isEmpty()) {
			AmaziaTradeOffer tradeOffer2;
			final int i = (this.width - this.backgroundWidth) / 2;
			final int j = (this.height - this.backgroundHeight) / 2;
			int k = j + 16 + 1;
			final int l = i + 5 + 5;
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, TradingScreen.TEXTURE);
			this.renderScrollbar(matrices, i, j, tradeOfferList);
			int m = 0;
			for (final AmaziaTradeOffer iter : tradeOfferList) {
				if (this.canScroll(tradeOfferList.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset)) {
					++m;
					continue;
				}
				final ItemStack itemStack = iter.getFistBuyItem();
				final ItemStack itemStack3 = iter.getSecondBuyItem();
				final ItemStack itemStack4 = iter.getSellItem();
				this.itemRenderer.zOffset = 100.0f;
				final int n = k + 2;
				this.renderFirstBuyItem(matrices, itemStack, l, n);
				if (!itemStack3.isEmpty()) {
					this.itemRenderer.renderInGui(itemStack3, i + 5 + 35, n);
					this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack3, i + 5 + 35, n);
				}
				this.renderArrow(matrices, iter, i, n);
				this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
				this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
				this.itemRenderer.zOffset = 0.0f;
				k += 20;
				++m;
			}
			final int o = this.selectedIndex;
			tradeOffer2 = tradeOfferList.get(o);
			if (this.handler.isLeveled()) {
				this.drawLevelInfo(matrices, i, j, tradeOffer2);
			}
			if (tradeOffer2.isDisabled() && this.isPointWithinBounds(186, 35, 22, 21, mouseX, mouseY) && this.handler.canRefreshTrades()) {
				this.renderTooltip(matrices, TradingScreen.DEPRECATED_TEXT, mouseX, mouseY);
			}
			for (final WidgetButtonPage widgetButtonPage : this.offers) {
				if (widgetButtonPage.isHovered()) {
					widgetButtonPage.renderTooltip(matrices, mouseX, mouseY);
				}
				widgetButtonPage.visible = widgetButtonPage.index < this.handler.getRecipes().size();
			}
			RenderSystem.enableDepthTest();
		}
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	private void renderArrow(final MatrixStack matrices, final AmaziaTradeOffer tradeOffer, final int x, final int y) {
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TradingScreen.TEXTURE);
		if (tradeOffer.isDisabled()) {
			DrawableHelper.drawTexture(matrices, x + 5 + 35 + 20, y + 3, this.getZOffset(), 25.0f, 171.0f, 10, 9, 512, 256);
		} else {
			DrawableHelper.drawTexture(matrices, x + 5 + 35 + 20, y + 3, this.getZOffset(), 15.0f, 171.0f, 10, 9, 512, 256);
		}
	}

	private void renderFirstBuyItem(final MatrixStack matrices, final ItemStack originalFirstBuyItem, final int x, final int y) {
		this.itemRenderer.renderInGui(originalFirstBuyItem, x, y);
		this.itemRenderer.renderGuiItemOverlay(this.textRenderer, originalFirstBuyItem, x, y);
	}

	private boolean canScroll(final int listSize) {
		return listSize > 7;
	}

	@Override
	public boolean mouseScrolled(final double mouseX, final double mouseY, final double amount) {
		final int i = this.handler.getRecipes().size();
		if (this.canScroll(i)) {
			final int j = i - 7;
			this.indexStartOffset = MathHelper.clamp((int)(this.indexStartOffset - amount), 0, j);
		}
		return true;
	}

	@Override
	public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
		final int i = this.handler.getRecipes().size();
		if (this.scrolling) {
			final int j = this.y + 18;
			final int k = j + 139;
			final int l = i - 7;
			float f = ((float)mouseY - j - 13.5f) / (k - j - 27.0f);
			f = f * l + 0.5f;
			this.indexStartOffset = MathHelper.clamp((int)f, 0, l);
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
		this.scrolling = false;
		final int i = (this.width - this.backgroundWidth) / 2;
		final int j = (this.height - this.backgroundHeight) / 2;
		if (this.canScroll(this.handler.getRecipes().size()) && mouseX > i + 94 && mouseX < i + 94 + 6 && mouseY > j + 18 && mouseY <= j + 18 + 139 + 1) {
			this.scrolling = true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Environment(value=EnvType.CLIENT)
	class WidgetButtonPage
	extends ButtonWidget {
		final int index;

		public WidgetButtonPage(final int x, final int y, final int index, final ButtonWidget.PressAction onPress) {
			super(x, y, 89, 20, ScreenTexts.EMPTY, onPress);
			this.index = index;
			this.visible = false;
		}

		public int getIndex() {
			return this.index;
		}

		@Override
		public void renderTooltip(final MatrixStack matrices, final int mouseX, final int mouseY) {
			if (this.hovered && TradingScreen.this.handler.getRecipes().size() > this.index + TradingScreen.this.indexStartOffset) {
				if (mouseX < this.x + 20) {
					final ItemStack itemStack = TradingScreen.this.handler.getRecipes().get(this.index + TradingScreen.this.indexStartOffset).getFistBuyItem();
					TradingScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
				} else if (mouseX < this.x + 50 && mouseX > this.x + 30) {
					final ItemStack itemStack = TradingScreen.this.handler.getRecipes().get(this.index + TradingScreen.this.indexStartOffset).getSecondBuyItem();
					if (!itemStack.isEmpty()) {
						TradingScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
					}
				} else if (mouseX > this.x + 65) {
					final ItemStack itemStack = TradingScreen.this.handler.getRecipes().get(this.index + TradingScreen.this.indexStartOffset).getSellItem();
					TradingScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
				}
			}
		}
	}
}

