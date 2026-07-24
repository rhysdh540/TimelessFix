package dev.rdh.timelessfix.mixin.textures;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractClientPlayer.class)
public interface AbstractClientPlayerAccessor {
	@Accessor("playerInfo")
	NetworkPlayerInfo timelessfix$getCachedPlayerInfo();
}
