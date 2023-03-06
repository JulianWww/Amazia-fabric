package net.denanu.amazia.economy.itemEconomy;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.EconomyFactory;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

/*
 * An item economy that calculates its prices out of a list of ingredients and adds the prices together, also update ingredients.
 */

public class ItemCompundEconomy extends BaseItemEconomy {
	private final ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients;
	private final RandomnessFactory<Integer> stackSizeFactory;

	public ItemCompundEconomy(final Item itm, final RandomnessFactory<Integer> stackSizeFactory, final ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients) {
		super(itm);
		this.ingredients = ingredients;
		this.stackSizeFactory = stackSizeFactory;
	}

	@Override
	public void fromNbt(final NbtCompound compound) {
	}

	@Override
	public NbtElement toNbt() {
		return null;
	}

	@Override
	protected ItemStack getStack() {
		return new ItemStack(this.itm, this.stackSizeFactory.next());
	}

	@Override
	public void reset() {
	}

	@Override
	public void update() {
	}

	@Override
	public float getPriceDelta(final float quantity, final boolean buy) {
		float delta = 1;
		for (final ImmutablePair<BaseItemEconomy, Float> child : this.ingredients) {
			delta += child.getLeft().getPriceDelta(quantity * child.getRight(), buy);
		}
		return delta;
	}

	@Override
	public float getCurrentPrice() {
		float value = 0;
		for (final ImmutablePair<BaseItemEconomy, Float> child : this.ingredients) {
			value += child.getLeft().getCurrentPrice() * child.getRight();
		}
		return value;
	}

	@Override
	public void setCurrentValue(final float float1) {
	}

	@Override
	public void updatePrice(final float quantity, final boolean buy) {
		for (final ImmutablePair<BaseItemEconomy, Float> child : this.ingredients) {
			child.getLeft().updatePrice(quantity * child.getRight(), buy);
		}
	}

	public static BaseItemEconomy register(final Item itm, final ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients, final RandomnessFactory<Integer> stackSizeFactory, final ImmutableSet<String> professions) {
		return ItemCompundEconomy.register(Amazia.MOD_ID, itm, ingredients, stackSizeFactory, professions);
	}

	public static BaseItemEconomy register(final String modid, final Item itm, final ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients, final RandomnessFactory<Integer> stackSizeFactory, final ImmutableSet<String> professions) {
		return EconomyFactory.register(modid, itm, new ItemCompundEconomy(itm, stackSizeFactory, ingredients), professions);
	}

	@Override
	public boolean hasNbt() {
		return false;
	}
}
