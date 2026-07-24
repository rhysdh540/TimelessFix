package dev.rdh.timelessfix;

import net.minecraft.util.ResourceLocation;

public interface TextureManagerExtension {
	void timelessfix$releaseTexture(ResourceLocation location);

	void timelessfix$releaseRemoteTextures();
}
