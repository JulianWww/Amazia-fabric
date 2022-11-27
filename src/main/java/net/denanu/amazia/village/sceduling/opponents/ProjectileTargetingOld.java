package net.denanu.amazia.village.sceduling.opponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Deprecated
public class ProjectileTargetingOld {
	public static float g=0.025f;
	public static double acc = Math.pow(10, -13);

	public static double CalculateAcceleration( final double PAtTMinus2,  final double PAtTMinus1,  final double PAtTMinus0)
	{
		return PAtTMinus2 - 2.0 * PAtTMinus1 + PAtTMinus0;
	}

	public static double CalculateVelocity( final double PAtTMinus2,  final double PAtTMinus1,  final double PAtTMinus0)
	{
		return PAtTMinus2 / 2.0 - 2.0 * PAtTMinus1 + 3.0 * PAtTMinus0 / 2.0;
	}

	public static Vec3d aimAtTarget(final Entity target, final Entity shooter, final float speed) {
		final Vec3d center = target.getBoundingBox().getCenter();
		final Vec3d vector = ProjectileTargetingOld.intercept(
				target.getVelocity().getX(),
				target.getVelocity().getY(),
				target.getVelocity().getZ(),
				center.getX() - shooter.getPos().getX(),
				center.getY() - shooter.getPos().getY(),
				center.getZ() - shooter.getPos().getZ(),
				speed
				);
		return vector;
	}
	public static Vec3d aimAtTarget(final BlockPos pos, final Entity shooter, final float speed) {
		ProjectileTargetingOld.g = 0.025f;
		return ProjectileTargetingOld.intercept(
				0f,0f,0f,
				pos.getX() - shooter.getPos().getX() + 0.5,
				pos.getY() - shooter.getPos().getY() + 1,
				pos.getZ() - shooter.getPos().getZ() + 0.5,
				speed
				);
	}

	public static Vec3d intercept(
			final double vx, final double vy, final double vz,
			final double px, final double py, final double pz,
			final double v
			)
	{
		final List<Double> roots = ProjectileTargetingOld.rootsOf(
				ProjectileTargetingOld.g*ProjectileTargetingOld.g,
				2*ProjectileTargetingOld.g*vy,
				vx*vx + vy*vy + vz*vz - v*v,
				2*(vx*px + vy*py + vz*pz),
				px*px + py*py + pz*pz
				);

		final Optional<Double> t = roots.stream().filter(ProjectileTargetingOld::nonNegative).min(Double::compare);

		return t.isPresent() ? new Vec3d(
				px / t.get() + vx,
				py / t.get() + vy + ProjectileTargetingOld.g*t.get(),
				pz / t.get() + vz
				) : null;
	}

	private static boolean nonNegative(final double val) {
		return val >= 0;
	}

