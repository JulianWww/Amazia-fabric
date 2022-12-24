package net.denanu.amazia.sounds;

import net.denanu.amazia.Amazia;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaBardSounds {
	public static Identifier GATES_OF_GLORY_ID		= Identifier.of(Amazia.MOD_ID, "gates_of_glory");


	public static SoundEvent GATES_OF_GLORY_EVENT	= AmaziaBardSounds.register(AmaziaBardSounds.GATES_OF_GLORY_ID);


	private static SoundEvent register(final Identifier id) {
		return AmaziaBardSounds.register(new SoundEvent(id), id);
	}

	private static SoundEvent register(final SoundEvent event, final Identifier id) {
		Registry.register(Registry.SOUND_EVENT, id, event);
		return event;
	}

	public static void setup() {}
}
