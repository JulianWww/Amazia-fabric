package net.denanu.amazia.mechanics.leveling;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableFloat;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ProfessionLevelManager {
	private Map<Identifier, MutableFloat> professionLevel;

	public ProfessionLevelManager() {
		this.setup();
	}

	public void load(final NbtCompound nbt) {
		for (final String key : nbt.getKeys()) {
			this.professionLevel.put(new Identifier(key), new MutableFloat(nbt.getFloat(key)));
		}
	}

	public NbtCompound save() {
		final NbtCompound nbt = new NbtCompound();
		for (final Entry<Identifier, MutableFloat> val : this.professionLevel.entrySet()) {
			nbt.putFloat(val.getKey().toString(), val.getValue().getValue());
		}
		return nbt;
	}

	private void setup() {
		this.professionLevel = new HashMap<Identifier, MutableFloat>();
		for (final Identifier id : AmaziaProfessions.PROFESSIONS) {
			this.professionLevel.put(id, new MutableFloat(0f));
		}
	}

	public float getLevel(final Identifier profession) {
		return this.professionLevel.get(profession).getValue();
	}

	public void gainXp(final Identifier profession, final float xpVal, final float intelligence) {
		final MutableFloat level = this.professionLevel.get(profession);
		level.add(
				(intelligence - level.getValue()) * xpVal
				);
		return;
	}

	public int getLevelById(final int idx) {
		if (idx < AmaziaProfessions.PROFESSIONS.size()) {
			return (int)(float) this.professionLevel.get(AmaziaProfessions.PROFESSIONS.get(idx)).getValue();
		}
		return 0;
	}
}
