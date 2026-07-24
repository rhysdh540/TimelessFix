package dev.rdh.timelessfix;

public final class EnchantmentReferenceCleaner {
	private static Clearable hurtIterator;
	private static Clearable damageIterator;

	private EnchantmentReferenceCleaner() {
	}

	public static void registerHurtIterator(Clearable iterator) {
		hurtIterator = iterator;
	}

	public static void registerDamageIterator(Clearable iterator) {
		damageIterator = iterator;
	}

	public static void clearHurtIterator() {
		hurtIterator.clearReferences();
	}

	public static void clearDamageIterator() {
		damageIterator.clearReferences();
	}

	public interface Clearable {
		void clearReferences();
	}
}
