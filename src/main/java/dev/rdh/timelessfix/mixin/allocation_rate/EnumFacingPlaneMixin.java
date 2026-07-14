package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnumFacing.Plane.class)
abstract class EnumFacingPlaneMixin {
	@Unique private static volatile EnumFacing[] timelessFix$horizontal;
	@Unique private static volatile EnumFacing[] timelessFix$vertical;

	@Redirect(
		method = "iterator",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing$Plane;facings()[Lnet/minecraft/util/EnumFacing;")
	)
	private EnumFacing[] reuseFacings(EnumFacing.Plane plane) {
		if (plane == EnumFacing.Plane.HORIZONTAL) {
			if (timelessFix$horizontal == null) {
				timelessFix$horizontal = plane.facings();
			}
			return timelessFix$horizontal;
		}
		if (timelessFix$vertical == null) {
			timelessFix$vertical = plane.facings();
		}
		return timelessFix$vertical;
	}
}
