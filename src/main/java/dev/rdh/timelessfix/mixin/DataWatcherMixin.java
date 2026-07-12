package dev.rdh.timelessfix.mixin;

import dev.rdh.timelessfix.NoOpReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.entity.DataWatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataWatcher.class)
abstract class DataWatcherMixin {
	@Shadow private ReadWriteLock lock;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void removeMainThreadLocking(CallbackInfo ci) {
		this.lock = NoOpReadWriteLock.INSTANCE;
	}
}
