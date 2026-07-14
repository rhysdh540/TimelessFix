package dev.rdh.timelessfix.mixin.client;

import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TextureUtil.class)
abstract class TextureUtilMixin {
	@ModifyConstant(method = "uploadTextureImageSubImpl", constant = @Constant(intValue = 4194304))
	private static int rightSizeUploadBuffer(int maximumPixels, BufferedImage image) {
		return (int) Math.min(maximumPixels, (long) image.getWidth() * image.getHeight());
	}
}
