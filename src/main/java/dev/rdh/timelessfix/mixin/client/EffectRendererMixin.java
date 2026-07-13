package dev.rdh.timelessfix.mixin.client;

import dev.rdh.timelessfix.ParticleCullState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectRenderer.class)
abstract class EffectRendererMixin {
	@Redirect(
		method = "renderParticles",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/EntityFX;renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V")
	)
	private void skipCulledParticle(EntityFX particle, WorldRenderer renderer, Entity camera, float tickDelta,
		float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (((ParticleCullState) particle).timelessFix$isVisible()) {
			particle.renderParticle(renderer, camera, tickDelta, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		}
	}
}
