package dev.rdh.timelessfix.mixin.memory_management;

import dev.rdh.timelessfix.ClassInfoManager;
import dev.rdh.timelessfix.TextureManagerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@Shadow public WorldClient theWorld;
	@Shadow public EntityRenderer entityRenderer;
	@Shadow private TextureManager renderEngine;

	@Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
	private void clearMapRenderers(WorldClient world, String message, CallbackInfo ci) {
		if (world != this.theWorld && this.entityRenderer != null) {
			this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
			((TextureManagerExtension) this.renderEngine).timelessfix$releaseRemoteTextures();
		}
	}

	@Inject(method = "startGame", at = @At("RETURN"))
	private void clearMixinMetadata(CallbackInfo ci) {
		ClassInfoManager.clear();
	}

	@Redirect(
		method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V")
	)
	private void skipWorldTransitionGc() {
	}
}
