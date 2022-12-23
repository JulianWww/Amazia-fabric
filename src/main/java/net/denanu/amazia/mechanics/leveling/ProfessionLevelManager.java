package net.denanu.amazia.mechanics.leveling;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableFloat;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ProfessionLevelManager {
	private Map<Identifier, MutableFloat> professionLevel;
	private float professionEfficency;
	private float levelBoost = 0;

	public ProfessionLevelManager() {
		this.setup();
	}

	public void load(final NbtCompound nbt, final Identifier profession) {
		for (final String key : nbt.getKeys()) {
			this.professionLevel.put(new Identifier(key), new MutableFloat(nbt.getFloat(key)));
		}
		this.updateEfficency(this.getUnmodLevel(profession));
	}

	public NbtCompound save() {
		final NbtCompound nbt = new NbtCompound();
		for (final Entry<Identifier, MutableFloat> val : this.professionLevel.entrySet()) {
			nbt.putFloat(val.getKey().toString(), val.getValue().getValue());
		}
		return nbt;
	}

	private void setup() {
		this.professionLevel = new HashMap<>();
		for (final Identifier id : AmaziaProfessions.PROFESSIONS) {
			this.professionLevel.put(id, new MutableFloat(0f));
		}
	}

	public float getLevel(final Identifier profession) {
		return this.getUnmodLevel(profession) + this.levelBoost;
	}

	private float getUnmodLevel(final Identifier profession) {
		return this.professionLevel.get(profession).getValue();
	}

	public void gainXp(final Identifier profession, final float xpVal, final float intelligence) {
		final MutableFloat level = this.professionLevel.get(profession);
		level.add(
				(intelligence - level.getValue()) * xpVal
				);
		this.updateEfficency(level.getValue());
	}

	public int getLevelById(final int idx) {
		if (idx < AmaziaProfessions.PROFESSIONS.size()) {
			return (int) Math.ceil(this.professionLevel.get(AmaziaProfessions.PROFESSIONS.get(idx)).getValue());
		}
		return 0;
	}



	// EFFECTS

	private void updateEfficency(final float lvl) {
		this.professionEfficency = (float) Math.exp(-0.02*(lvl + this.levelBoost));
	}

	private float getBoonModifier() {
		return 1/(1+this.professionEfficency);
	}

	public float getProfessionEfficency() {
		return this.professionEfficency;
	}

	public float getLevelBoost() {
		return this.levelBoost;
	}

	public void setLevelBoost(final float levelBoost, final Identifier profession) {
		this.levelBoost = levelBoost;
		this.updateEfficency(this.getUnmodLevel(profession));
	}

	private int getTime(final int defaultTime, final boolean depressed) {
		int time = (int)Math.ceil(defaultTime * this.getProfessionEfficency());

		if (depressed) {
			time = time + 200;
		}

		return time;
	}

	private int getBoonTime(final int defaultTime) {
		return (int)(defaultTime * this.getBoonModifier());
	}

	public int getHoingTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getPlantingTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getHarvestingTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getBlockPlaceTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getMineTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getCraftingTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getHealTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getBlessTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getBlessLastTime(final boolean depressed) {
		return this.getBoonTime(20000);
	}

	public float getHealAmount(final boolean depressed) {
		return this.getBoonTime(20);
	}

	public int getPlantGrowTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public float getMineRegeneration(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getGrowRadius(final boolean depressed) {
		return this.getBoonTime(20);
	}

	public int getMaxMineRegrowAbility(final boolean depressed) {
		return this.getBoonTime(10);
	}

	public int getEnchantingTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getEnchantAbility(final Identifier profession) {
		return 1 + (int)this.getLevel(profession);
	}

	public int getAttackInterval(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getLeashTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getFeedTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getAnimalInteractionTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}

	public int getAttackTime(final boolean depressed) {
		return this.getTime(20, depressed);
	}
}
