package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void tf$clearTitles(CallbackInfo ci) {
        GuiIngame ingameGUI = Minecraft.getMinecraft().ingameGUI;
        ingameGUI.displayTitle("", "", -1, -1, -1);
        ingameGUI.setDefaultTitlesTimes();
    }
}
