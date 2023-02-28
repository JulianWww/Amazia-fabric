package net.denanu.amazia.advancement.criterion;

import java.util.function.Predicate;

import com.google.gson.JsonObject;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.mechanics.title.Titles;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class GainTitleCriterion extends AbstractCriterion<net.denanu.amazia.advancement.criterion.GainTitleCriterion.Conditions> {
	static final Identifier ID = Identifier.of(Amazia.MOD_ID, "gain_title");

	@Override
	public Identifier getId() {
		return GainTitleCriterion.ID;
	}

	@Override
	public Conditions conditionsFromJson(final JsonObject jsonObject, final EntityPredicate.Extended extended, final AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new Conditions(extended, Titles.of(JsonHelper.getString(jsonObject, "title")));
	}

	public void trigger(final ServerPlayerEntity player, final Titles stack) {
		final Predicate<Conditions> pred = conditions -> conditions.test(stack);
		this.trigger(player, pred);
	}


	public static class Conditions
	extends AbstractCriterionConditions {
		private final Titles title;

		public Conditions(final EntityPredicate.Extended player, final Titles title) {
			super(GainTitleCriterion.ID, player);
			this.title = title;
		}

		public boolean test(final Titles title) {
			return this.title == title;
		}

		@Override
		public JsonObject toJson(final AdvancementEntityPredicateSerializer predicateSerializer) {
			final JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("title", this.title.getId());
			return jsonObject;
		}
	}
}
