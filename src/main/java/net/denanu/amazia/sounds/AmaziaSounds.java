package net.denanu.amazia.sounds;

import net.denanu.amazia.Amazia;
import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaSounds {
	public static Identifier BARD_DAY_MUSIC_ID = Identifier.of(Amazia.MOD_ID, "bard_day_music");

	public static SoundEvent BARD_DAY_MUSIC = AmaziaSounds.register(AmaziaSounds.BARD_DAY_MUSIC_ID);


	private static SoundEvent register(final Identifier id) {
		final SoundEvent event = new SoundEvent(id);
		ServerSoundGroups.register(id);
		return Registry.register(Registry.SOUND_EVENT, id, event);
	}

	public static void setup() {}
}
