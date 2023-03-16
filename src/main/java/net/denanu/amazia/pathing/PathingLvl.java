package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PathingLvl {
	// positions are x, z, y
	private final HashMap<Integer, HashMap<Integer, HashMap<Integer, BasePathingNode>>> positionMap;

	PathingLvl() {
		this.positionMap = new HashMap<>();
	}

	public BasePathingNode get(final BlockPos pos) {
		return PathingLvl.gety(PathingLvl.getz(PathingLvl.getx(this.positionMap, pos.getX()), pos.getZ()), pos.getY());
	}

	public void add(final BlockPos pos, final BasePathingNode node) {
		PathingLvl.setz(PathingLvl.setx(this.positionMap, pos.getX()), pos.getZ()).put(pos.getY(), node);
	}

	public HashMap<Integer, BasePathingNode> getRemove(final int x, final int z) {
		return PathingLvl.getz(PathingLvl.getx(this.positionMap, x), z);
	}




	static <E> HashMap<Integer, HashMap<Integer, E>> setx(final HashMap<Integer, HashMap<Integer, HashMap<Integer, E>>> map, final int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, HashMap<Integer, E>>());
		}
		return map.get(key);
	}
	static <E> HashMap<Integer, E> setz(final HashMap<Integer, HashMap<Integer, E>> map, final int key) {
		if (!map.containsKey(key)) {
			map.put(key, new HashMap<Integer, E>());
		}
		return map.get(key);
	}

	static <E> HashMap<Integer, HashMap<Integer, E>> getx(final HashMap<Integer, HashMap<Integer, HashMap<Integer, E>>> map, final int key) {
		if (!map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
	static <E> HashMap<Integer, E> getz(final HashMap<Integer, HashMap<Integer, E>> map, final int key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}
	static <E> E gety(final HashMap<Integer, E> map, final int key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		return map.get(key);
	}

	public BlockPos getRandom(final BlockPos center, final int range) {
		final BasePathingNode node = JJUtils.maxOrNull(
				JJUtils.getOrNull(
						JJUtils.getOrNull(this.positionMap, center.getX() + JJUtils.rand.nextInt(-range, range)),
						center.getY() + JJUtils.rand.nextInt(-range, range)
						)
				);
		return node == null ? null : node.getBlockPos();
	}

	@Nullable
	public BlockPos getRandomVillageEnterNode() {
		return JJUtils.max(
				JJUtils.getRandomExtreme(
						JJUtils.getRandomExtreme(
								this.positionMap.entrySet()
								).entrySet()
						).entrySet()
				).getBlockPos();
	}

	public List<PacketByteBuf> toBuf(final Consumer<PacketByteBuf> meta) {
		final LinkedList<BasePathingNode> nodes = new LinkedList<>();
		for (final HashMap<Integer, HashMap<Integer, BasePathingNode>> xmap : this.positionMap.values()) {
			for (final HashMap<Integer, BasePathingNode> zmap : xmap.values()) {
				nodes.addAll(zmap.values());
			}
		}

		final int partition = 1024;
		final List<PacketByteBuf> out = new LinkedList<>();
		final Iterator<BasePathingNode> iter = nodes.iterator();

		while (iter.hasNext()) {
			final LinkedList<BasePathingNode> nodeBuf = new LinkedList<>();
			while (nodeBuf.size() < partition && iter.hasNext()) {
				nodeBuf.add(iter.next());
			}
			final PacketByteBuf buf = PacketByteBufs.create();
			meta.accept(buf);
			buf.writeCollection(nodeBuf, (buf2, node) -> {
				buf2.writeBlockPos(node.getBlockPos());
				buf2.writeCollection(node.ajacentNodes, (buf3, aj) -> {
					buf3.writeBlockPos(aj.getBlockPos());
				});
			});
			out.add(buf);
		}
		return out;
	}
}
