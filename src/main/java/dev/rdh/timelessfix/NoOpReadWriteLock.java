package dev.rdh.timelessfix;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public final class NoOpReadWriteLock implements ReadWriteLock {
	public static final NoOpReadWriteLock INSTANCE = new NoOpReadWriteLock();
	private static final Lock LOCK = new Lock() {
		@Override public void lock() {}
		@Override public void lockInterruptibly() {}
		@Override public boolean tryLock() { return true; }
		@Override public boolean tryLock(long time, TimeUnit unit) { return true; }
		@Override public void unlock() {}
		@Override public Condition newCondition() { throw new UnsupportedOperationException(); }
	};

	private NoOpReadWriteLock() {}

	@Override public Lock readLock() { return LOCK; }
	@Override public Lock writeLock() { return LOCK; }
}
