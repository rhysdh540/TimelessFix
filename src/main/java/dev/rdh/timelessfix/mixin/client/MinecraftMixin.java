package dev.rdh.timelessfix.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@Redirect(
		method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V")
	)
	private void skipWorldTransitionGc() {
	}
}
