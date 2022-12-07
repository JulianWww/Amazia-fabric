package net.denanu.amazia.entities.village.server.goal.druid;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.AmaziaDruidRegenerator;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.DefaultFillerGenerator;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.OreRarity;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.SingleBlockDruidRegenerator;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs.GenerationProbabilities;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class RegenerateMineSubGoal extends TimedVillageGoal<DruidEntity> {
	public static WeightedRandomCollection<AmaziaDruidRegenerator> generators = new WeightedRandomCollection<AmaziaDruidRegenerator>();

	public static AmaziaDruidRegenerator COAL_GEN 		= RegenerateMineSubGoal.register(OreRarity.VERY_COMMON,	new SingleBlockDruidRegenerator(GenerationProbabilities.COAL, 		Blocks.COAL_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator COPPER_GEN 	= RegenerateMineSubGoal.register(OreRarity.COMMON,  	new SingleBlockDruidRegenerator(GenerationProbabilities.COPPER, 	Blocks.COPPER_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator LAPIS_GEN	 	= RegenerateMineSubGoal.register(OreRarity.UNCOMMON,	new SingleBlockDruidRegenerator(GenerationProbabilities.LAPIS, 		Blocks.LAPIS_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator IRON_GEN	 	= RegenerateMineSubGoal.register(OreRarity.COMMON,		new SingleBlockDruidRegenerator(GenerationProbabilities.IRON, 		Blocks.IRON_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator GOLD_GEN	 	= RegenerateMineSubGoal.register(OreRarity.RARE,		new SingleBlockDruidRegenerator(GenerationProbabilities.GOLD, 		Blocks.GOLD_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator REDSTONE_GEN	= RegenerateMineSubGoal.register(OreRarity.UNCOMMON,	new SingleBlockDruidRegenerator(GenerationProbabilities.REDSTONE, 	Blocks.REDSTONE_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator DIAMOND_GEN 	= RegenerateMineSubGoal.register(OreRarity.RARE,		new SingleBlockDruidRegenerator(GenerationProbabilities.DIAMOND,	Blocks.DIAMOND_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator EMERALD_GEN	= RegenerateMineSubGoal.register(OreRarity.UNCOMMON,	new SingleBlockDruidRegenerator(GenerationProbabilities.EMERALD, 	Blocks.EMERALD_ORE.getDefaultState()));


	public static AmaziaDruidRegenerator register(final OreRarity weight, final AmaziaDruidRegenerator val) {
		return RegenerateMineSubGoal.register(weight.getValue(), val);
	}
	public static AmaziaDruidRegenerator register(final float weight, final AmaziaDruidRegenerator val) {
		RegenerateMineSubGoal.generators.add(weight, val);
		return val;
	}

	public RegenerateMineSubGoal(final DruidEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getBlockPlaceTime();
	}

	@Override
	protected void takeAction() {
		Amazia.LOGGER.info("RegeneratedMine");

		final ServerWorld world = (ServerWorld) this.entity.getWorld();
		if (this.entity.getMine().isEmpty(world, this.entity)) {
			BlockPos.stream(this.entity.getMine().getBox()).forEach(pos -> {
				if (this.entity.getMineRagenerationAbility() > this.entity.getRandom().nextFloat()) {
					final AmaziaDruidRegenerator gen = RegenerateMineSubGoal.getGen();
					if (gen.test(this.requiredTime, this.entity.getRandom(), pos, world)) {
						gen.place(world, pos);
						return;
					}
				}
				DefaultFillerGenerator.place(world, pos);
			});
			Amazia.getVillageManager().onPathingBlockUpdate(this.entity.getMine().getMainEntrance());
			this.entity.getMine().resetMine();
			ActivityFoodConsumerMap.regrowMineUseFood(this.entity);
		}
		this.entity.leaveMine();
	}

	private static AmaziaDruidRegenerator getGen() {
		return RegenerateMineSubGoal.generators.next();
	}

}
