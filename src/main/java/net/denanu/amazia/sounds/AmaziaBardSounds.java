package net.denanu.amazia.sounds;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaBardSounds {
	public static Identifier A_BARDS_TALE										= Identifier.of(Amazia.MOD_ID, "a_bards_tale");											// 200
	public static Identifier AROUND_THE_FIRE									= Identifier.of(Amazia.MOD_ID, "around_the_fire");										// 300
	public static Identifier A_TAVERN_ON_THE_RIVERBANK							= Identifier.of(Amazia.MOD_ID, "a_tavern_on_the_riverbank");							// 142
	public static Identifier BACK_ON_THE_PATH									= Identifier.of(Amazia.MOD_ID, "back_on_the_path");										// 240
	public static Identifier DACW_NGHARIAD										= Identifier.of(Amazia.MOD_ID, "dacw_nghariad");										// 240
	public static Identifier DRAGONS_DOGMA_CHARACTER_CREATION					= Identifier.of(Amazia.MOD_ID, "dragons_dogma_character_creation");						// 222
	public static Identifier DRINK_UP_THERES_MORE								= Identifier.of(Amazia.MOD_ID, "drink_up_theres_more");									// 137
	public static Identifier ENCHANTERS											= Identifier.of(Amazia.MOD_ID, "enchanters");											// 320
	public static Identifier FALL_OF_THE_MAGISTER								= Identifier.of(Amazia.MOD_ID, "fall_of_the_magister");									// 255
	public static Identifier FOR_THE_DANCING_AND_THE_DREAMING_INSTRUMENTAL		= Identifier.of(Amazia.MOD_ID, "for_the_dancing_and_the_dreaming_instrumental");
	public static Identifier GATES_OF_GLORY										= Identifier.of(Amazia.MOD_ID, "gates_of_glory");
	public static Identifier HANDS_OF_GOLD										= Identifier.of(Amazia.MOD_ID, "hands_of_gold");
	public static Identifier I_AM_THE_ONE										= Identifier.of(Amazia.MOD_ID, "i_am_the_one");											// 200
	public static Identifier IN_UTHENERA										= Identifier.of(Amazia.MOD_ID, "in_uthenera");
	public static Identifier KYDONIA											= Identifier.of(Amazia.MOD_ID, "kydonia");
	public static Identifier LOVE_SONG											= Identifier.of(Amazia.MOD_ID, "love_song");
	public static Identifier MEMORIES_OF_MOTHER									= Identifier.of(Amazia.MOD_ID, "memories_of_mother");
	public static Identifier MERCHANTS_OF_NOVIGRAD								= Identifier.of(Amazia.MOD_ID, "merchants_of_novigrad");
	public static Identifier NIGHTINGALES_EYES									= Identifier.of(Amazia.MOD_ID, "nightingales_eyes");
	public static Identifier OUT_OF_THE_COLD									= Identifier.of(Amazia.MOD_ID, "out_of_the_cold");
	public static Identifier ROGUE_HEART										= Identifier.of(Amazia.MOD_ID, "rogue_heart");
	public static Identifier SALTARELLO											= Identifier.of(Amazia.MOD_ID, "saltarello");
	public static Identifier SERA_WAS_NEVER										= Identifier.of(Amazia.MOD_ID, "sera_was_never");
	public static Identifier SHADY_REST											= Identifier.of(Amazia.MOD_ID, "shady_rest");
	public static Identifier SHELAYA											= Identifier.of(Amazia.MOD_ID, "shelaya");
	public static Identifier STONE_FIRE											= Identifier.of(Amazia.MOD_ID, "stone_fire");
	public static Identifier TAVERN_THEME_3										= Identifier.of(Amazia.MOD_ID, "tavern_theme_3");
	public static Identifier THE_BANNERED_MARE									= Identifier.of(Amazia.MOD_ID, "the_bannered_mare");
	public static Identifier THE_BARDS_SONG										= Identifier.of(Amazia.MOD_ID, "the_bards_song");
	public static Identifier THE_BEAR_AND_THE_MAIDEN_FAIR						= Identifier.of(Amazia.MOD_ID, "the_bear_and_the_maiden_fair");
	public static Identifier THE_DORNISHMANS_WIFE								= Identifier.of(Amazia.MOD_ID, "the_dornishmans_wife");
	public static Identifier THE_NIGHTINGALE									= Identifier.of(Amazia.MOD_ID, "the_nightingale");
	public static Identifier THE_WOLVEN_STORM									= Identifier.of(Amazia.MOD_ID, "the_wolven_storm");
	public static Identifier THUNDERBREW										= Identifier.of(Amazia.MOD_ID, "thunderbrew");
	public static Identifier TOSS_A_COIN_TO_YOUR_WITCHER						= Identifier.of(Amazia.MOD_ID, "toss_a_coin_to_your_witcher");
	public static Identifier UNRELEASED_GWENT_TRACK								= Identifier.of(Amazia.MOD_ID, "unreleased_gwent_track");


	public static SoundEvent A_BARDS_TALE_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.A_BARDS_TALE);
	public static SoundEvent AROUND_THE_FIRE_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.AROUND_THE_FIRE);
	public static SoundEvent A_TAVERN_ON_THE_RIVERBANK_							= AmaziaBardSounds.register(AmaziaBardSounds.A_TAVERN_ON_THE_RIVERBANK);
	public static SoundEvent BACK_ON_THE_PATH_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.BACK_ON_THE_PATH);
	public static SoundEvent DACW_NGHARIAD_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.DACW_NGHARIAD);
	public static SoundEvent DRAGONS_DOGMA_CHARACTER_CREATION_EVENT				= AmaziaBardSounds.register(AmaziaBardSounds.DRAGONS_DOGMA_CHARACTER_CREATION);
	public static SoundEvent DRINK_UP_THERES_MORE_EVENT							= AmaziaBardSounds.register(AmaziaBardSounds.DRINK_UP_THERES_MORE);
	public static SoundEvent ENCHANTERS_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.ENCHANTERS);
	public static SoundEvent FALL_OF_THE_MAGISTER_EVENT							= AmaziaBardSounds.register(AmaziaBardSounds.FALL_OF_THE_MAGISTER);
	public static SoundEvent FOR_THE_DANCING_AND_THE_DREAMING_INSTRUMENTAL_EVENT= AmaziaBardSounds.register(AmaziaBardSounds.FOR_THE_DANCING_AND_THE_DREAMING_INSTRUMENTAL);
	public static SoundEvent GATES_OF_GLORY_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.GATES_OF_GLORY);
	public static SoundEvent HANDS_OF_GOLD_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.HANDS_OF_GOLD);
	public static SoundEvent I_AM_THE_ONE_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.I_AM_THE_ONE);
	public static SoundEvent IN_UTHENERA_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.IN_UTHENERA);
	public static SoundEvent KYDONIA_EVENT										= AmaziaBardSounds.register(AmaziaBardSounds.KYDONIA);
	public static SoundEvent LOVE_SONG_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.LOVE_SONG);
	public static SoundEvent MEMORIES_OF_MOTHER_EVENT							= AmaziaBardSounds.register(AmaziaBardSounds.MEMORIES_OF_MOTHER);
	public static SoundEvent MERCHANTS_OF_NOVIGRAD_EVENT						= AmaziaBardSounds.register(AmaziaBardSounds.MERCHANTS_OF_NOVIGRAD);
	public static SoundEvent NIGHTINGALES_EYES_EVENT							= AmaziaBardSounds.register(AmaziaBardSounds.NIGHTINGALES_EYES);
	public static SoundEvent OUT_OF_THE_COLD_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.OUT_OF_THE_COLD);
	public static SoundEvent ROGUE_HEART_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.ROGUE_HEART);
	public static SoundEvent SALTARELLO_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.SALTARELLO);
	public static SoundEvent SERA_WAS_NEVER_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.SERA_WAS_NEVER);
	public static SoundEvent SHADY_REST_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.SHADY_REST);
	public static SoundEvent SHELAYA_EVENT										= AmaziaBardSounds.register(AmaziaBardSounds.SHELAYA);
	public static SoundEvent STONE_FIRE_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.STONE_FIRE);
	public static SoundEvent TAVERN_THEME_3_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.TAVERN_THEME_3);
	public static SoundEvent THE_BANNERED_MARE_EVENT							= AmaziaBardSounds.register(AmaziaBardSounds.THE_BANNERED_MARE);
	public static SoundEvent THE_BARDS_SONG_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.THE_BARDS_SONG);
	public static SoundEvent THE_BEAR_AND_THE_MAIDEN_FAIR_EVENT					= AmaziaBardSounds.register(AmaziaBardSounds.THE_BEAR_AND_THE_MAIDEN_FAIR);
	public static SoundEvent THE_DORNISHMANS_WIFE_EVENT							= AmaziaBardSounds.register(AmaziaBardSounds.THE_DORNISHMANS_WIFE);
	public static SoundEvent THE_NIGHTINGALE_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.THE_NIGHTINGALE);
	public static SoundEvent THE_WOLVEN_STORM_EVENT								= AmaziaBardSounds.register(AmaziaBardSounds.THE_WOLVEN_STORM);
	public static SoundEvent THUNDERBREW_EVENT									= AmaziaBardSounds.register(AmaziaBardSounds.THUNDERBREW);
	public static SoundEvent TOSS_A_COIN_TO_YOUR_WITCHER_EVENT					= AmaziaBardSounds.register(AmaziaBardSounds.TOSS_A_COIN_TO_YOUR_WITCHER);
	public static SoundEvent UNRELEASED_GWENT_TRACK_EVENT						= AmaziaBardSounds.register(AmaziaBardSounds.UNRELEASED_GWENT_TRACK);


	public static WeightedRandomCollection<SoundEvent> BARD_DAYTIME_MUSIC = new WeightedRandomCollection<SoundEvent>()
			.add(1f, AmaziaBardSounds.A_BARDS_TALE_EVENT)
			.add(1f, AmaziaBardSounds.AROUND_THE_FIRE_EVENT)
			.add(1f, AmaziaBardSounds.A_TAVERN_ON_THE_RIVERBANK_)
			.add(1f, AmaziaBardSounds.BACK_ON_THE_PATH_EVENT)
			.add(1f, AmaziaBardSounds.DACW_NGHARIAD_EVENT)
			.add(1f, AmaziaBardSounds.DRAGONS_DOGMA_CHARACTER_CREATION_EVENT)
			.add(1f, AmaziaBardSounds.DRINK_UP_THERES_MORE_EVENT)
			.add(1f, AmaziaBardSounds.ENCHANTERS_EVENT)
			.add(1f, AmaziaBardSounds.FALL_OF_THE_MAGISTER_EVENT)
			.add(1f, AmaziaBardSounds.FOR_THE_DANCING_AND_THE_DREAMING_INSTRUMENTAL_EVENT)
			.add(1f, AmaziaBardSounds.GATES_OF_GLORY_EVENT)
			.add(1f, AmaziaBardSounds.HANDS_OF_GOLD_EVENT)
			.add(1f, AmaziaBardSounds.I_AM_THE_ONE_EVENT)
			.add(1f, AmaziaBardSounds.IN_UTHENERA_EVENT)
			.add(1f, AmaziaBardSounds.KYDONIA_EVENT)
			.add(1f, AmaziaBardSounds.LOVE_SONG_EVENT)
			.add(1f, AmaziaBardSounds.MEMORIES_OF_MOTHER_EVENT)
			.add(1f, AmaziaBardSounds.MERCHANTS_OF_NOVIGRAD_EVENT)
			.add(1f, AmaziaBardSounds.NIGHTINGALES_EYES_EVENT)
			.add(1f, AmaziaBardSounds.OUT_OF_THE_COLD_EVENT)
			.add(1f, AmaziaBardSounds.ROGUE_HEART_EVENT)
			.add(1f, AmaziaBardSounds.SALTARELLO_EVENT)
			.add(1f, AmaziaBardSounds.SERA_WAS_NEVER_EVENT)
			.add(1f, AmaziaBardSounds.SHADY_REST_EVENT)
			.add(1f, AmaziaBardSounds.SHELAYA_EVENT)
			.add(1f, AmaziaBardSounds.STONE_FIRE_EVENT)
			.add(1f, AmaziaBardSounds.TAVERN_THEME_3_EVENT)
			.add(1f, AmaziaBardSounds.THE_BANNERED_MARE_EVENT)
			.add(1f, AmaziaBardSounds.THE_BARDS_SONG_EVENT)
			.add(1f, AmaziaBardSounds.THE_BEAR_AND_THE_MAIDEN_FAIR_EVENT)
			.add(1f, AmaziaBardSounds.THE_DORNISHMANS_WIFE_EVENT)
			.add(1f, AmaziaBardSounds.THE_BEAR_AND_THE_MAIDEN_FAIR_EVENT)
			.add(1f, AmaziaBardSounds.THE_DORNISHMANS_WIFE_EVENT)
			.add(1f, AmaziaBardSounds.THE_NIGHTINGALE_EVENT)
			.add(1f, AmaziaBardSounds.THE_WOLVEN_STORM_EVENT)
			.add(1f, AmaziaBardSounds.THUNDERBREW_EVENT)
			.add(1f, AmaziaBardSounds.TOSS_A_COIN_TO_YOUR_WITCHER_EVENT)
			.add(1f, AmaziaBardSounds.UNRELEASED_GWENT_TRACK_EVENT);


	private static SoundEvent register(final Identifier id) {
		return AmaziaBardSounds.register(new SoundEvent(id), id);
	}

	private static SoundEvent register(final SoundEvent event, final Identifier id) {
		Registry.register(Registry.SOUND_EVENT, id, event);
		return event;
	}

	public static void setup() {}
}
