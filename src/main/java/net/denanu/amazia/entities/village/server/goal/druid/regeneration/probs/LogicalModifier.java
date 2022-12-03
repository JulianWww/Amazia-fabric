package net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs;

import java.util.ArrayList;

public abstract class LogicalModifier implements AmaziaGenerationProbability {
	ArrayList<AmaziaGenerationProbability> mods = new ArrayList<AmaziaGenerationProbability>();

	public LogicalModifier add(final AmaziaGenerationProbability mod) {
		this.mods.add(mod);
		return this;
	}

	public LogicalModifier build() {
		this.mods.trimToSize();
		return this;
	}
}
