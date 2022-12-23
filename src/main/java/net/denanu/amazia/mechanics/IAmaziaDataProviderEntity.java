package net.denanu.amazia.mechanics;

import net.denanu.amazia.mechanics.education.IAmaziaEducatedEntity;
import net.denanu.amazia.mechanics.happyness.IAmaziaHappynessEntity;
import net.denanu.amazia.mechanics.hunger.IAmaziaFoodConsumerEntity;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;
import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public interface IAmaziaDataProviderEntity extends
IAmaziaLevelProviderEntity,
IAmaziaFoodConsumerEntity,
IAmaziaIntelligenceEntity,
IAmaziaEducatedEntity,
IAmaziaHappynessEntity
{}
