package net.denanu.amazia.village.sceduling.opponents;

import net.denanu.amazia.JJUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ProjectileTargeting {
	private static float g = 0.047f;
	private static float d = -0.0104f;

	public static Vec3d getTargeting(final Entity target, final Entity shooter, final double v) {
		final Vec3d center = target.getBoundingBox().getCenter();
		return ProjectileTargeting.getTargeting(
				center.getX() - shooter.getPos().getX(),
				center.getY() - shooter.getPos().getY(),
				center.getZ() - shooter.getPos().getZ(),
				target.getVelocity().getX(),
				target.getVelocity().getY(),
				target.getVelocity().getZ(),
				v
				);
	}

	public static Vec3d getTargeting(
			final double px, final double py, final double pz,
			final double mx, final double my, final double mz,
			final double v
			) {
		final double dist = Math.sqrt(JJUtils.square(px) + JJUtils.square(py) + JJUtils.square(pz));
		final double t = dist/v;
		return ProjectileTargeting.getVec(
				px, py, pz,
				mx, my, mz,
				v, t
				);
	}

	private static Vec3d getVec(
			final double px, final double py, final double pz,
			final double mx, final double my, final double mz,
			final double v, final double t
			) {
		final double exp = Math.exp(ProjectileTargeting.d*t);
		final double xz_scalar = ProjectileTargeting.d/(exp - 1);

		final double vx = (px + mx*t)*xz_scalar;
		final double vz = (pz + mz*t)*xz_scalar;
		final double vy = ((py + my*t)*ProjectileTargeting.d*ProjectileTargeting.d + exp*ProjectileTargeting.g - ProjectileTargeting.d*ProjectileTargeting.g*t-ProjectileTargeting.g)/(ProjectileTargeting.d*(exp-1));
		return new Vec3d(vx, vy, vz);
	}

	public static Vec3d getTargeting(final BlockPos pos, final PersistentProjectileEntity shooter, final float v) {
		return ProjectileTargeting.getTargeting(
				pos.getX() - shooter.getPos().getX(),
				pos.getY() - shooter.getPos().getY(),
				pos.getZ() - shooter.getPos().getZ(),
				0, 0, 0,
				v
				);
	}
}
