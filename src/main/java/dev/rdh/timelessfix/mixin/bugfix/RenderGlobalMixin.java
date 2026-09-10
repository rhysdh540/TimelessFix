package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {
    @ModifyExpressionValue(method = "setupTerrain", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;boundingBox:Lnet/minecraft/util/AxisAlignedBB;", opcode = Opcodes.GETFIELD, ordinal = 0))
    private AxisAlignedBB tf$renderEntitiesOutsideWorld(AxisAlignedBB original, @Local(ordinal = 0) BlockPos blockPos) {
        return original.addCoord(0.0D, blockPos.getY() > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, 0.0D);
    }
}
