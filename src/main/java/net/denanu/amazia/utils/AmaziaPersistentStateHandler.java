package net.denanu.amazia.utils;

import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.utils.scanners.ChunkScanner;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class AmaziaPersistentStateHandler extends PersistentState {

	@Override
	public NbtCompound writeNbt(final NbtCompound nbt) {
		nbt.put("economy", Economy.writeNbt());
		nbt.put("scanner", ChunkScanner.writeNbt());
		nbt.putInt("naming", NamingLanguageOptions.NAMINGLANGUAGE.ordinal());
		return nbt;
	}

	public static AmaziaPersistentStateHandler fromNbt(final NbtCompound nbt) {
		Economy.fromNbt(nbt.getCompound("economy"));
		ChunkScanner.fromNbt(nbt.getCompound("scanner"));
		NamingLanguageOptions.update(
				NamingLanguageOptions.values()[nbt.getInt("naming")]
				);
		return new AmaziaPersistentStateHandler();
	}

	public static AmaziaPersistentStateHandler loadDefault() {
		ChunkScanner.init();
		NamingLanguageOptions.update(NamingLanguageOptions.ENGLISH);

		return new AmaziaPersistentStateHandler();
	}

	@Override
	public boolean isDirty() {
		return true;
	}

}
