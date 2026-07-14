package dev.rdh.timelessfix.mixin.resourcepacks;

import java.io.File;
import java.util.Map;
import net.minecraft.client.resources.ResourceIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ResourceIndex.class)
abstract class ResourceIndexMixin {
	@Redirect(
		method = "<init>(Ljava/io/File;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
	)
	private Object onlyIndexExistingFiles(Map<Object, Object> map, Object key, Object value) {
		return ((File) value).isFile() ? map.put(key, value) : null;
	}
}
