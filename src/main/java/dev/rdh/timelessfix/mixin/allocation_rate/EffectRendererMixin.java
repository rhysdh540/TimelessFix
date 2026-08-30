package dev.rdh.timelessfix.mixin.allocation_rate;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.client.particle.EffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectRenderer.class)
abstract class EffectRendererMixin {
	@SuppressWarnings("SuspiciousMethodCalls")
	@Redirect(
		method = "updateEffectAlphaLayer",
		at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z")
	)
	private boolean timelessFix$fastRemoveDead(List<?> particles, Collection<?> dead) {
		return !dead.isEmpty() && particles.removeAll(Set.copyOf(dead));
	}
}
