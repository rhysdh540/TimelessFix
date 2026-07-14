package dev.rdh.timelessfix.mixin.core;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Timer.class)
abstract class TimerMixin {
	@Shadow private double timeSyncAdjustment;
	@Shadow private long counter;

	@Inject(method = "updateTimer", at = @At("HEAD"))
	private void disableClockSynchronization(CallbackInfo ci) {
		this.timeSyncAdjustment = 1.0;
		this.counter = 0L;
	}
}
