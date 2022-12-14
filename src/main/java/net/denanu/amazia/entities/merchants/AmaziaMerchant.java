package net.denanu.amazia.entities.merchants;

import java.util.Collection;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.IAmaziaMerchant;
import net.denanu.amazia.economy.ProfessionFactory;
import net.denanu.amazia.entities.merchants.goals.WonderAroundSameYGoal;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.s2c.AmaziaSetTradeOffersS2CPacket;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AmaziaMerchant extends PassiveEntity implements IAmaziaMerchant, IAnimatable {
	
	private AnimationFactory factory = new AnimationFactory(this);
	
	String profession;
	@Nullable
	private PlayerEntity customer;
	@Nullable
	protected AmaziaTradeOfferList offers;
	private BlockPos home;

	public AmaziaMerchant(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
		return;
	}
	
	@Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
		this.home = this.getBlockPos();
		return data;
	}
	
	@Override
    public boolean cannotDespawn() {
    	return true;
    }
	
	@Override
    protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0));
        this.goalSelector.add(3, new TemptGoal(this, 1, Ingredient.ofItems(Items.EMERALD, Items.EMERALD_BLOCK), false));
		this.goalSelector.add(7, new WonderAroundSameYGoal(this, 0.5));
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
		this.goalSelector.add(9, new LookAroundGoal(this));
	}
	
	@Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("home", NbtUtils.toNbt(this.home));
        nbt.putString("profession", this.profession);
        if (this.offers != null) { nbt.put("Trades", this.offers.toNbt()); }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.home = NbtUtils.toBlockPos(nbt.getList("home", NbtList.INT_TYPE));
        this.profession = nbt.getString("profession");
        this.offers = new AmaziaTradeOfferList(nbt.getCompound("Trades"));
        return;
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
	
	// Merchant
	
	@Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (this.isAlive() && !this.hasCustomer() && !this.isBaby()) {
			if (hand == Hand.MAIN_HAND) {
                player.incrementStat(Stats.TALKED_TO_VILLAGER);
            }
            /*if (this.getOffers().isEmpty()) {
                return ActionResult.success(this.world.isClient);
            }*/
            if (!this.world.isClient) {
                this.setCustomer(player);
                this.sendOffers(player, this.getOffers(), this.getName(), this);
            }
            return ActionResult.success(this.world.isClient);
		}
        return super.interactMob(player, hand);
    }
	
	@Override
	public Text getDefaultName() {
		return this.profession != "" ? Text.translatable("profession." + profession) : super.getDefaultName();
		
	}

	public AmaziaTradeOfferList getOffers() {
		if (this.offers == null || this.offers.isEmpty()) {
			Amazia.LOGGER.info("Built trades");
			this.offers = Amazia.economy.buildTrades(this);
		}
		else {
			this.offers.update(this);
		}
		return this.offers;
	}

	@Override
	public float getProfitMargin() {
		return 1.3f;
	}

	@Override
	public Collection<String> getTradePossibilities() {
		if (this.world.isClient) {
			throw new RuntimeException("Client executing serveronly code");
		}
		this.profession = ProfessionFactory.get();
		return Economy.getTrades(8, this.profession);
	}

	@Nullable
	public PlayerEntity getCustomer() {
		return customer;
	}

	@Override
	public void setCustomer(PlayerEntity customer) {
		this.customer = customer;
	}
	
	public boolean hasCustomer() {
		return this.customer != null;
	}

	@Override
	public void setOffersFromServer(AmaziaTradeOfferList offers) {
	}

	@Override
	public boolean isClient() {
		return this.world.isClient;
	}

	@Override
	public void onSellingItem(ItemStack stack) {
	}

	@Override
    public void trade(AmaziaTradeOffer offer) {
        offer.use();
        this.ambientSoundChance = -this.getMinAmbientSoundDelay();
        this.afterUsing(offer);
    }

	private void afterUsing(AmaziaTradeOffer offer) {
		if (this.world.isClient) { throw new RuntimeException("runnign server script on client"); }
		
		offer.updateModifiers(this);
		Amazia.economy.getItem(offer.getKey()).updatePrice(offer.getQuantity(), offer.isBuy());
		ServerPlayNetworking.send((ServerPlayerEntity)this.customer, AmaziaNetworking.SET_TRADE_OFFERS, AmaziaSetTradeOffersS2CPacket.toBuf(this.customer.currentScreenHandler.syncId, this.getOffers()));
	}
}
