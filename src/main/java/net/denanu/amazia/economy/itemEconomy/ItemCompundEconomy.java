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
	
	public ItemCompundEconomy(Item itm, RandomnessFactory<Integer> stackSizeFactory, ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients) {
		super(itm);
		this.ingredients = ingredients;
		this.stackSizeFactory = stackSizeFactory;
	}

	@Override
	public void fromNbt(NbtCompound compound) {
	}

	@Override
	public NbtElement toNbt() {
		return null;
	}

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
	public float getPriceDelta(float quantity, boolean buy) {
		float delta = 1;
		for (ImmutablePair<BaseItemEconomy, Float> child : this.ingredients) {
			delta += child.getLeft().getPriceDelta(quantity * child.getRight(), buy);
		}
		return delta;
	}

	@Override
	public float getCurrentPrice() {
		float value = 0;
		for (ImmutablePair<BaseItemEconomy, Float> child : this.ingredients) {
			value += child.getLeft().getCurrentPrice() * child.getRight();
		}
		return value;
	}

	@Override
	public void setCurrentValue(float float1) {
	}

	@Override
	public void updatePrice(float quantity, boolean buy) {
		for (ImmutablePair<BaseItemEconomy, Float> child : this.ingredients) {
			child.getLeft().updatePrice(quantity * child.getRight(), buy);;
		}
	}
	
	public static BaseItemEconomy register(Item itm, ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients, RandomnessFactory<Integer> stackSizeFactory, ImmutableSet<String> professions) {
		return register(Amazia.MOD_ID, itm, ingredients, stackSizeFactory, professions);
	}
	
	public static BaseItemEconomy register(String modid, Item itm, ImmutableSet<ImmutablePair<BaseItemEconomy, Float>> ingredients, RandomnessFactory<Integer> stackSizeFactory, ImmutableSet<String> professions) {
		return EconomyFactory.register(Amazia.MOD_ID, itm, new ItemCompundEconomy(itm, stackSizeFactory, ingredients), professions);
	}
	
	@Override
	public boolean hasNbt() {
		return false;
	}
}
