package net.denanu.amazia.block;

import net.denanu.amazia.Amazia;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmaziaBlockProperties {
	public static BooleanProperty RESERVED = BooleanProperty.of("has" + Amazia.MOD_ID + "villager");

	public static void setup() {}

	public static boolean setBedReservation(final World world, final BlockPos pos, final boolean val) {
		final BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BedBlock) {
			world.setBlockState(pos, state.with(AmaziaBlockProperties.RESERVED, val));
			return true;
		}
		return false;
	}
}
