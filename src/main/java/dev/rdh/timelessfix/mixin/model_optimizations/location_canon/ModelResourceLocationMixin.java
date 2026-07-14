package dev.rdh.timelessfix.mixin.model_optimizations.location_canon;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelResourceLocation.class)
abstract class ModelResourceLocationMixin extends ResourceLocation {
	@Shadow @Final @Mutable private String variant;
	protected ModelResourceLocationMixin(int ignored, String... parts) {
		super(ignored, parts);
	}

	@Inject(method = "<init>(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)V", at = @At("TAIL"))
	private void reuseLocationStrings(ResourceLocation location, String ignored, CallbackInfo ci) {
		ResourceLocationAccessor self = (ResourceLocationAccessor) this;
		self.timelessFix$setResourceDomain(location.getResourceDomain());
		self.timelessFix$setResourcePath(location.getResourcePath());
		this.variant = this.variant.intern();
	}
}
