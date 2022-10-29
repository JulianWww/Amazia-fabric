package net.denanu.amazia.entities.merchants;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AmaziaMerchant extends PassiveEntity implements Merchant, IAnimatable {
	
	private AnimationFactory factory = new AnimationFactory(this);
	
	@Nullable
	private PlayerEntity customer;
	@Nullable
	protected TradeOfferList offers;

	public AmaziaMerchant(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public PassiveEntity createChild(ServerWorld var1, PassiveEntity var2) {
		return null;
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
		data.addAnimationController(new AnimationController<AmaziaMerchant>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	public static DefaultAttributeContainer.Builder setAttributes() {
        return PassiveEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }

	@Override
	public void setCustomer(PlayerEntity customer) {
		this.customer = customer;
	}

	@Override
	public PlayerEntity getCustomer() {
		return this.customer;
	}

	@Override
	public TradeOfferList getOffers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOffersFromServer(TradeOfferList var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trade(TradeOffer var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSellingItem(ItemStack var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getExperience() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setExperienceFromServer(int var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLeveledMerchant() {
		return false;
	}

	@Override
	public SoundEvent getYesSound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClient() {
		return this.world.isClient;
	}
}
