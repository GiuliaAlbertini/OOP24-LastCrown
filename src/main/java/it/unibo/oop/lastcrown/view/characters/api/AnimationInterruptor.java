package it.unibo.oop.lastcrown.view.characters.api;

/**
 * An interface that defines methods useful to avoid concurrent errors.
 */
public interface AnimationInterruptor {
      /**
     * Notify the current working thread that
     * it has to finish doing the animation loop.
     * If no thread currently posess the mutual exclusion it does anything.
     */
    void notifyDone();

    /**
     * Set the boolean flag done to false. The current working thread
     * will start doing the animation loop and it won't stop
     * until another thread calls the notifyDone() method.
     */
    void start();

    /**
     * Should be call by a method that posess the mutual exclusion.
     * Release the mutual exclusion and notifies the thread/s who are waiting.
     */
    void freeLock();

    /**
     * Tries to acquire the mutual exclusion, if it's not immediatly available, 
     * the current thread will start waiting until it can acquire the mutual exclusion.
     */
    void acquireLock();
}
