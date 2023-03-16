package net.denanu.amazia;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.denanu.amazia.mixin.MinecraftClientWorldAccessor;
import net.denanu.amazia.mixin.MinecraftServerWorldAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class JJUtils {

	public static final Random rand = new Random();

	public static Vec3d UP = new Vec3d(0f, 1f, 0f);

	public static boolean equal(final BlockPos a, final BlockPos b) {
		return a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ();
	}

	@Nullable
	public static <E extends Object> E getRandomSetElement(final Set<E> set) {
		if (set.size() == 0) {
			return null;
		}
		final int item = JJUtils.rand.nextInt(set.size());
		int i = 0;
		for (final E obj : set) {
			if (i == item) {
				return obj;
			}
			i++;
		}
		return null;
	}

	@Nullable
	public static <E extends Object> E getRandomListElement(final List<E> list) {
		if (list.size() == 0) {
			return null;
		}
		return list.get(JJUtils.rand.nextInt(list.size()));
	}

	@Nullable
	public static <E extends Object> E getRandomArrayElement(final E[] arr) {
		if (arr.length == 0) {
			return null;
		}
		return arr[JJUtils.rand.nextInt(arr.length)];
	}

	public static <T extends Object> T getNullableRandomListElement(final List<T> list) {
		if (list != null) {
			return JJUtils.getRandomListElement(list);
		}
		return null;
	}

	@Nullable
	public static Entity getEntityByUniqueId(final UUID uniqueId, final ServerWorld world) {
		if (uniqueId == null) {
			return null;
		}
		return ((MinecraftServerWorldAccessor) world).getEntityManager().getLookup().get(uniqueId);
	}

	@Nullable
	public static Entity getEntityByUniqueId(final UUID uniqueId, final ClientWorld world) {
		if (uniqueId == null) {
			return null;
		}
		return ((MinecraftClientWorldAccessor) world).getEntityManager().getLookup().get(uniqueId);
	}

	public static float power(float base, int power) {
		float out = 1;
		while (power > 0) {
			if ((power & 1) == 1) {
				out = out * base;
			}
			base = base * base;
			power = power >> 1;
		}
		return out;
	}

	public static double square(final double d) {
		return d * d;
	}

	public static float square(final float d) {
		return d * d;
	}

	public static int square(final int d) {
		return d * d;
	}

	public static BlockPos getRandomListElement(final List<BlockPos> tables, final Predicate<BlockState> pred, final World world) {
		return JJUtils.getRandomListElement(tables.stream().filter(pos -> pred.test(world.getBlockState(pos))).toList());
	}

	public static <T> T getRandomExtreme(
			final Set<Entry<Integer, T>> entrySet) {
		if (JJUtils.rand.nextBoolean()) {
			return JJUtils.min(entrySet);
		}
		return JJUtils.max(entrySet);
	}

	public static <T> T min(final Set<Entry<Integer, T>> entrySet) {
		final Iterator<Entry<Integer, T>> iter = entrySet.iterator();
		Entry<Integer, T> next, out = iter.next();


		while (iter.hasNext()) {
			next = iter.next();
			if (out.getKey() > next.getKey()) {
				out = next;
			}
		}
		return out.getValue();
	}

	@Nullable
	public static <T> T max(final Set<Entry<Integer, T>> entrySet) {
		if (entrySet == null || entrySet.isEmpty()) {
			return null;
		}
		final Iterator<Entry<Integer, T>> iter = entrySet.iterator();
		Entry<Integer, T> next, out = iter.next();

		while (iter.hasNext()) {
			next = iter.next();
			if (out.getKey() < next.getKey()) {
				out = next;
			}
		}
		return out.getValue();
	}

	public static <Key, T> T getOrNull(final HashMap<Key, T> positionMap, final Key i) {
		if (positionMap == null) {
			return null;
		}
		return positionMap.get(i);
	}

	public static <T> T maxOrNull(final HashMap<Integer, T> map) {
		if (map == null) {
			return null;
		}
		return JJUtils.max(map.entrySet());
	}
}
