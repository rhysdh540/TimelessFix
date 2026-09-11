package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "displayGuiScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setIngameFocus()V"))
    private void tf$reapplyKeybinds(GuiScreen guiScreenIn, CallbackInfo ci) {
        for (KeyBinding keyBinding : KeyBinding.keybindArray) {
            int keyCode = keyBinding.getKeyCode();
            if (keyCode > 0 && keyCode < Keyboard.KEYBOARD_SIZE) {
                KeyBinding.setKeyBindState(keyCode, Keyboard.isKeyDown(keyCode));
            }
        }
    }
}
