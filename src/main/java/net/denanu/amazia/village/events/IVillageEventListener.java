package net.denanu.amazia.village.events;

public interface IVillageEventListener {
	void receiveEvent(VillageEvents event, EventData data);
	void setGlowing(boolean active);
}
