package dev.rdh.timelessfix;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public final class Xoshiro256StarStarRandom extends Random {
	private static final long SPLITMIX_GAMMA = 0x9E3779B97F4A7C15L;
	private static final AtomicLong UNIQUIFIER = new AtomicLong(System.nanoTime());

	private long s0;
	private long s1;
	private long s2;
	private long s3;

	public Xoshiro256StarStarRandom() {
		this(System.nanoTime() ^ nextUniquifier());
	}

	public Xoshiro256StarStarRandom(long seed) {
		super(seed);
	}

	private static long nextUniquifier() {
		return splitmix64(UNIQUIFIER.addAndGet(SPLITMIX_GAMMA));
	}

	private static long splitmix64(long z) {
		z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
		z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
		return z ^ (z >>> 31);
	}

	private static long rotl(long x, int k) {
		return (x << k) | (x >>> (64 - k));
	}

	@Override
	public void setSeed(long seed) {
		super.setSeed(seed);
		long z = seed + SPLITMIX_GAMMA;
		this.s0 = splitmix64(z);
		z += SPLITMIX_GAMMA;
		this.s1 = splitmix64(z);
		z += SPLITMIX_GAMMA;
		this.s2 = splitmix64(z);
		z += SPLITMIX_GAMMA;
		this.s3 = splitmix64(z);
	}

	@Override
	protected int next(int bits) {
		return (int) (nextLong() >>> (64 - bits));
	}

	@Override
	public long nextLong() {
		long result = rotl(this.s1 * 5, 7) * 9;
		long t = this.s1 << 17;
		this.s2 ^= this.s0;
		this.s3 ^= this.s1;
		this.s1 ^= this.s2;
		this.s0 ^= this.s3;
		this.s2 ^= t;
		this.s3 = rotl(this.s3, 45);
		return result;
	}

	@Override
	public int nextInt() {
		return (int) nextLong();
	}

	@Override
	public int nextInt(int bound) {
		if (bound <= 0) {
			throw new IllegalArgumentException("bound must be positive");
		}
		return (int) ((nextLong() & Long.MAX_VALUE) % bound);
	}

	@Override
	public boolean nextBoolean() {
		return (nextLong() & 1) != 0;
	}

	@Override
	public double nextDouble() {
		return (nextLong() >>> 11) * 0x1.0p-53;
	}

	@Override
	public float nextFloat() {
		return (nextLong() >>> 40) * 0x1.0p-24f;
	}

	@Override
	public void nextBytes(byte[] bytes) {
		int i = 0;
		while (i < bytes.length) {
			long random = nextLong();
			for (int n = Math.min(bytes.length - i, 8); n-- > 0; random >>>= 8) {
				bytes[i++] = (byte) random;
			}
		}
	}
}
