package net.denanu.amazia.pathing.node;

import java.util.List;

import net.minecraft.util.math.BlockPos;

public class ClientPathingNode extends BlockPos {
	public final List<BlockPos> ajacents;

	public ClientPathingNode(final List<BlockPos> ajacents, final BlockPos pos) {
		super(pos);
		this.ajacents = ajacents;
	}
}
