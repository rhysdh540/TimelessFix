package dev.rdh.timelessfix.mixin.fastrandom;

import java.util.Random;
import dev.rdh.timelessfix.Xoshiro256StarStarRandom;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
abstract class EntityMixin {
	@Redirect(method = "<init>*", at = @At(value = "NEW", target = "java/util/Random"))
	private Random timelessFix$fastRandom() {
		return new Xoshiro256StarStarRandom();
	}
}
