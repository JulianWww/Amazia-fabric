package net.denanu.amazia.components.components;

import java.util.HashSet;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.BlockPos;

public interface IVillageComponent extends Component {
	HashSet<BlockPos> get();
	void add(BlockPos pos);
	void remove(BlockPos pos);
}
