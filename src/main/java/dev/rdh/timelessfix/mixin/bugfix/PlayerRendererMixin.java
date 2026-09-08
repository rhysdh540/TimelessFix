package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class PlayerRendererMixin extends RendererLivingEntity<AbstractClientPlayer> {
    public PlayerRendererMixin(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    @Inject(method = {"renderRightArm", "renderLeftArm"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z", opcode = Opcodes.PUTFIELD))
    private void tf$disableRidingArm(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        super.getMainModel().isRiding = false;
    }
}
