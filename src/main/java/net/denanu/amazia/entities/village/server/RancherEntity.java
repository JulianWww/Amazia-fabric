package net.denanu.amazia.entities.village.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.components.AmaziaComponents;
import net.denanu.amazia.entities.village.server.goal.mineing.EnterMineGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.ExtendMineGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.FixBrokenMineSeroundingsGoal;
import net.denanu.amazia.entities.village.server.goal.mineing.LightUpMine;
import net.denanu.amazia.entities.village.server.goal.mineing.MoveToEndOfMine;
import net.denanu.amazia.entities.village.server.goal.rancher.BringAnimalsToPen;
import net.denanu.amazia.entities.village.server.goal.rancher.FeedAnimalGoal;
import net.denanu.amazia.entities.village.server.goal.rancher.FetchAnimal;
import net.denanu.amazia.entities.village.server.goal.rancher.LeashAnimal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RancherEntity extends AmaziaVillagerEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);
	
	@Nullable
	public AnimalEntity targetAnimal;

	public RancherEntity(EntityType<? extends PassiveEntity> entityType, World world)  {
		super(entityType, world);
		AmaziaComponents.setCanCollide(this, false);
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.farmer.idle", true));
        return PlayState.CONTINUE;
    }

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<RancherEntity>(this, "controller", 0, this::predicate));
	}
	
	// Rancher
	
	public void searchForTargetAnimal() {
		this.targetAnimal = this.village.getRanching().getUnboundAnimal();
	}
	
	@Override
    protected void initGoals() {
		
		this.goalSelector.add(47, new FeedAnimalGoal(this, 47));
		this.goalSelector.add(48, new BringAnimalsToPen(this, 48));
		this.goalSelector.add(49, new LeashAnimal(this, 49));
		this.goalSelector.add(50, new FetchAnimal(this, 50));
		
        super.registerBaseGoals();
    }
	
	@Override 
	public void mobTick() {
		this.releaseTargetEntityIfDead();
	}

	private void releaseTargetEntityIfDead() {
		if (this.targetAnimal != null && this.targetAnimal.isDead()) {
			this.targetAnimal = null;
		}
		
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	public boolean hasTargetAnimal() {
		return this.targetAnimal != null && this.targetAnimal.isAlive();
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return null;
	}

	@Override
	public HashMap<Item, ArrayList<CraftingRecipe>> getCraftables() {
		return null;
	}

	@Override
	public boolean canDepositItems() {
		return false;
	}

	public int getLeashTime() {
		return 20;
	}

	public int getFeedTime() {
		return 20;
	}
	
	@Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.targetAnimal!=null)  nbt.putUuid("targetAnimal", this.targetAnimal.getUuid());
        return;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
		if (!this.world.isClient && nbt.contains("targetAnimal")) {
        	if (JJUtils.getEntityByUniqueId(nbt.getUuid("targetAnimal"), (ServerWorld)this.world) instanceof AnimalEntity animal) {
        		this.targetAnimal = animal;
        	}
		}
		
    }

}
