package dev.rdh.timelessfix.mixin.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
// Per-string batching adapted from PolyPatcher's FontRendererHook.
abstract class FontRendererMixin {
	@Unique private boolean timelessFix$batching;
	@Unique private boolean timelessFix$drawing;
	@Unique private ResourceLocation timelessFix$texture;
	@Unique private final float[] timelessFix$vertices = new float[20];
	@Unique private float timelessFix$u;
	@Unique private float timelessFix$v;
	@Unique private int timelessFix$vertexCount;
	@Shadow private float renderChar(char character, boolean italic) { return 0.0F; }

	@Inject(method = "renderStringAtPos", at = @At("HEAD"))
	private void beginStringBatch(String text, boolean shadow, CallbackInfo ci) {
		this.timelessFix$batching = true;
	}

	@Inject(method = "renderStringAtPos", at = @At("RETURN"))
	private void endStringBatch(String text, boolean shadow, CallbackInfo ci) {
		this.timelessFix$endBatch();
		this.timelessFix$batching = false;
	}

	@Redirect(
		method = {"renderDefaultChar", "loadGlyphTexture"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")
	)
	private void batchTexture(TextureManager manager, ResourceLocation texture) {
		if (!this.timelessFix$batching) {
			manager.bindTexture(texture);
			return;
		}
		if (this.timelessFix$drawing && texture.equals(this.timelessFix$texture)) return;
		this.timelessFix$endBatch();
		manager.bindTexture(texture);
		this.timelessFix$texture = texture;
		this.timelessFix$drawing = true;
		GL11.glBegin(GL11.GL_TRIANGLES);
	}

	@Redirect(
		method = {"renderDefaultChar", "renderUnicodeChar"},
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBegin(I)V")
	)
	private void skipGlyphBegin(int mode) {
		if (!this.timelessFix$batching) GL11.glBegin(mode);
	}

	@Redirect(
		method = {"renderDefaultChar", "renderUnicodeChar"},
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnd()V")
	)
	private void skipGlyphEnd() {
		if (!this.timelessFix$batching) GL11.glEnd();
	}

	@Redirect(
		method = {"renderDefaultChar", "renderUnicodeChar"},
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTexCoord2f(FF)V")
	)
	private void captureTextureCoordinate(float u, float v) {
		if (!this.timelessFix$batching) {
			GL11.glTexCoord2f(u, v);
			return;
		}
		this.timelessFix$u = u;
		this.timelessFix$v = v;
	}

	@Redirect(
		method = {"renderDefaultChar", "renderUnicodeChar"},
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glVertex3f(FFF)V")
	)
	private void captureVertex(float x, float y, float z) {
		if (!this.timelessFix$batching) {
			GL11.glVertex3f(x, y, z);
			return;
		}
		int offset = this.timelessFix$vertexCount++ * 5;
		this.timelessFix$vertices[offset] = x;
		this.timelessFix$vertices[offset + 1] = y;
		this.timelessFix$vertices[offset + 2] = z;
		this.timelessFix$vertices[offset + 3] = this.timelessFix$u;
		this.timelessFix$vertices[offset + 4] = this.timelessFix$v;
		if (this.timelessFix$vertexCount == 4) {
			this.timelessFix$emitVertex(0);
			this.timelessFix$emitVertex(1);
			this.timelessFix$emitVertex(2);
			this.timelessFix$emitVertex(2);
			this.timelessFix$emitVertex(1);
			this.timelessFix$emitVertex(3);
			this.timelessFix$vertexCount = 0;
		}
	}

	@Redirect(
		method = "renderStringAtPos",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderChar(CZ)F")
	)
	private float flushBeforeCustomGlyph(FontRenderer renderer, char character, boolean italic) {
		if (character == '\u011e') this.timelessFix$endBatch();
		return this.renderChar(character, italic);
	}

	@Redirect(
		method = "renderStringAtPos",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableTexture2D()V")
	)
	private void flushBeforeUntexturedGeometry() {
		this.timelessFix$endBatch();
		GlStateManager.disableTexture2D();
	}

	@Unique
	private void timelessFix$endBatch() {
		if (!this.timelessFix$drawing) return;
		GL11.glEnd();
		this.timelessFix$drawing = false;
		this.timelessFix$vertexCount = 0;
	}

	@Unique
	private void timelessFix$emitVertex(int index) {
		int offset = index * 5;
		GL11.glTexCoord2f(this.timelessFix$vertices[offset + 3], this.timelessFix$vertices[offset + 4]);
		GL11.glVertex3f(
			this.timelessFix$vertices[offset],
			this.timelessFix$vertices[offset + 1],
			this.timelessFix$vertices[offset + 2]
		);
	}
}
