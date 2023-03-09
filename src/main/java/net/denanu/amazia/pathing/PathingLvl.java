package net.denanu.amazia.pathing;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.pathing.node.BasePathingNode;
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

	public BlockPos getRandom() {
		return
				JJUtils.getRandomSetElement(
						JJUtils.getRandomSetElement(
								JJUtils.getRandomSetElement(
										this.positionMap.entrySet()
										).getValue().entrySet()
								).getValue().entrySet()
						).getValue().getBlockPos();
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
}
