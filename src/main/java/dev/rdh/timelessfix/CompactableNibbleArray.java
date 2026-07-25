package dev.rdh.timelessfix;

public interface CompactableNibbleArray {
	byte[] timelessfix$writableData();

	void timelessfix$compact();
}
