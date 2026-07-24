package dev.rdh.timelessfix.mixin.textures;

import dev.rdh.timelessfix.SkinTextureCleaner;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldClient.class)
abstract class WorldClientMixin {
	@Inject(method = "removeEntityFromWorld", at = @At("RETURN"))
	private void releaseRemovedPlayerSkin(int id, CallbackInfoReturnable<Entity> cir) {
		if (cir.getReturnValue() instanceof AbstractClientPlayer) {
			NetworkPlayerInfo info = ((AbstractClientPlayerAccessor) cir.getReturnValue()).timelessfix$getCachedPlayerInfo();
			if (info != null) {
				SkinTextureCleaner.releaseIfUnused(info);
			}
		}
	}
}
