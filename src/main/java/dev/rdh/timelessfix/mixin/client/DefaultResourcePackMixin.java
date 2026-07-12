package dev.rdh.timelessfix.mixin.client;

import java.io.File;
import net.minecraft.client.resources.DefaultResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultResourcePack.class)
abstract class DefaultResourcePackMixin {
	@Redirect(
		method = "getInputStreamAssets",
		at = @At(value = "INVOKE", target = "Ljava/io/File;isFile()Z")
	)
	private boolean trustValidatedResourceIndex(File file) {
		return true;
	}
}
