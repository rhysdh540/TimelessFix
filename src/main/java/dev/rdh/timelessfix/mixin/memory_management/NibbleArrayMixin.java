package dev.rdh.timelessfix.mixin.memory_management;

import dev.rdh.timelessfix.CompactableNibbleArray;
import java.util.Arrays;
import net.minecraft.world.chunk.NibbleArray;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NibbleArray.class)
abstract class NibbleArrayMixin implements CompactableNibbleArray {
	@Unique private static final byte[] timelessfix$zero = new byte[2048];
	@Unique private static final byte[] timelessfix$full = timelessfix$full();

	@Shadow @Final @Mutable private byte[] data;

	@Inject(method = "<init>()V", at = @At("RETURN"))
	private void shareZeroData(CallbackInfo ci) {
		this.data = timelessfix$zero;
	}

	@Inject(method = "<init>([B)V", at = @At("RETURN"))
	private void compactInitialData(byte[] data, CallbackInfo ci) {
		this.timelessfix$compact();
	}

	@Inject(method = "setIndex", at = @At("HEAD"))
	private void prepareWrite(int index, int value, CallbackInfo ci) {
		this.timelessfix$writableData();
	}

	@Override
	@Unique
	public byte[] timelessfix$writableData() {
		if (this.data == timelessfix$zero || this.data == timelessfix$full) {
			this.data = this.data.clone();
		}
		return this.data;
	}

	@Override
	@Unique
	public void timelessfix$compact() {
		if (this.data != timelessfix$zero && Arrays.equals(this.data, timelessfix$zero)) {
			this.data = timelessfix$zero;
		} else if (this.data != timelessfix$full && Arrays.equals(this.data, timelessfix$full)) {
			this.data = timelessfix$full;
		}
	}

	@Unique
	private static byte[] timelessfix$full() {
		byte[] data = new byte[2048];
		Arrays.fill(data, (byte)-1);
		return data;
	}
}
