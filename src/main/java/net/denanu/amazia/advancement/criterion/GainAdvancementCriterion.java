package net.denanu.amazia.advancement.criterion;

import java.util.function.Predicate;

import com.google.gson.JsonObject;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.mechanics.title.AbilityRankTitles;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class GainAdvancementCriterion extends AbstractCriterion<net.denanu.amazia.advancement.criterion.GainAdvancementCriterion.Conditions> {
	static final Identifier ID = Identifier.of(Amazia.MOD_ID, "gain_advancement");

	@Override
	public Identifier getId() {
		return GainAdvancementCriterion.ID;
	}

	@Override
	public Conditions conditionsFromJson(final JsonObject jsonObject, final EntityPredicate.Extended extended, final AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new Conditions(extended, AbilityRankTitles.of(JsonHelper.getString(jsonObject, "tier")), JsonHelper.getString(jsonObject, "profession"));
	}

	public void trigger(final ServerPlayerEntity player, final AbilityRankTitles stack, final Identifier villagerType) {
		final Predicate<Conditions> pred = conditions -> conditions.test(stack, villagerType);
		this.trigger(player, pred);
	}


	public static class Conditions
	extends AbstractCriterionConditions {
		private final AbilityRankTitles title;
		private final Identifier villagerType;

		public Conditions(final EntityPredicate.Extended player, final AbilityRankTitles title, final String villager) {
			super(GainAdvancementCriterion.ID, player);
			this.title = title;
			this.villagerType = new Identifier(villager);
		}

		public boolean test(final AbilityRankTitles title, final Identifier villagerType) {
			return this.title == title && this.villagerType.equals(villagerType);
		}

		@Override
		public JsonObject toJson(final AdvancementEntityPredicateSerializer predicateSerializer) {
			final JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("tier", this.title.getKey());
			return jsonObject;
		}
	}
}
