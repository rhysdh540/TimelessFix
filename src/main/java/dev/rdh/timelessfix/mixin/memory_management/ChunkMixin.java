package dev.rdh.timelessfix.mixin.memory_management;

import dev.rdh.timelessfix.CompactableNibbleArray;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
abstract class ChunkMixin {
	@Shadow public abstract ExtendedBlockStorage[] getBlockStorageArray();

	@Redirect(
		method = "fillChunk",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/NibbleArray;getData()[B")
	)
	private byte[] getWritableLightData(NibbleArray light) {
		return ((CompactableNibbleArray)light).timelessfix$writableData();
	}

	@Inject(method = "fillChunk", at = @At("RETURN"))
	private void compactLightData(byte[] data, int mask, boolean fullChunk, CallbackInfo ci) {
		for (ExtendedBlockStorage section : this.getBlockStorageArray()) {
			if (section != null) {
				((CompactableNibbleArray)section.getBlocklightArray()).timelessfix$compact();
				if (section.getSkylightArray() != null) {
					((CompactableNibbleArray)section.getSkylightArray()).timelessfix$compact();
				}
			}
		}
	}
}
