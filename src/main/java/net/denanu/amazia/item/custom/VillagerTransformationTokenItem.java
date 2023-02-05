package net.denanu.amazia.item.custom;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.ChildEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

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
				final NbtCompound nbt = villager.writeNbt(new NbtCompound());
				final AmaziaVillagerEntity other = this.type.create(user.world);
				other.readNbt(nbt);
				villager.discard();
				user.world.spawnEntity(other);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}