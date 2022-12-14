package net.denanu.amazia.mechanics.education;

public class AmaziaGainEducationMap {
	private final static float DEFAULT_EDU = 0.0001f;

	private void gainEducation(final IAmaziaEducatedEntity entity, final float amount) {
		entity.learn(amount);
	}

	public void gainLibraryEducation(final IAmaziaEducatedEntity entity) {
		this.gainEducation(entity, AmaziaGainEducationMap.DEFAULT_EDU);
	}
}
