package net.denanu.amazia.entities.village.server.goal.bard;

import net.denanu.amazia.entities.village.server.BardEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.sounds.AmaziaBardSounds;
import net.denanu.stoppablesound.events.ServerStoppableEntitySound;
import net.denanu.stoppablesound.events.StoppableSound;

public class PlayMusicToVillagersGoal extends TimedVillageGoal<BardEntity> {
	ServerStoppableEntitySound sound;

	public PlayMusicToVillagersGoal(final BardEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return 200;
	}

	@Override
	protected void takeAction() {
	}

	@Override
	public void start() {
		super.start();
		this.sound = StoppableSound.of(this.entity, AmaziaBardSounds.GATES_OF_GLORY_EVENT, this.entity.getSoundCategory(), 1f, 1f);
	}

	@Override
	public void stop() {
		super.stop();
		this.sound.stop();
		this.sound = null;
	}

}
