package net.denanu.amazia.compat.malilib;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.compat.malilib.NameGeneratorLocalOptionsScreen.LanguageSelectionListWidget.LanguageEntry;
import net.denanu.amazia.networking.c2s.UpdateNamingSystemC2SPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class NameGeneratorLocalOptionsScreen extends GameOptionsScreen {
	private static final Text LANGUAGE_WARNING_TEXT = Text.literal("(").append(Text.translatable(Amazia.MOD_ID + ".options.namegenlangWarning")).append(")").formatted(Formatting.GRAY);
	private LanguageSelectionListWidget languageSelectionList;
	NamingLanguageOptions current;

	public NameGeneratorLocalOptionsScreen(final Screen parent, final GameOptions options, final NamingLanguageOptions current) {
		super(parent, options, Text.translatable(Amazia.MOD_ID + ".options.namegenlang"));
		this.current = current;
	}

	@Override
	protected void init() {
		this.languageSelectionList = new LanguageSelectionListWidget(this.client);
		this.addSelectableChild(this.languageSelectionList);
		this.addDrawableChild(this.gameOptions.getForceUnicodeFont().createButton(this.gameOptions, this.width / 2 - 155, this.height - 38, 150));
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 155 + 160, this.height - 38, 150, 20, ScreenTexts.DONE, button -> {
			if (this.languageSelectionList.getSelectedOrNull() != null) {
				UpdateNamingSystemC2SPacket.send(this.languageSelectionList.getSelectedOrNull().getLanguageDefinition());
			}
			this.client.setScreen(this.parent);
		}));
		super.init();
	}

	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
		this.languageSelectionList.render(matrices, mouseX, mouseY, delta);
		DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
		DrawableHelper.drawCenteredText(matrices, this.textRenderer, NameGeneratorLocalOptionsScreen.LANGUAGE_WARNING_TEXT, this.width / 2, this.height - 56, 0x808080);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Environment(value=EnvType.CLIENT)
	class LanguageSelectionListWidget
	extends AlwaysSelectedEntryListWidget<LanguageEntry> {
		public LanguageSelectionListWidget(final MinecraftClient client) {
			super(client, NameGeneratorLocalOptionsScreen.this.width, NameGeneratorLocalOptionsScreen.this.height, 32, NameGeneratorLocalOptionsScreen.this.height - 65 + 4, 18);
			for (final NamingLanguageOptions languageDefinition : NamingLanguageOptions.values()) {
				final LanguageEntry languageEntry = new LanguageEntry(languageDefinition);
				this.addEntry(languageEntry);

				if (NameGeneratorLocalOptionsScreen.this.current == languageDefinition) {
					this.setSelected(languageEntry);
				}
			}
			if (this.getSelectedOrNull() != null) {
				this.centerScrollOn(this.getSelectedOrNull());
			}
		}

		@Override
		protected int getScrollbarPositionX() {
			return super.getScrollbarPositionX() + 20;
		}

		@Override
		public int getRowWidth() {
			return super.getRowWidth() + 50;
		}

		@Override
		protected void renderBackground(final MatrixStack matrices) {
			NameGeneratorLocalOptionsScreen.this.renderBackground(matrices);
		}

		@Override
		protected boolean isFocused() {
			return NameGeneratorLocalOptionsScreen.this.getFocused() == this;
		}

		@Environment(value=EnvType.CLIENT)
		public class LanguageEntry
		extends AlwaysSelectedEntryListWidget.Entry<LanguageEntry> {
			final NamingLanguageOptions languageDefinition;

			public LanguageEntry(final NamingLanguageOptions languageDefinition) {
				this.languageDefinition = languageDefinition;
			}

			@Override
			public void render(final MatrixStack matrices, final int index, final int y, final int x, final int entryWidth, final int entryHeight, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
				final String string = this.languageDefinition.getDisplayName();
				NameGeneratorLocalOptionsScreen.this.textRenderer.drawWithShadow(matrices, string, LanguageSelectionListWidget.this.width / 2 - NameGeneratorLocalOptionsScreen.this.textRenderer.getWidth(string) / 2, y + 1, 0xFFFFFF, true);
			}

			@Override
			public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
				if (button == 0) {
					this.onPressed();
					return true;
				}
				return false;
			}

			private void onPressed() {
				LanguageSelectionListWidget.this.setSelected(this);
			}

			@Override
			public Text getNarration() {
				return Text.translatable("narrator.select", this.languageDefinition);
			}

			public NamingLanguageOptions getLanguageDefinition() {
				return this.languageDefinition;
			}
		}
	}
}
