package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Unique private boolean tf$renderingDebugCrosshair = false;

    @WrapMethod(method = "renderWorldDirections")
    private void tf$wrapDebugCrosshair(float partialTicks, Operation<Void> original) {
        this.tf$renderingDebugCrosshair = true;
        original.call(partialTicks);
        this.tf$renderingDebugCrosshair = false;
    }

    @ModifyExpressionValue(method = "orientCamera", at = @At(value = "CONSTANT", args = "floatValue=-0.1F"))
    private float tf$fixParallax(float original) {
        return this.tf$renderingDebugCrosshair ? original : 0.05F;
    }
}
