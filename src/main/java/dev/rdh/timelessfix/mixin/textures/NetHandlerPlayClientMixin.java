package dev.rdh.timelessfix.mixin.textures;

import dev.rdh.timelessfix.SkinTextureCleaner;
import java.util.Map;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetHandlerPlayClient.class)
abstract class NetHandlerPlayClientMixin {
	@Redirect(
		method = "handlePlayerListItem",
		at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;")
	)
	private Object releaseRemovedPlayerSkin(Map<?, ?> players, Object id) {
		NetworkPlayerInfo removed = (NetworkPlayerInfo) players.remove(id);
		if (removed != null) {
			SkinTextureCleaner.releaseIfUnused(removed);
		}
		return removed;
	}
}
