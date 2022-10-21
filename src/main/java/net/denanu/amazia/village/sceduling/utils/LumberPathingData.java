package net.denanu.amazia.village.sceduling.utils;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.structures.LumberFarmStructure;
import net.minecraft.util.math.BlockPos;

public class LumberPathingData extends BlockAreaPathingData<LumberFarmStructure> {
	public LumberPathingData(BlockPos pos, Village _village) {
		super(new LumberFarmStructure(pos, _village), _village);
	}

	@Override
	protected void getAccessPoints(PathingListenerRegistryOperation operation, BlockPos origin, Village v) {
		operation.put(this, v, origin.up());
		this.getSeroundingPoints(operation, origin.east(), v);
		this.getSeroundingPoints(operation, origin.west(), v);
		this.getSeroundingPoints(operation, origin.north(), v);
		this.getSeroundingPoints(operation, origin.south(), v);
	}
	
	private void getSeroundingPoints(PathingListenerRegistryOperation operation, BlockPos origin, Village v) {
		operation.put(this, v, origin);
		operation.put(this, v, origin.up());
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
