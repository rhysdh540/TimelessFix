package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class EntityMixin {
	@Shadow public World worldObj;

	@Unique private long timelessFix$brightnessTick = Long.MIN_VALUE;
	@Unique private int timelessFix$brightnessValue;

	@Inject(method = "getBrightnessForRender", at = @At("HEAD"), cancellable = true)
	private void timelessFix$brightnessCacheHit(float partialTicks, CallbackInfoReturnable<Integer> cir) {
		if (this.worldObj != null && this.timelessFix$brightnessTick == this.worldObj.getTotalWorldTime()) {
			cir.setReturnValue(this.timelessFix$brightnessValue);
		}
	}

	@Inject(method = "getBrightnessForRender", at = @At("RETURN"))
	private void timelessFix$brightnessCacheStore(float partialTicks, CallbackInfoReturnable<Integer> cir) {
		if (this.worldObj != null) {
			this.timelessFix$brightnessTick = this.worldObj.getTotalWorldTime();
			this.timelessFix$brightnessValue = cir.getReturnValue();
		}
	}
}
