package dev.rdh.timelessfix.mixin.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThreadDownloadImageData.class)
abstract class ThreadDownloadImageDataMixin {
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
}
