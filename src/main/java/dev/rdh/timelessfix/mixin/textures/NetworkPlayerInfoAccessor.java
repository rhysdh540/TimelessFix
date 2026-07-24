package dev.rdh.timelessfix.mixin.textures;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetworkPlayerInfo.class)
public interface NetworkPlayerInfoAccessor {
	@Accessor("locationSkin")
	ResourceLocation timelessfix$getCachedSkin();

	@Accessor("locationCape")
	ResourceLocation timelessfix$getCachedCape();
}
