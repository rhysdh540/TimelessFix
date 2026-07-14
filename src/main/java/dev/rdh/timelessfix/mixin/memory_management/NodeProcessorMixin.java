package dev.rdh.timelessfix.mixin.memory_management;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NodeProcessor.class)
abstract class NodeProcessorMixin {
	@Shadow protected IBlockAccess blockaccess;

	@Inject(method = "postProcess", at = @At("HEAD"))
	private void releaseBlockAccess(CallbackInfo ci) {
		this.blockaccess = null;
	}
}
