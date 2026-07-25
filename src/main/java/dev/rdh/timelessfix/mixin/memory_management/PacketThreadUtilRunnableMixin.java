package dev.rdh.timelessfix.mixin.memory_management;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.PacketThreadUtil$1")
abstract class PacketThreadUtilRunnableMixin {
	@Shadow(remap = false, aliases = "f_54564080") @Final private Packet<?> packet;

	@Inject(method = "run", at = @At("HEAD"), cancellable = true)
	private void dropPacketsWithoutWorld(CallbackInfo ci) {
		if (Minecraft.getMinecraft().theWorld == null && !(this.packet instanceof S01PacketJoinGame)) {
			ci.cancel();
		}
	}
}
