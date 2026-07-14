package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
abstract class EntityCullingMixin {
	@Inject(method = "shouldRender", at = @At(value = "NEW", target = "(DDDDDD)Lnet/minecraft/util/AxisAlignedBB;"), cancellable = true)
	private void a(Entity entity, ICamera camera, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
		if(camera instanceof Frustum f) {
			cir.setReturnValue(
					entity.isInRangeToRender3d(cameraX, cameraY, cameraZ)
							&& (entity.ignoreFrustumCheck
							|| f.isBoxInFrustum(
							entity.posX - 2.0,
							entity.posY - 2.0,
							entity.posZ - 2.0,
							entity.posX + 2.0,
							entity.posY + 2.0,
							entity.posZ + 2.0
					))
			);
		}
	}
}
