package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenBook.class)
public class GuiScreenBookMixin extends GuiScreen {
    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void tf$drawBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawWorldBackground(0);
    }
}
