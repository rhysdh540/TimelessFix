package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty);
    @Unique private int tf$range = 17;

    @Inject(method = "checkLightFor", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 0))
    private void tf$updateRange(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.tf$range = this.isAreaLoaded(pos, 18, false) ? 17 : 15;
    }

    @ModifyExpressionValue(method  = "checkLightFor", at = @At(value = "CONSTANT", args = "intValue=17", ordinal = 0))
    private int tf$setStaticRange(int original) {
        return 16;
    }

    @ModifyExpressionValue(method  = "checkLightFor", at = {@At(value = "CONSTANT", args = "intValue=17", ordinal = 1), @At(value = "CONSTANT", args = "intValue=17", ordinal = 2)})
    private int setVariableRange(int original) {
        return this.tf$range;
    }
}
