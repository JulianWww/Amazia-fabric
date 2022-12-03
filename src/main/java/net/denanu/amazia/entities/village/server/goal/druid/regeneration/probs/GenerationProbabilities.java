package net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs;

public class GenerationProbabilities {
	public static final AmaziaGenerationProbability COPPER = 		new TrapezoidHeightModifier(112, 47, -16);
	public static final AmaziaGenerationProbability COAL = 			new UniformHeightPlacementOffsetter(320, -63);
	public static final AmaziaGenerationProbability LAPIS = 		new UniformHeightPlacementOffsetter(64, -63);
	public static final AmaziaGenerationProbability IRON = 			new OrModifier()
			.add(new UniformHeightPlacementOffsetter(72, -63))
			//.add(new TrapezoidHeightModifier(54, 15, -24))
			.add(new TrapezoidHeightModifier(320, 255, 128));
	public static final AmaziaGenerationProbability GOLD = 			new OrModifier()
			.add(new UniformHeightPlacementOffsetter(-32, -64))
			.add(new TrapezoidHeightModifier(32, -16, -64));
	//TODO: public static final AmaziaGenerationProbability GOLD_BADLANDS = new UniformHeightPlacementOffsetter(256, 32);
	public static final AmaziaGenerationProbability REDSTONE = 		new UniformHeightPlacementOffsetter(15, -63);
	public static final AmaziaGenerationProbability DIAMOND = 		new UniformHeightPlacementOffsetter(14, -63);
	public static final AmaziaGenerationProbability EMERALD = 		new TrapezoidHeightModifier(320, 256, -16);
}
