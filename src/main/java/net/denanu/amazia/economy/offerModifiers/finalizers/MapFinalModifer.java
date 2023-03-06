package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.utils.random.WeightedRandomCollection;
import net.denanu.amazia.utils.scanners.ChunkScanner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.structure.Structure;

public class MapFinalModifer extends OfferFinalModifer {
	private final WeightedRandomCollection<Identifier> structures;
	private final MapIcon.Type icon;
	private final String nameKey;

	public MapFinalModifer(final MapIcon.Type icon, final String name, final Identifier ident) {
		super(ident);
		this.nameKey = name;
		this.icon = icon;
		this.structures = new WeightedRandomCollection<>();
	}


	public MapFinalModifer add(final Identifier structure, final float weight) {
		this.structures.add(weight, structure);
		return this;
	}

	@Override
	public void modify(final AmaziaTradeOffer offer, final LivingEntity merchant) {
		final ServerWorld world = (ServerWorld)merchant.world;

		final Identifier structure = this.structures.next();
		final ChunkPos pos = ChunkScanner.getPos(structure);
		offer.item.setCustomName(Text.translatable(this.nameKey));
		if (pos!= null) {
			final BlockPos loc = pos.getBlockPos(6, 0, 6);
			ChunkScanner.markDirty(structure);
			offer.item = FilledMapItem.createMap(world, loc.getX(), loc.getZ(), (byte)2, true, true);
			MapState.addDecorationsNbt(offer.item, loc, "+", this.icon);
		}
		else {
			offer.disable();
		}
	}



	public MapFinalModifer add(final RegistryKey<Structure> key, final float weight) {
		return this.add(key.getValue(), weight);
	}

}
