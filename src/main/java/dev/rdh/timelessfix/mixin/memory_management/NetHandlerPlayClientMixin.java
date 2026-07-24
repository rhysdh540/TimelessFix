package dev.rdh.timelessfix.mixin.memory_management;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetHandlerPlayClient.class)
abstract class NetHandlerPlayClientMixin {
	@WrapMethod(method = "handleCustomPayload")
	private void releaseCustomPayload(S3FPacketCustomPayload packet, Operation<Void> original) {
		boolean clientThread = Minecraft.getMinecraft().isCallingFromMinecraftThread();
		try {
			original.call(packet);
		} finally {
			PacketBuffer data = packet.getBufferData();
			if (clientThread && data != null && data.refCnt() > 0) {
				data.release();
			}
		}
	}
}
