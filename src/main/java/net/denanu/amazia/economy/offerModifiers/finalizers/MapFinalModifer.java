package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.utils.random.RandomCollection;
import net.denanu.amazia.utils.scanners.ChunkScanner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.structure.Structure;

public class MapFinalModifer extends OfferFinalModifer {
	private final RandomCollection<Identifier> structures;
	private final MapIcon.Type icon;
	private final String nameKey;
	
	public MapFinalModifer(MapIcon.Type icon, String name, Identifier ident) {
		super(ident);
		this.nameKey = name;
		this.icon = icon;
		this.structures = new RandomCollection<Identifier>();
	}
	
	
	public MapFinalModifer add(Identifier structure, float weight) {
		this.structures.add(weight, structure);
		return this;
	}

	@Override
	public void modify(AmaziaTradeOffer offer, LivingEntity merchant) {
		ServerWorld world = (ServerWorld)merchant.world;
		
		Identifier structure = this.structures.next();
		ChunkPos pos = Amazia.chunkScanner.getPos(structure);
		offer.item.setCustomName(Text.translatable(this.nameKey));
		if (pos!= null) {
			BlockPos loc = pos.getBlockPos(6, 0, 6);
			Amazia.chunkScanner.markDirty(structure);
			offer.item = FilledMapItem.createMap(world, loc.getX(), loc.getZ(), (byte)2, true, true);
			MapState.addDecorationsNbt(offer.item, loc, "+", this.icon);
		}
		else {
			offer.disable();
		}
	}



	public MapFinalModifer add(RegistryKey<Structure> key, float weight) {
		return this.add(key.getValue(), weight);
	}

}
