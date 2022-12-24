package net.denanu.amazia.entities.village.server.goal.bard;

import net.denanu.amazia.entities.village.server.BardEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.sounds.AmaziaBardSounds;

public class PlayMusicToVillagersGoal extends TimedVillageGoal<BardEntity> {

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
		this.entity.playSound(null, this.ticker, this.requiredTime);
		this.entity.playSound(AmaziaBardSounds.GATES_OF_GLORY_EVENT, 1f, 1f);
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

}
