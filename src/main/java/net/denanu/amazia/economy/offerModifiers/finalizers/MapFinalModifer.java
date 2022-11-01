package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.economy.AmaziaTradeOffer;
import net.denanu.amazia.utils.random.RandomCollection;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;

public class MapFinalModifer extends OfferFinalModifer {
	private final RandomCollection<TagKey<Structure>> structures;
	private final MapIcon.Type icon;
	private final String nameKey;
	
	public MapFinalModifer(MapIcon.Type icon, String name, Identifier ident) {
		super(ident);
		this.nameKey = name;
		this.icon = icon;
		this.structures = new RandomCollection<TagKey<Structure>>();
	}
	
	
	public MapFinalModifer add(TagKey<Structure> structure, float weight) {
		this.structures.add(weight, structure);
		return this;
	}

	@Override
	public void modify(AmaziaTradeOffer offer, LivingEntity merchant) {
		ServerWorld world = (ServerWorld)merchant.world;
		
		TagKey<Structure> structure = this.structures.next();
		BlockPos pos = world.locateStructure(structure, merchant.getBlockPos(), 100, true);
		if (pos!= null) {
			 offer.item = FilledMapItem.createMap(world, pos.getX(), pos.getZ(), (byte)2, true, true);
			 FilledMapItem.fillExplorationMap(world, offer.item);
             MapState.addDecorationsNbt(offer.item, pos, "+", this.icon);
             offer.item.setCustomName(Text.translatable(this.nameKey));
		}
		else {
			offer.disable();
		}
	}

}
