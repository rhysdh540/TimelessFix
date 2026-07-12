package dev.rdh.timelessfix.mixin.client;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResourceLocation.class)
interface ResourceLocationAccessor {
	@Mutable
	@Accessor("resourceDomain")
	void timelessFix$setResourceDomain(String domain);

	@Mutable
	@Accessor("resourcePath")
	void timelessFix$setResourcePath(String path);
}
