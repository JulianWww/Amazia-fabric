package net.denanu.amazia.mechanics.title;

import java.util.function.Predicate;

import net.denanu.amazia.advancement.criterion.AmaziaCriterions;
import net.denanu.amazia.mechanics.IAmaziaDataProviderEntity;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.minecraft.server.network.ServerPlayerEntity;

public enum Titles {
	DOCTOR		("phd", 		"Dr. ",		TitlePosition.FRONT,  	entity -> entity.getEducation() >= 80),
	PROFESSOR	("professor", 	"Prof. ", 	TitlePosition.FRONT,	entity -> entity.getEducation() >= 80 && entity.getProfession() == AmaziaProfessions.TEACHER),

	MEDICAL		("medical", 	", MD",		TitlePosition.BACK,		entity -> entity.getProfession() == AmaziaProfessions.CLERIC);

	private final String title, id;
	private final TitlePosition position;
	private Predicate<IAmaziaDataProviderEntity> qualifies;

	Titles(final String id, final String title, final TitlePosition position, final Predicate<IAmaziaDataProviderEntity> pred) {
		this.title = title;
		this.qualifies = pred;
		this.position = position;
		this.id = id;
	}

	public String getId() {
		return this.id;
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

	public static String getLastName(final String nameAndTitle) {
		final String name = Titles.removeTitles(nameAndTitle);
		final String[] split = name.split(" ");
		return split[split.length - 1];
	}

	private static String setTitles(String name, final IAmaziaDataProviderEntity entity, final ServerPlayerEntity mayor) {
		for (final Titles title : Titles.values()) {
			if (title.qualifies.test(entity)) {
				if (mayor != null) {
					AmaziaCriterions.GAIN_TITLE.trigger(mayor, title);
				}
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

	public static Titles of(final String key) {
		for (final Titles title : Titles.values()) {
			if (title.id.equals(key)) {
				return title;
			}
		}
		return null;
	}

	public static String updateTitles(final String name, final IAmaziaDataProviderEntity entity, final ServerPlayerEntity mayor) {
		return Titles.setTitles(Titles.removeTitles(name), entity, mayor);
	}

	enum TitlePosition {
		FRONT,
		BACK;
	}
}
