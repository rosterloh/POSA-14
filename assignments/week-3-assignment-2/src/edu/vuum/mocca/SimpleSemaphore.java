package edu.vuum.mocca;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject (which is accessed via a Condition). It must
 *        implement both "Fair" and "NonFair" semaphore semantics,
 *        just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a Lock to protect the critical section.
     */
    // TODO - you fill in here
	private final ReentrantLock mLock;
	
    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	private final Condition noPermitsAvailable;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	private volatile int availablePermits;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
    	this.availablePermits = permits;
    	mLock = new ReentrantLock(fair);
    	noPermitsAvailable = mLock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
    	final ReentrantLock lock = this.mLock;
    	lock.lockInterruptibly();
    	
    	try {
    		while(availablePermits <= 0) {
    			noPermitsAvailable.await();
    		}
    		--availablePermits;    		
    	} finally {
    		lock.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
    	final ReentrantLock lock = this.mLock;
    	lock.lock();
    	
    	try {
    		while(availablePermits <= 0) {
    			noPermitsAvailable.awaitUninterruptibly();
    		}
    		--availablePermits;    		
    	} finally {
    		lock.unlock();
    	}
    }

    /**
     * Return one permit to the semaphore.
     */
    public void release() {
        // TODO - you fill in here.
    	final ReentrantLock lock = this.mLock;
    	lock.lock();
    	
    	try {
    		availablePermits++;
    		// Only signal if waiters can make progress.
    		if(availablePermits > 0)
    			noPermitsAvailable.signal();
    	} finally {
    		lock.unlock();
    	}
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        // return value.
    	
    	// No lock is needed as availablePermits is volatile.
        return availablePermits;
    }
}
