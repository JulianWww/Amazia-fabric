package net.denanu.amazia.entities.village.server.goal.druid;

import java.util.Iterator;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.TimedMultirunVillageGoal;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.AmaziaDruidRegenerator;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.DefaultFillerGenerator;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.OreRarity;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.SingleBlockDruidRegenerator;
import net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs.GenerationProbabilities;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
import net.denanu.amazia.utils.blockPos.BlockPosStream;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class RegenerateMineSubGoal extends TimedMultirunVillageGoal<DruidEntity> {
	public static WeightedRandomCollection<AmaziaDruidRegenerator> generators = new WeightedRandomCollection<>();

	public static AmaziaDruidRegenerator COAL_GEN = RegenerateMineSubGoal.register(OreRarity.VERY_COMMON,
			new SingleBlockDruidRegenerator(GenerationProbabilities.COAL, Blocks.COAL_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator COPPER_GEN = RegenerateMineSubGoal.register(OreRarity.COMMON,
			new SingleBlockDruidRegenerator(GenerationProbabilities.COPPER, Blocks.COPPER_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator LAPIS_GEN = RegenerateMineSubGoal.register(OreRarity.UNCOMMON,
			new SingleBlockDruidRegenerator(GenerationProbabilities.LAPIS, Blocks.LAPIS_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator IRON_GEN = RegenerateMineSubGoal.register(OreRarity.COMMON,
			new SingleBlockDruidRegenerator(GenerationProbabilities.IRON, Blocks.IRON_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator GOLD_GEN = RegenerateMineSubGoal.register(OreRarity.RARE,
			new SingleBlockDruidRegenerator(GenerationProbabilities.GOLD, Blocks.GOLD_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator REDSTONE_GEN = RegenerateMineSubGoal.register(OreRarity.UNCOMMON,
			new SingleBlockDruidRegenerator(GenerationProbabilities.REDSTONE, Blocks.REDSTONE_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator DIAMOND_GEN = RegenerateMineSubGoal.register(OreRarity.RARE,
			new SingleBlockDruidRegenerator(GenerationProbabilities.DIAMOND, Blocks.DIAMOND_ORE.getDefaultState()));
	public static AmaziaDruidRegenerator EMERALD_GEN = RegenerateMineSubGoal.register(OreRarity.UNCOMMON,
			new SingleBlockDruidRegenerator(GenerationProbabilities.EMERALD, Blocks.EMERALD_ORE.getDefaultState()));

	private Iterator<BlockPos> iter;

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
	public void start() {
		super.start();
		if (this.iter == null) {
			this.iter = BlockPosStream.iterate(this.entity.getMine().getBox()).iterator();
		}
	}

	@Override
	protected void takeAction() {
		final var world = (ServerWorld) this.entity.getWorld();

		this.entity.getMine().getBox();

		if (this.entity.getMine() != null && this.entity.getMine().isEmpty(world, this.entity)) {
			var done = true;
			BlockPos pos;
			var regrow = 0;
			final var maxRegrow = this.entity.getMaxRegrowMine();
			for (; this.iter.hasNext();) {
				pos = this.iter.next();

				world.spawnParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0.0,
						0.0, 0.0, 0.0);

				world.getServer().sendMessage(Text.literal(pos.toString()));

				if (world.getBlockState(pos).isOf(Blocks.AIR)) {
					if (this.entity.getMineRagenerationAbility() > this.entity.getRandom().nextFloat()) {
						final var gen = RegenerateMineSubGoal.getGen();
						if (gen.test(this.maxCount, this.entity.getRandom(), pos, world)) {
							gen.place(world, pos);
							return;
						}
					}
					DefaultFillerGenerator.place(world, pos);
					ActivityFoodConsumerMap.regrowMineUseFood(this.entity);
					AmaziaXpGainMap.gainMineRegrowXp(this.entity);
					world.setBlockState(pos, Blocks.STONE.getDefaultState());
					regrow++;
					if (this.entity.isHungry() || regrow > maxRegrow) {
						done = false;
						break;
					}
				}
			}
			if (done || !this.iter.hasNext()) {
				Amazia.getVillageManager().onPathingBlockUpdate(this.entity.getMine().getMainEntrance());
				this.entity.getMine().resetMine();
				this.leaveMine();
			}
		} else {
			this.leaveMine();
		}
	}

	private static AmaziaDruidRegenerator getGen() {
		return RegenerateMineSubGoal.generators.next();
	}

	private void leaveMine() {
		this.entity.leaveMine();
		this.iter = null;
		this.terminate();
	}

}
