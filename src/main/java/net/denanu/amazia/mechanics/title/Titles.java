package net.denanu.amazia.mechanics.title;

import java.util.function.Predicate;

import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;

public enum Titles {
	DOCTOR		("Dr. ",	TitlePosition.FRONT,  	entity -> entity.getEducation() >= 80),
	PROFESSOR	("Prof. ", 	TitlePosition.FRONT,	entity -> entity.getEducation() >= 80 && entity.getProfession() == AmaziaProfessions.TEACHER),

	MEDICAL		(", MD",	TitlePosition.BACK,		entity -> entity.getProfession() == AmaziaProfessions.CLERIC);

	private final String title;
	private final TitlePosition position;
	private Predicate<IAmaziaDataProviderEntity> qualifies;

	Titles(final String title, final TitlePosition position, final Predicate<IAmaziaDataProviderEntity> pred) {
		this.title = title;
		this.qualifies = pred;
		this.position = position;
	}

	public static String removeTitles(String name) {
		for (int idx = Titles.values().length - 1; idx >= 0; idx--) {
			final Titles title = Titles.values()[idx];
			switch(title.position) {
			case FRONT -> {
				if (name.startsWith(title.title)) {
					name = name.substring(title.title.length());
				}
			}
			case BACK -> {
				if (name.endsWith(title.title)) {
					name = name.substring(0, name.length() - title.title.length());
				}
			}
			}
		}
		return name;
	}

	private static String setTitles(String name, final IAmaziaDataProviderEntity entity) {
		for (final Titles title : Titles.values()) {
			if (title.qualifies.test(entity)) {
				switch (title.position) {
				case FRONT -> {
					name = title.title + name;
				}
				case BACK -> {
					name = name + title.title;
				}
				}
			}
		}
		return name;
	}

	public static String updateTitles(final String name, final IAmaziaDataProviderEntity entity) {
		return Titles.setTitles(Titles.removeTitles(name), entity);
	}

	enum TitlePosition {
		FRONT,
		BACK;
	}
}
