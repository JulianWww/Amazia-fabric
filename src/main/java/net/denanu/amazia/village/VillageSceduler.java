package net.denanu.amazia.village;

import java.util.List;
import java.util.Random;

public class VillageSceduler {
	private Village village;
	
	public VillageSceduler(Village _village) {
		this.village = _village;
	}
	
	public Village getVillage() {
		return this.village;
	}
	
	protected static <E extends Object> E getRandomListElement(List<E> list) {
		Random rand = new Random();
		return list.get(rand.nextInt(list.size()));
	}
	
	public void setChanged() {
		this.village.setChanged();
	}
}
