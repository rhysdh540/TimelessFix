package dev.rdh.timelessfix.mixin.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.IImageBuffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadDownloadImageData.class)
abstract class ThreadDownloadImageDataMixin {
	@Shadow @Final @Mutable private IImageBuffer imageBuffer;

	private static final AtomicInteger THREAD_NUMBER = new AtomicInteger();
	private static final Executor EXECUTOR = Executors.newFixedThreadPool(4, task -> {
		Thread thread = new Thread(task, "Skin Downloader #" + THREAD_NUMBER.incrementAndGet());
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		return thread;
	});

	@Redirect(method = "loadTextureFromServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;start()V"))
	private void useSharedExecutor(Thread download) {
		EXECUTOR.execute(download);
	}

	@Inject(method = "setBufferedImage", at = @At("TAIL"))
	private void releaseCompletedCallback(CallbackInfo ci) {
		this.imageBuffer = null;
	}
}
