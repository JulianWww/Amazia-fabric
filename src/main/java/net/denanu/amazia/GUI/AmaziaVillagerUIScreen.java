package net.denanu.amazia.GUI;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.amazia.Amazia;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
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
				Text.literal("Health: ").append(Integer.toString(1)),
				this.playerInventoryTitleX,
				this.playerInventoryTitleY,
				0x404040);
	}

	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		super.init();
		// Center the title
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}
}
