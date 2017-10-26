package org.usfirst.frc.team6038.framework.threading;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeString {
	private volatile String value;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	public String getValue() {
		try {
			lock.readLock().lock();
			return value;
		} finally {
			lock.readLock().unlock();
		}

	}

	public void setValue(String newValue) {
		try {
			lock.writeLock().lock();
			value = newValue;
		} finally {
			lock.writeLock().unlock();
		}
	}
}