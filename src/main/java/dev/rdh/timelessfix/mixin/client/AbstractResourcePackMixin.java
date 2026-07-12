package dev.rdh.timelessfix.mixin.client;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractResourcePack.class)
abstract class AbstractResourcePackMixin {
	/**
	 * @author embeddedt
	 * @reason reduce method size & possibly allocation rate
	 */
	@Overwrite
	private static String locationToName(ResourceLocation location) {
		return "assets/" + location.getResourceDomain() + '/' + location.getResourcePath();
	}
}
