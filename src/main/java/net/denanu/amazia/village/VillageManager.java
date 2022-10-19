package net.denanu.amazia.village;

import java.util.HashSet;

import net.minecraft.util.math.BlockPos;

public class VillageManager {
	private HashSet<Village> villages = new HashSet<Village>();
	
	public void addVillage(Village v) {
		villages.add(v);
	}
	public void removeVillage(Village v) {
		villages.remove(v);
	}
	
	public void onPathingBlockUpdate(BlockPos pos) {
		for (Village v: this.villages) {
			v.onPathingBlockUpdate(pos);
		}
	}
	public void onVillageBlockUpdate(BlockPos pos) {
		for (Village v: this.villages) {
			v.onVillageBlockUpdate(pos);
		}
	}
}
