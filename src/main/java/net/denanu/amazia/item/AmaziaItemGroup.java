package net.denanu.amazia.item;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AmaziaItemGroup {
    public static final ItemGroup VILLAGE = FabricItemGroupBuilder.build(
            new Identifier(Amazia.MOD_ID, "tanzanite"), () -> new ItemStack(AmaziaItems.RUBY));
}