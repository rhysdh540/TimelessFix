package dev.rdh.timelessfix.mixin.allocation_rate;

import java.util.Locale;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FontRenderer.class)
abstract class FontRendererMixin {
	@Redirect(
		method = "renderStringAtPos",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;toLowerCase(Ljava/util/Locale;)Ljava/lang/String;")
	)
	private String skipWholeStringLowercase(String text, Locale locale) {
		return text;
	}

	@Redirect(
		method = "renderStringAtPos",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I")
	)
	private int findFormattingCode(String codes, int code) {
		return codes.indexOf(Character.toLowerCase((char)code));
	}
}
