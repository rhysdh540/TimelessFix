package dev.rdh.timelessfix.mixin.client;

import dev.rdh.timelessfix.ParticleCulling;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
abstract class EntityRendererMixin {
	@Redirect(
		method = "renderWorldPass",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;renderEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V")
	)
	private void captureParticleCamera(RenderGlobal renderer, Entity entity, ICamera camera, float tickDelta) {
		ParticleCulling.camera = camera;
		renderer.renderEntities(entity, camera, tickDelta);
	}
}
