package dev.rdh.timelessfix.mixin.client;

import java.util.Map;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
abstract class TextureManagerMixin {
	@Shadow @Final private Map<ResourceLocation, ITextureObject> mapTextureObjects;

	@Inject(method = "deleteTexture", at = @At("TAIL"))
	private void removeDeletedTexture(ResourceLocation location, CallbackInfo ci) {
		this.mapTextureObjects.remove(location);
	}
}
