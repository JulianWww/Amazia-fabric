package net.denanu.amazia.entities.village.server;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.InitializationException;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.utils.CuboidSampler;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmaziaEntity extends PassiveEntity {
	protected Village village;
	protected BlockPos villageCorePos;
	private CuboidSampler villageSampler;
	private final int SCAN_ATTEMTS = 10;

	protected AmaziaEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
		//this.navigation = new PathFinder(this);
		this.villageSampler = new CuboidSampler(this.getPos(), 6, 6, 6);
	}

	@Override
	public PassiveEntity createChild(ServerWorld var1, PassiveEntity var2) {
		return null;
	}
	
	private void discoverVillage() {
		this.villageSampler.setPos(this.getPos());
		for (int idx = 0; idx < SCAN_ATTEMTS; idx++) {
			BlockPos pos = this.villageSampler.getPos();
			this.world.setBlockState(pos, Blocks.EMERALD_BLOCK.getDefaultState());
			BlockState state =  this.world.getBlockState(pos);
			if (state.getBlock().equals(AmaziaBlocks.VILLAGE_CORE)) {
				this.villageCorePos = pos;
				BlockEntity entity = world.getBlockEntity(pos);
				if (entity instanceof VillageCoreBlockEntity core) {
					this.village = core.getVillage();
					Amazia.LOGGER.info("Villager found village");
				}
				else {
					throw new InitializationException("Village Core Block at " + pos.toString() + " missing block entity");
				}
			}
		}
	}
	
	public boolean hasVillage() {
		return this.village != null;
	}
	public Village getVillage() {
		return this.village;
	}
	
	@Override
    public void tick() {
		super.tick();
		if (!this.world.isClient) {
			this.update();
		}
	}

	private void update() {
		if (this.village == null) { this.discoverVillage(); return; }
	}
}
