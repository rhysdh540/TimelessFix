package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnumFacing.Plane.class)
abstract class EnumFacingPlaneMixin {
	@Unique private static final EnumFacing[] timelessFix$horizontal = {
		EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST
	};
	@Unique private static final EnumFacing[] timelessFix$vertical = {EnumFacing.UP, EnumFacing.DOWN};

	@Redirect(
		method = "iterator",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing$Plane;facings()[Lnet/minecraft/util/EnumFacing;")
	)
	private EnumFacing[] reuseFacings(EnumFacing.Plane plane) {
		return plane == EnumFacing.Plane.HORIZONTAL ? timelessFix$horizontal : timelessFix$vertical;
	}
}
