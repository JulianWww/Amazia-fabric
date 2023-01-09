package net.denanu.amazia.sounds;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.denanu.stoppablesound.sounds.LongSoundEvent;
import net.denanu.stoppablesound.utils.TimeBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaBardSounds {
	public static LongSoundEvent PIGSTEP_MUSIC_DISC	= AmaziaBardSounds.register(2, 28, "music_disc.pigstep");

	public static WeightedRandomCollection<LongSoundEvent> BARD_DAYTIME_MUSIC = new WeightedRandomCollection<LongSoundEvent>()
			.add(1f, AmaziaBardSounds.PIGSTEP_MUSIC_DISC);

	private static LongSoundEvent register(final int min, final int sec, final String id) {
		return AmaziaBardSounds.register(Identifier.of(Amazia.MOD_ID, id), TimeBuilder.of().min(min).sec(sec).build());
	}

	private static LongSoundEvent register(final Identifier id, final int lenght) {
		return AmaziaBardSounds.register(new LongSoundEvent(id, lenght), id);
	}

	private static LongSoundEvent register(final LongSoundEvent event, final Identifier id) {
		Registry.register(Registry.SOUND_EVENT, id, event);
		return event;
	}

	public static void setup() {}
}
