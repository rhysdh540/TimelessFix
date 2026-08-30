package dev.rdh.timelessfix.mixin.core;

import net.minecraft.util.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Vec3i.class)
abstract class Vec3iMixin {
	@Shadow public abstract int getX();
	@Shadow public abstract int getY();
	@Shadow public abstract int getZ();

	/**
	 * @author ZZZank
	 * @reason use another algorithm with much less hash collision in general
	 */
	@Overwrite
	public int hashCode() {
		return mix(mix(this.getX()) + this.getY()) + this.getZ();
	}

	@Unique
	private static int mix(int x) {
		int y = x * 0x9E3779B9;
		return y ^ (y >>> 16);
	}
}
