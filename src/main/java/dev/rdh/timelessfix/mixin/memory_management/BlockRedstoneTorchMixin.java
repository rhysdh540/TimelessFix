package dev.rdh.timelessfix.mixin.memory_management;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRedstoneTorch.class)
abstract class BlockRedstoneTorchMixin {
	@Shadow @Mutable private static Map<World, List<?>> toggles;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void timelessFix$weakenTogglesMap(CallbackInfo ci) {
		toggles = new WeakHashMap<>();
	}
}
