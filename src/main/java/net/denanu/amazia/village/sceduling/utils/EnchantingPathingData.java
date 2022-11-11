package net.denanu.amazia.village.sceduling.utils;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.structures.LumberFarmStructure;
import net.minecraft.util.math.BlockPos;

public class EnchantingPathingData extends BlockAreaPathingData<LumberFarmStructure> {
	public EnchantingPathingData(BlockPos pos, Village _village) {
		super(new LumberFarmStructure(pos, _village), _village);
	}

	@Override
	protected void getAccessPoints(PathingListenerRegistryOperation operation, BlockPos origin, Village v) {
		operation.put(this, v, origin.east());
		operation.put(this, v, origin.west());
		operation.put(this, v, origin.north());
		operation.put(this, v, origin.south());
	}

	@Override
	public BlockPos getAccessPoint() {
		return JJUtils.getRandomSetElement(this.getPathingOptions());
	}
	
	@Override
	public void destroy(Village village) {
		super.destroy(village);
		this.getPos().destroy();
	}
}
