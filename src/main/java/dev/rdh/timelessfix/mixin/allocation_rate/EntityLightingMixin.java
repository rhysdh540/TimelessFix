package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
abstract class EntityLightingMixin {
	@Unique private static final ThreadLocal<BlockPos.MutableBlockPos> timelessFix$lightPosition =
		ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);

	@Redirect(
		method = {"getBrightnessForRender", "getBrightness"},
		at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/util/BlockPos;")
	)
	private BlockPos reuseLightPosition(double x, double y, double z) {
		return timelessFix$lightPosition.get().set(
			MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)
		);
	}
}
