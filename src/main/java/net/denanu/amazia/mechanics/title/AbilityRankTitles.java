package net.denanu.amazia.mechanics.title;

public enum AbilityRankTitles {
	UNTRAINED		(-1, -1, ""),
	NOVICE			(0,  0x404040, "novice"),

	BEGINNER		(10, 0xFF5555, "beginner"),
	APRENTICE		(20, 0xFFFF55, "aprentice"),
	JOURNEYMAN		(30, 0x55FF55, "journeyman"),

	STUDENT			(40, 0x5555FF, "student"),
	GRAD_STUDENT	(50, 170,	   "grad_student"),
	EXPERT			(60, 0xFF55FF, "expert"),
	LEADING_EXPERT	(70, 0xAA00AA, "leading_expert"),
	MASTER			(80, 0xFFAA00, "master"),
	GRAND_MASTER	(90, 0xFFAA00, "grand_master");

	private final int levelRequired, color;
	private final String key;

	AbilityRankTitles(final int lvl, final int color, final String key) {
		this.levelRequired = lvl;
		this.color = color;
		this.key = key;
	}

	public boolean qualifies(final int lvl) {
		return lvl >= this.levelRequired;
	}

	public int getColor() {
		return this.color;
	}

	public boolean shouldRender() {
		return this.color != -1;
	}

	public String getTranslationKey() {
		return "amazia.abilityrank." + this.key + ".title";
	}

	public static AbilityRankTitles of(final int lvl) {
		if (lvl > 0) {
			final AbilityRankTitles[] titles = AbilityRankTitles.values();
			for (int idx = titles.length-1; idx >= 0; idx--) {
				final AbilityRankTitles title = titles[idx];
				if (title.qualifies(lvl)) {
					return title;
				}
			}
		}
		return AbilityRankTitles.UNTRAINED;
	}

	public static AbilityRankTitles of(final String key) {
		for (final AbilityRankTitles title : AbilityRankTitles.values()) {
			if (title.key.equals(key)) {
				return title;
			}
		}
		return null;
	}

	public AbilityRankTitles next() {
		return switch(this) {
		case GRAND_MASTER -> this;
		default -> AbilityRankTitles.values()[this.ordinal() + 1];
		};
	}

	public AbilityRankTitles nextIfQualifies(final int lvl) {
		final AbilityRankTitles rank = this.next();
		return rank.qualifies(lvl) ? rank : this;
	}

	public String getKey() {
		return this.key;
	}
}
