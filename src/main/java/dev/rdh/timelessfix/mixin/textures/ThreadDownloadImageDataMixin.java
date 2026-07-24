package dev.rdh.timelessfix.mixin.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.resources.IResourceManager;
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
	@Shadow private BufferedImage bufferedImage;
	@Shadow private Thread imageThread;
	@Shadow private boolean textureUploaded;

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

	@Inject(method = "checkTextureUploaded", at = @At("RETURN"))
	private void releaseUploadedImage(CallbackInfo ci) {
		if (this.textureUploaded) {
			this.bufferedImage = null;
			this.imageThread = null;
		}
	}

	@Inject(method = "loadTexture", at = @At("HEAD"), cancellable = true)
	private void keepUploadedTexture(IResourceManager resourceManager, CallbackInfo ci) throws IOException {
		if (this.textureUploaded && this.bufferedImage == null) {
			ci.cancel();
		}
	}
}
