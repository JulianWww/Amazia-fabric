package net.denanu.amazia.entities.village.server.goal.bard;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.BardEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.sounds.AmaziaBardSounds;
import net.denanu.stoppablesound.events.ServerStoppableEntitySound;
import net.denanu.stoppablesound.events.StoppableSound;
import net.denanu.stoppablesound.sounds.LongSoundEvent;

public class PlayMusicToVillagersGoal extends TimedVillageGoal<BardEntity> {
	private ServerStoppableEntitySound sound;
	private LongSoundEvent soundEvent;
	private int happyness_gained;

	public PlayMusicToVillagersGoal(final BardEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return this.entity.getTarget() != null && this.entity.getTarget().squaredDistanceTo(this.entity) < 64;
	}

	@Override
	protected int getRequiredTime() {
		return this.soundEvent.getLength();
	}

	@Override
	protected void takeAction() {
		this.entity.setTarget(null);

		this.applyHappyness(HappynessMap::gainBardPlayInVillageEndHappyness);

		if (this.happyness_gained < 4) {
			this.entity.looseHappyness(5);
		}
		else {
			this.entity.gainHappyness(this.happyness_gained);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.ticker % 40 == 0) {
			this.applyHappyness(HappynessMap::gainBardPlayInVillageConinuousHappyness);
		}
	}

	private void applyHappyness(final HappynessCalculator calc) {
		for (final AmaziaVillagerEntity entity : this.entity.world.getEntitiesByClass(AmaziaVillagerEntity.class, this.entity.getBoundingBox().expand(16), e -> true)) {
			if (entity != this.entity) {
				this.happyness_gained = this.happyness_gained + calc.run(entity);
			}
		}
	}

	@Override
	public void start() {
		this.soundEvent = AmaziaBardSounds.BARD_DAYTIME_MUSIC.next();
		this.sound = StoppableSound.of(this.entity, this.soundEvent, this.entity.getSoundCategory(), 1f, 1f).play();

		this.happyness_gained = 0;

		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		this.sound.stop();
		this.sound = null;
	}

	interface HappynessCalculator{
		int run(AmaziaVillagerEntity entity);
	}
}
