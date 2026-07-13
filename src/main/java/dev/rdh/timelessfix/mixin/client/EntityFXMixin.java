package dev.rdh.timelessfix.mixin.client;

import dev.rdh.timelessfix.ParticleCullState;
import dev.rdh.timelessfix.ParticleCulling;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFX.class)
abstract class EntityFXMixin implements ParticleCullState {
	@Unique private boolean timelessFix$visible = true;

	@Inject(method = "onUpdate", at = @At("TAIL"))
	private void updateVisibility(CallbackInfo ci) {
		if (ParticleCulling.camera != null) {
			this.timelessFix$visible = ParticleCulling.camera.isBoundingBoxInFrustum(
				((EntityFX) (Object) this).getEntityBoundingBox()
			);
		}
	}

	@Override
	public boolean timelessFix$isVisible() {
		return this.timelessFix$visible;
	}
}