	public static List<Double> rootsOf( final double A,  double B,  double C,  double D,  double E)
	{
		final List<Double> roots = new ArrayList<Double>();
		if (A == 0.0)
		{
			if (B == 0.0)
			{
				if (C == 0.0)
				{
					if (D == 0.0)
					{
						if (E == 0.0) {
							roots.add(null);
						}
					} else {
						roots.add(-D / E);
					}
				} else {
					roots.add((-D + Math.sqrt(D * D - 4 * C * E)) / (2.0 * C));
					roots.add((-D - Math.sqrt(D * D - 4 * C * E)) / (2.0 * C));
				}
			}
			else
			{
				C /= B;
				D /= B;
				E /= B;
				final double F = (3.0 * D - C * C) / 3.0;
				final double G = (2.0 * C * C * C - 9.0 * C * D + 27.0 * E) / 27.0;
				final double H = G * G / 4.0 + F * F * F / 27.0;
				if (H > 0)
				{
					double intermediate = -G / 2.0 + Math.sqrt(H);
					final double m = intermediate < 0.0 ? -Math.pow(-intermediate, 1.0 / 3.0) : Math.pow(intermediate, 1.0 / 3.0);
					intermediate -= 2.0 * Math.sqrt(H);
					final double n = intermediate < 0.0 ? -Math.pow(-intermediate, 1.0 / 3.0) : Math.pow(intermediate, 1.0 / 3.0);
					roots.add(m + n - C / 3.0);
				}
				else
				{
					final double intermediate = Math.sqrt(G * G / 4.0 - H);
					final double rc = intermediate < 0.0 ? -Math.pow(-intermediate, 1.0 / 3.0) : Math.pow(intermediate, 1.0 / 3.0);
					final double theta = Math.acos(-G / (2.0 * intermediate)) / 3.0;
					roots.add(2.0 * rc * Math.cos(theta) - C / 3.0);
					roots.add(-rc * (Math.cos(theta) + Math.sqrt(3.0) * Math.sin(theta)) - C / 3.0);
					roots.add(-rc * (Math.cos(theta) - Math.sqrt(3.0) * Math.sin(theta)) - C / 3.0);
				}
				if (F + G + H == 0.0)
				{
					final double intermediate = E < 0.0 ? Math.pow(-E, 1.0 / 3.0) : -Math.pow(E, 1.0 / 3.0);
					roots.clear();
					roots.add(intermediate);
					roots.add(intermediate);
					roots.add(intermediate);
				}
			}
		}
		else
		{
			B /= A;
			C /= A;
			D /= A;
			E /= A;
			final double F = C - 3.0 * B * B / 8.0;
			final double G = D + B * B * B / 8.0 - B * C / 2.0;
			final double H = E - 3.0 * B * B * B * B / 256.0 + B * B * C / 16.0 - B * D / 4.0;
			final double b = F / 2.0;
			final double c = (F * F - 4.0 * H) / 16.0;
			final double d = G * G / -64.0;
			final double f = (3.0 * c - b * b) / 3.0;
			final double g = (2.0 * b * b * b - 9.0 * b * c + 27.0 * d) / 27.0;
			final double h = g * g / 4.0 + f * f * f / 27.0;
			double y1;
			double y2r;
			double y2i;
			double y3r;
			double y3i;
			if (h > 0.0)
			{
				double intermediate = -g / 2.0 + Math.sqrt(h);
				final double m = intermediate < 0.0 ? -Math.pow(-intermediate, 1.0 / 3.0) : Math.pow(intermediate, 1.0 / 3.0);
				intermediate -= 2.0 * Math.sqrt(h);
				final double n = intermediate < 0.0 ? -Math.pow(-intermediate, 1.0 / 3.0) : Math.pow(intermediate, 1.0 / 3.0);
				y1 = m + n - b / 3.0;
				y2r = (m + n) / -2.0 - b / 3.0;
				y2i = (m - n) / 2.0 * Math.sqrt(3.0);
				y3r = (m + n) / -2.0 - b / 3.0;
				y3i = (m - n) / 2.0 * Math.sqrt(3.0);
			}
			else
			{
				final double intermediate = Math.sqrt(g * g / 4.0 - h);
				final double rc = intermediate < 0.0 ? -Math.pow(-intermediate, 1.0 / 3.0) : Math.pow(intermediate, 1.0 / 3.0);
				final double theta = Math.acos(-g / (2.0 * intermediate)) / 3.0;
				y1 = 2.0 * rc * Math.cos(theta) - b / 3.0;
				y2r = -rc * (Math.cos(theta) + Math.sqrt(3.0) * Math.sin(theta)) - b / 3.0;
				y2i = 0.0;
				y3r = -rc * (Math.cos(theta) - Math.sqrt(3.0) * Math.sin(theta)) - b / 3.0;
				y3i = 0.0;
			}
			if (f + g + h == 0.0)
			{
				final double intermediate = d < 0.0 ? Math.pow(-d, 1.0 / 3.0) : -Math.pow(d, 1.0 / 3.0);
				y1 = intermediate;
				y2r = intermediate;
				y2i = 0.0;
				y3r = intermediate;
				y3i = 0.0;
			}
			double p;
			double q;
			if (h <= 0.0)
			{
				int zeroCheck = 0;
				final double[] cubicRoots = new double[] { y1, y2r, y3r };
				Arrays.sort(cubicRoots);
				p = Math.sqrt(cubicRoots[1]);
				q = Math.sqrt(cubicRoots[2]);
				if (Math.abs(y1) < ProjectileTargetingOld.acc)
				{
					p = Math.sqrt(y2r);
					q = Math.sqrt(y3r);
					zeroCheck = 1;
				}
				if (Math.abs(y2r) < ProjectileTargetingOld.acc)
				{
					p = Math.sqrt(y1);
					q = Math.sqrt(y3r);
					zeroCheck += 2;
				}
				if (Math.abs(y3r) <= ProjectileTargetingOld.acc)
				{
					p = Math.sqrt(y1);
					q = Math.sqrt(y2r);
					zeroCheck += 4;
				}
				switch (zeroCheck)
				{
				case 3:
					p = Math.sqrt(y3r);
					break;
				case 5:
					p = Math.sqrt(y2r);
					break;
				case 6:
					p = Math.sqrt(y1);
					break;
				}
				if (y1 < ProjectileTargetingOld.acc || y2r < ProjectileTargetingOld.acc || y3r < ProjectileTargetingOld.acc)
				{
					if (E == 0.0) {
						roots.add(0.0);
					}
				}
				else
				{
					double r;
					if (zeroCheck < 5) {
						r = G / (-8.0 * p * q);
					} else {
						r = 0.0;
					}
					final double s = B / 4.0;
					roots.add(p + q + r - s);
					roots.add(p - q - r - s);
					roots.add(-p + q - r - s);
					roots.add(-p - q + r - s);
				}
			}
			else
			{
				final double r2mod = Math.sqrt(y2r * y2r + y2i * y2i);
				final double y2mod = Math.sqrt((r2mod - y2r) / 2.0);
				final double x2mod = y2i / (2.0 * y2mod);
				p = x2mod + y2mod;
				final double r3mod = Math.sqrt(y3r * y3r + y3i * y3i);
				final double y3mod = Math.sqrt((r3mod - y3r) / 2.0);
				final double x3mod = y3i / (2.0 * y3mod);
				q = x3mod + y3mod;
				final double r = G / (-8.0 * (x2mod * x3mod + y2mod * y3mod));
				final double s = B / 4.0;
				roots.add(x2mod  + x3mod + r - s);
				roots.add(-x2mod - x3mod + r - s);
			}
		}
		for (int i = 0; i != roots.size(); i++) {
			if (Double.isInfinite(roots.get(i)) || Double.isNaN(roots.get(i))) {
				roots.remove(i--);
			}
		}
		roots.sort(Double::compare);
		return roots;
	}
}
