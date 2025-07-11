package it.unibo.oop.lastcrown.view.characters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 *  A class that provides a mutual exclusion system based on ReentrantLock and Condition.
 */
public class CustomLock {
    private static final Logger LOG = Logger.getLogger(CharacterHealthBar.class.getName());
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean forceLock;
    private Thread currentThread;

    /**
     * Initialises CustomLock.
     */
    public CustomLock() {
        this.forceLock = false;
        this.currentThread = null;
    }
    /**
     * To work properly must be called by a thread.
     * Try to acquire the mutual exclusion; if it is not
     * available immediatly, the thread waits until it becomes available.
     */
    public void acquireLock() {
        this.lock.lock();
        try {
            while (this.forceLock && !this.currentThread.equals(Thread.currentThread())) {
                condition.await();
            }
            this.forceLock = true;
            this.currentThread = Thread.currentThread();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.fine("Error occurred when a thread tries to acquire the lock");
        } finally {
            lock.unlock();
        }
    }

    /**
     * To work properly must be call by a thread that currently posses
     * the mutual eclusion. Release the mutual exclusion and notify
     * the threads who are waiting.
     */
    public void releaseLock() {
        lock.lock();
        try {
            if (this.currentThread.equals(Thread.currentThread())) {
                this.forceLock = false;
                this.currentThread = null;
                this.condition.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }
}
