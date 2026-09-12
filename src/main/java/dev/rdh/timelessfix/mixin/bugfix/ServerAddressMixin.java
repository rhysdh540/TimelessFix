package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.multiplayer.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerAddress.class)
public class ServerAddressMixin {
    @WrapMethod(method = "getIP")
    private String tf$preventCrash(Operation<String> original) {
        try {
            return original.call();
        } catch (Exception _) {
            return "";
        }
    }
}
