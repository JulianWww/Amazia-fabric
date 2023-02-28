package net.denanu.amazia.compat.malilib;

import java.util.Locale;

import com.github.javafaker.Faker;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.GuiBase;
import net.denanu.amazia.config.Config;
import net.denanu.amazia.config.GuiConfigs;
import net.denanu.amazia.mechanics.title.Titles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public enum NamingLanguageOptions implements IConfigOptionListEntry {
	BULGARIAN("bg", "български"),
	CANADIAN(Locale.CANADA, "ca", "Canadian"),
	DANISH("da-DK", "Dansk"),
	GERMAN("de", "Deutsch"),
	GERMAN_AUSTRIA("de-AT", "Österreichisches (Deitsch)"),
	GERMAN_SWITZERLAND("de-CH", "Schwiizerdutsch"),
	ENGLISH(Locale.ENGLISH, "en", "English"),
	ENGLISH_AUSTRALIA("en-AU", "English (Australia)"),
	ENGLISH_AUSTRALIA_OCKER("en-AU", "English (Australia) ocker"),
	ENGLISH_BORK("en-BORK", "Bork! Bork! Bork!"),
	ENGLISH_CANADA("en-CA", "English (Canada)"),
	ENGLISH_UK("en-GB", "English (United Kingdom)"),
	ENGLISH_INDIA("en-IND", "English (IND)"),
	ENGLISH_MALAY("en-MS", "English (Malay)"),
	ENGLISH_NEP("en-NEP", "English (NEP)"),
	ENGLISH_NIGERIA("en-NG", "English (Nigeria)"),
	ENGLISH_NEWZEALAND("en-NZ", "English (New Zealand)"),
	ENGLISH_PAKISTAN("en-PAK", "English (Pakistan)"),
	ENGLISH_SINGAPORE("en-SG", "English (Singapore)"),
	ENGLISH_UGANDA("en-UG", "English (Uganda)"),
	ENGLISH_US("en-US", "English (United States)"),
	ENGLISH_SA("en-ZA", "English (South Africa)"),
	SPANISH("es", "Spanish"),
	SPANISH_MEXICO("es-MX", "Spanish (Mexico)"),
	PERSIAN("fa", "Persian"),
	FINNISH("fi-FI", "Finnish"),
	FRENCH("fr", "French"),
	FRENCH_FI("he", "Hebrew"),
	ANGKU("hu", "Angku"),
	INDONESIAN("in-ID", "Indonesian"),
	ITALIAN("it", "Italian"),
	JAPANESE("ja", "Japanese"),
	KOREAN("ko", "korean"),
	NORWEGIAN("nb-NO", "Norwegian"),
	DUTCH("nl", "Dutch"),
	POLISH("pl", "Polish"),
	PORTUGUESE("pt", "Portuguese (Portugal)"),
	PORTUGUESE_BRAZIL("pt", "Portuguese (Brazil)"),
	RUSSIAN("ru", "Russian"),
	SLOVAK("sk", "Slovak"),
	SWEDISH("sv", "Swedish"),
	SWEDISH_SWEDEN("sv-SE", "Swedish (Sweden)"),
	TURKISH("tr", "Turkish"),
	UK("uk", "Uk (Whatever that is)"),
	VIETNAMESE("vi", "Vietnamese"),
	CHINESE("zh-CN", "Chinese (PRC)"),
	TAIWANESE("zh-TW", "Taiwanese Manderin");

	private final Locale locale;
	private final String name, display;

	public static Faker NAMEGENERATOR = new Faker();
	public static NamingLanguageOptions NAMINGLANGUAGE = NamingLanguageOptions.ENGLISH;

	NamingLanguageOptions(final String name, final String display) {
		this(new Locale(name), name, display);
	}

	NamingLanguageOptions(final Locale locale, final String name, final String display) {
		this.locale = locale;
		this.name = name;
		this.display = display;
	}

	public Locale getLocale() {
		return this.locale;
	}

	@Override
	public String getStringValue() {
		return this.name;
	}

	@Override
	public String getDisplayName() {
		return this.display;
	}

	public static NamingLanguageOptions of(final String value) {
		for (final NamingLanguageOptions entry : NamingLanguageOptions.values()) {
			if (entry.name == value) {
				return entry;
			}
		}
		return NamingLanguageOptions.ENGLISH;
	}

	@Override
	public IConfigOptionListEntry cycle(final boolean forward) {
		final MinecraftClient client = MinecraftClient.getInstance();
		GuiBase.openGui(new NameGeneratorLocalOptionsScreen(GuiConfigs.getInstance(), client.options, this));
		return this;
	}

	@Override
	public IConfigOptionListEntry fromString(final String value) {
		return NamingLanguageOptions.of(value);
	}

	public void updateNameGeneratorService() {
		NamingLanguageOptions.NAMEGENERATOR = new Faker(this.locale);
	}

	public static String generateName(final String lastName) {
		final String name = new StringBuilder()
				.append(NamingLanguageOptions.NAMEGENERATOR.name().firstName())
				.append(" ")
				.append(lastName == null ? NamingLanguageOptions.NAMEGENERATOR.name().lastName() : lastName)
				.toString();
		return Titles.removeTitles(name);
	}

	public static void update(final NamingLanguageOptions selected) {
		Config.Admin.NAMELANGUAGE.setOptionListValue(selected);
		selected.updateNameGeneratorService();
		NamingLanguageOptions.NAMINGLANGUAGE = selected;
		Loader.update();
	}

	public static class Loader extends PersistentState {
		private static Loader INSTANCE;

		public static void update() {
			if (Loader.INSTANCE != null) {
				Loader.INSTANCE.markDirty();
			}
		}

		@Override
		public NbtCompound writeNbt(final NbtCompound nbt) {
			nbt.putInt("NameGenLang", NamingLanguageOptions.NAMINGLANGUAGE.ordinal());
			return nbt;
		}

		public static Loader loadDefault() {
			NamingLanguageOptions.update(NamingLanguageOptions.ENGLISH);
			return Loader.INSTANCE = new Loader();
		}

		public static Loader fromNbt(final NbtCompound nbt) {
			NamingLanguageOptions.update(
					NamingLanguageOptions.values()[nbt.getInt("NameGenLang")]
					);
			return Loader.INSTANCE = new Loader();
		}
	}
}
