package net.denanu.amazia.utils;

import net.minecraft.entity.ai.TargetPredicate;

public class Predicates {
	public static TargetPredicate ALWAYS_TRUE = TargetPredicate.createNonAttackable().setBaseMaxDistance(3.0)
			.setPredicate(v -> true);

	public static void setup() {

	}
}
