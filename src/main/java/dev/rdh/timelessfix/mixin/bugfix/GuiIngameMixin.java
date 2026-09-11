package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @WrapWithCondition(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderPumpkinOverlay(Lnet/minecraft/client/gui/ScaledResolution;)V"))
    private boolean tf$removeOverlayInSpectator(GuiIngame instance, ScaledResolution scaledRes) {
        return !Minecraft.getMinecraft().thePlayer.isSpectator();
    }
}
