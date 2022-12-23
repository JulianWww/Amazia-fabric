package net.denanu.amazia.mechanics.education;

public class AmaziaGainEducationMap {
	private final static float DEFAULT_EDU = 0.001f;

	private static void gainEducation(final IAmaziaEducatedEntity entity, final float amount) {
		entity.learn(amount);
	}

	public static void gainLibraryEducation(final IAmaziaEducatedEntity entity) {
		AmaziaGainEducationMap.gainEducation(entity, AmaziaGainEducationMap.DEFAULT_EDU);
	}
}
