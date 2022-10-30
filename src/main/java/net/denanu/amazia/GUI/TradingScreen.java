/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.GUI;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.c2s.AmaziaMerchantTradeSelectC2SPacket;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
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
    /*private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 256;
    private static final int field_32356 = 99;
    private static final int XP_BAR_X_OFFSET = 136;
    private static final int TRADE_LIST_AREA_Y_OFFSET = 16;
    private static final int FIRST_BUY_ITEM_X_OFFSET = 5;
    private static final int SECOND_BUY_ITEM_X_OFFSET = 35;
    private static final int SOLD_ITEM_X_OFFSET = 68;
    private static final int field_32362 = 6;
    private static final int MAX_TRADE_OFFERS = 7;
    private static final int field_32364 = 5;
    private static final int TRADE_OFFER_BUTTON_HEIGHT = 20;
    private static final int TRADE_OFFER_BUTTON_WIDTH = 89;
    private static final int SCROLLBAR_HEIGHT = 27;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_AREA_HEIGHT = 139;
    private static final int SCROLLBAR_OFFSET_Y = 18;
    private static final int SCROLLBAR_OFFSET_X = 94;*/
    private static final Text TRADES_TEXT = Text.translatable("merchant.trades");
    private static final Text SEPARATOR_TEXT = Text.literal(" - ");
    private static final Text DEPRECATED_TEXT = Text.translatable("merchant.deprecated");
    private int selectedIndex;
    private final WidgetButtonPage[] offers = new WidgetButtonPage[7];
    int indexStartOffset;
    private boolean scrolling;

    public TradingScreen(TradingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 276;
        this.playerInventoryTitleX = 107;
    }

    private void syncRecipeIndex() {
        ((TradingScreenHandler)this.handler).setRecipeIndex(this.selectedIndex);
        ((TradingScreenHandler)this.handler).switchTo(this.selectedIndex);
        ClientPlayNetworking.send(AmaziaNetworking.MERCHANT_TRADE_SELECT, AmaziaMerchantTradeSelectC2SPacket.toBuf(this.selectedIndex));
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
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
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int i = ((TradingScreenHandler)this.handler).getLevelProgress();
        if (i > 0 && i <= 5 && ((TradingScreenHandler)this.handler).isLeveled()) {
            MutableText text = this.title.copy().append(SEPARATOR_TEXT).append(Text.translatable("merchant.level." + i));
            int j = this.textRenderer.getWidth(text);
            int k = 49 + this.backgroundWidth / 2 - j / 2;
            this.textRenderer.draw(matrices, text, (float)k, 6.0f, 0x404040);
        } else {
            this.textRenderer.draw(matrices, this.title, (float)(49 + this.backgroundWidth / 2 - this.textRenderer.getWidth(this.title) / 2), 6.0f, 0x404040);
        }
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 0x404040);
        int l = this.textRenderer.getWidth(TRADES_TEXT);
        this.textRenderer.draw(matrices, TRADES_TEXT, (float)(5 - l / 2 + 48), 6.0f, 0x404040);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        TradingScreen.drawTexture(matrices, i, j, this.getZOffset(), 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 512, 256);
        AmaziaTradeOfferList tradeOfferList = ((TradingScreenHandler)this.handler).getRecipes();
        if (!tradeOfferList.isEmpty()) {
            int k = this.selectedIndex;
            if (k < 0 || k >= tradeOfferList.size()) {
                return;
            }
            AmaziaTradeOffer tradeOffer = (AmaziaTradeOffer)tradeOfferList.get(k);
            if (tradeOffer.isDisabled()) {
                RenderSystem.setShaderTexture(0, TEXTURE);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                TradingScreen.drawTexture(matrices, this.x + 83 + 99, this.y + 35, this.getZOffset(), 311.0f, 0.0f, 28, 21, 512, 256);
            }
        }
    }

    private void drawLevelInfo(MatrixStack matrices, int x, int y, AmaziaTradeOffer tradeOffer) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = ((TradingScreenHandler)this.handler).getLevelProgress();
        int j = ((TradingScreenHandler)this.handler).getExperience();
        if (i >= 5) {
            return;
        }
        TradingScreen.drawTexture(matrices, x + 136, y + 16, this.getZOffset(), 0.0f, 186.0f, 102, 5, 512, 256);
        int k = VillagerData.getLowerLevelExperience(i);
        if (j < k || !VillagerData.canLevelUp(i)) {
            return;
        }
        int l = 100;
        float f = 100.0f / (float)(VillagerData.getUpperLevelExperience(i) - k);
        int m = Math.min(MathHelper.floor(f * (float)(j - k)), 100);
        TradingScreen.drawTexture(matrices, x + 136, y + 16, this.getZOffset(), 0.0f, 191.0f, m + 1, 5, 512, 256);
        int n = ((TradingScreenHandler)this.handler).getMerchantRewardedExperience();
        if (n > 0) {
            int o = Math.min(MathHelper.floor((float)n * f), 100 - m);
            TradingScreen.drawTexture(matrices, x + 136 + m + 1, y + 16 + 1, this.getZOffset(), 2.0f, 182.0f, o, 3, 512, 256);
        }
    }

    private void renderScrollbar(MatrixStack matrices, int x, int y, AmaziaTradeOfferList tradeOfferList) {
        int i = tradeOfferList.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int m = Math.min(113, this.indexStartOffset * k);
            if (this.indexStartOffset == i - 1) {
                m = 113;
            }
            TradingScreen.drawTexture(matrices, x + 94, y + 18 + m, this.getZOffset(), 0.0f, 199.0f, 6, 27, 512, 256);
        } else {
            TradingScreen.drawTexture(matrices, x + 94, y + 18, this.getZOffset(), 6.0f, 199.0f, 6, 27, 512, 256);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        AmaziaTradeOfferList tradeOfferList = ((TradingScreenHandler)this.handler).getRecipes();
        if (!tradeOfferList.isEmpty()) {
        	AmaziaTradeOffer tradeOffer2;
            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.renderScrollbar(matrices, i, j, tradeOfferList);
            int m = 0;
            for (AmaziaTradeOffer iter : tradeOfferList) {
                if (this.canScroll(tradeOfferList.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset)) {
                    ++m;
                    continue;
                }
                ItemStack itemStack = iter.getFistBuyItem();
                ItemStack itemStack3 = iter.getSecondBuyItem();
                ItemStack itemStack4 = iter.getSellItem();
                this.itemRenderer.zOffset = 100.0f;
                int n = k + 2;
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
            int o = this.selectedIndex;
            tradeOffer2 = (AmaziaTradeOffer)tradeOfferList.get(o);
            if (((TradingScreenHandler)this.handler).isLeveled()) {
                this.drawLevelInfo(matrices, i, j, tradeOffer2);
            }
            if (tradeOffer2.isDisabled() && this.isPointWithinBounds(186, 35, 22, 21, mouseX, mouseY) && ((TradingScreenHandler)this.handler).canRefreshTrades()) {
                this.renderTooltip(matrices, DEPRECATED_TEXT, mouseX, mouseY);
            }
            for (WidgetButtonPage widgetButtonPage : this.offers) {
                if (widgetButtonPage.isHovered()) {
                    widgetButtonPage.renderTooltip(matrices, mouseX, mouseY);
                }
                widgetButtonPage.visible = widgetButtonPage.index < ((TradingScreenHandler)this.handler).getRecipes().size();
            }
            RenderSystem.enableDepthTest();
        }
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private void renderArrow(MatrixStack matrices, AmaziaTradeOffer tradeOffer, int x, int y) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (tradeOffer.isDisabled()) {
            TradingScreen.drawTexture(matrices, x + 5 + 35 + 20, y + 3, this.getZOffset(), 25.0f, 171.0f, 10, 9, 512, 256);
        } else {
            TradingScreen.drawTexture(matrices, x + 5 + 35 + 20, y + 3, this.getZOffset(), 15.0f, 171.0f, 10, 9, 512, 256);
        }
    }

    private void renderFirstBuyItem(MatrixStack matrices, ItemStack originalFirstBuyItem, int x, int y) {
        this.itemRenderer.renderInGui(originalFirstBuyItem, x, y);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, originalFirstBuyItem, x, y);
    }

    private boolean canScroll(int listSize) {
        return listSize > 7;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = ((TradingScreenHandler)this.handler).getRecipes().size();
        if (this.canScroll(i)) {
            int j = i - 7;
            this.indexStartOffset = MathHelper.clamp((int)((double)this.indexStartOffset - amount), 0, j);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int i = ((TradingScreenHandler)this.handler).getRecipes().size();
        if (this.scrolling) {
            int j = this.y + 18;
            int k = j + 139;
            int l = i - 7;
            float f = ((float)mouseY - (float)j - 13.5f) / ((float)(k - j) - 27.0f);
            f = f * (float)l + 0.5f;
            this.indexStartOffset = MathHelper.clamp((int)f, 0, l);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        if (this.canScroll(((TradingScreenHandler)this.handler).getRecipes().size()) && mouseX > (double)(i + 94) && mouseX < (double)(i + 94 + 6) && mouseY > (double)(j + 18) && mouseY <= (double)(j + 18 + 139 + 1)) {
            this.scrolling = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Environment(value=EnvType.CLIENT)
    class WidgetButtonPage
    extends ButtonWidget {
        final int index;

        public WidgetButtonPage(int x, int y, int index, ButtonWidget.PressAction onPress) {
            super(x, y, 89, 20, ScreenTexts.EMPTY, onPress);
            this.index = index;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.hovered && ((TradingScreenHandler)TradingScreen.this.handler).getRecipes().size() > this.index + TradingScreen.this.indexStartOffset) {
                if (mouseX < this.x + 20) {
                    ItemStack itemStack = ((AmaziaTradeOffer)((TradingScreenHandler)TradingScreen.this.handler).getRecipes().get(this.index + TradingScreen.this.indexStartOffset)).getFistBuyItem();
                    TradingScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
                } else if (mouseX < this.x + 50 && mouseX > this.x + 30) {
                    ItemStack itemStack = ((AmaziaTradeOffer)((TradingScreenHandler)TradingScreen.this.handler).getRecipes().get(this.index + TradingScreen.this.indexStartOffset)).getSecondBuyItem();
                    if (!itemStack.isEmpty()) {
                        TradingScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
                    }
                } else if (mouseX > this.x + 65) {
                    ItemStack itemStack = ((AmaziaTradeOffer)((TradingScreenHandler)TradingScreen.this.handler).getRecipes().get(this.index + TradingScreen.this.indexStartOffset)).getSellItem();
                    TradingScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
                }
            }
        }
    }
}

