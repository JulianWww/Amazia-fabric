package net.denanu.amazia.item.custom;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.ChildEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class VillagerTransformationTokenItem<T extends AmaziaVillagerEntity> extends Item {
	private final EntityType<T> type;

	public VillagerTransformationTokenItem(final Settings settings, final EntityType<T> villager) {
		super(settings);
		this.type = villager;
	}

	@Override
	public boolean hasGlint(final ItemStack stack) {
		return true;
	}

	@Override
	public ActionResult useOnEntity(final ItemStack stack, final PlayerEntity user, final LivingEntity entity, final Hand hand) {
		if (entity instanceof final AmaziaVillagerEntity villager && villager.getType() != this.type && !(villager instanceof ChildEntity)) {
			if (!user.world.isClient) {
				VillagerTransformationTokenItem.copy(villager, user.world, this.type);
				Criteria.USING_ITEM.trigger((ServerPlayerEntity)user, stack);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	public static <T extends AmaziaVillagerEntity> void copy(final AmaziaVillagerEntity villager, final World world, final EntityType<T> type) {
		final NbtCompound nbt = villager.writeNbt(new NbtCompound());
		final AmaziaVillagerEntity other = type.create(world);
		other.readNbt(nbt);
		villager.discard();
		world.spawnEntity(other);
	}
}
