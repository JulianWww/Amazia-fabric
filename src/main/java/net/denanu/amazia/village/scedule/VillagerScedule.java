package net.denanu.amazia.village.scedule;

import net.denanu.amazia.utils.random.ConstrainedGaussianIntRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class VillagerScedule {
	private static RandomnessFactory<Integer> WAKEUP_TIME_FACTORY = 	new ConstrainedGaussianIntRandom(23500f, 	400f, 	24000, 	23000);
	private static RandomnessFactory<Integer> ENDWORK_TIME_FACTORY = 	new ConstrainedGaussianIntRandom( 9000f, 	400f, 	10000, 	 8000);
	private static RandomnessFactory<Integer> SLEEP_TIME_FACTORY = 		new ConstrainedGaussianIntRandom(12500f, 	400f, 	13000, 	12000);

	private long wakeupTime;
	private long endWorkTime;
	private long sleepTime;

	private VillageActivityGroups currentGroup;

	public VillagerScedule() {
	}

	public NbtCompound writeCustomNbt(final NbtCompound nbt) {
		nbt.putLong("WakeupTime", 		this.wakeupTime);
		nbt.putLong("EndWorkTime", 		this.endWorkTime);
		nbt.putLong("StartSleepTime", 	this.sleepTime);
		return nbt;
	}

	public void readCustomNbt(final NbtCompound nbt) {
		if (nbt == null) {
			this.generate();
		} else {
		this.wakeupTime		= VillagerScedule.readOrGenerate(nbt, "WakeupTime", 	VillagerScedule.WAKEUP_TIME_FACTORY);
		this.endWorkTime	= VillagerScedule.readOrGenerate(nbt, "EndWorkTime", 	VillagerScedule.ENDWORK_TIME_FACTORY);
		this.sleepTime		= VillagerScedule.readOrGenerate(nbt, "StartSleepTime", VillagerScedule.SLEEP_TIME_FACTORY);
		}
	}

	private static long readOrGenerate(final NbtCompound nbt, final String key, final RandomnessFactory<Integer> factory) {
		return nbt.contains(key) ? nbt.getLong(key) : factory.next();
	}

	private void generate() {
		this.wakeupTime		= VillagerScedule.WAKEUP_TIME_FACTORY.next();
		this.endWorkTime	= VillagerScedule.ENDWORK_TIME_FACTORY.next();
		this.sleepTime		= VillagerScedule.SLEEP_TIME_FACTORY.next();
	}

	private VillageActivityGroups computeCurrentActionGroup(final World world) {
		long time = world.getTimeOfDay();
		if (this.pastGetUpTime(time) && !this.pastRelaxTime(time)) {
			return VillageActivityGroups.WORK;
		}
		if (!this.isPastBeadTime(time)) {
			return VillageActivityGroups.RECREATION;
		}

		return VillageActivityGroups.SLEEP;
	}

	public void update(final World world) {
		this.currentGroup = this.computeCurrentActionGroup(world);
	}

	public VillageActivityGroups getPerformActionGroup() {
		return this.currentGroup;
	}

	private boolean isPastBeadTime(final long time) {
		return time > this.sleepTime;
	}

	private boolean pastRelaxTime(final long time) {
		return time > this.endWorkTime;
	}

	private boolean pastGetUpTime(final long time) {
		return time > this.wakeupTime || time < 12000;
	}
}
