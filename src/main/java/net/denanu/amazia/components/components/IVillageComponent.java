package net.denanu.amazia.components.components;

import java.util.List;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.BlockPos;

public interface IVillageComponent extends Component {
	public List<BlockPos> get();
	public void add(BlockPos pos);
}
