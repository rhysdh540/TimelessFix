package dev.rdh.timelessfix.mixin;

import net.minecraft.util.LongHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LongHashMap.class)
abstract class LongHashMapMixin {
	/**
	 * @author embeddedt
	 * @reason Use a better hash (from TMCW) that avoids collisions.
	 */
	@Overwrite
	private static int getHashedKey(long key) {
		return (int) key + (int) (key >>> 32) * 92821;
	}
}
