package net.denanu.amazia;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import net.denanu.amazia.mixin.MinecraftClientWorldAccessor;
import net.denanu.amazia.mixin.MinecraftServerWorldAccessor;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
}
