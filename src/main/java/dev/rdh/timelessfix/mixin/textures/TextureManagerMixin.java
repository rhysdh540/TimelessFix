package dev.rdh.timelessfix.mixin.textures;

import dev.rdh.timelessfix.TextureManagerExtension;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.AbstractTexture;
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
abstract class TextureManagerMixin implements TextureManagerExtension {
	@Shadow @Final private Map<ResourceLocation, ITextureObject> mapTextureObjects;

	@Inject(method = "deleteTexture", at = @At("TAIL"))
	private void removeDeletedTexture(ResourceLocation location, CallbackInfo ci) {
		this.mapTextureObjects.remove(location);
	}

	@Override
	public void timelessfix$releaseTexture(ResourceLocation location) {
		ITextureObject texture = this.mapTextureObjects.remove(location);
		if (texture instanceof AbstractTexture) {
			((AbstractTexture) texture).deleteGlTexture();
		}
	}

	@Override
	public void timelessfix$releaseRemoteTextures() {
		Iterator<Map.Entry<ResourceLocation, ITextureObject>> textures = this.mapTextureObjects.entrySet().iterator();
		while (textures.hasNext()) {
			Map.Entry<ResourceLocation, ITextureObject> entry = textures.next();
			if (entry.getValue() instanceof ThreadDownloadImageData
				&& entry.getKey().getResourcePath().startsWith("skins/")) {
				((AbstractTexture) entry.getValue()).deleteGlTexture();
				textures.remove();
			}
		}
	}
}
