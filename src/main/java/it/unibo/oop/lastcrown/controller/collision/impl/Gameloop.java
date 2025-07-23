package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.oop.lastcrown.controller.collision.api.MainControllerExample;

/**
 * Game loop thread that updates the match controller at a fixed interval.
 * Responsible for continuously updating the game logic while the game is running.
 */
public final class Gameloop extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Gameloop.class.getName());
    private static final long PERIOD = 64;
    private final MainControllerExample controller;
    private boolean running; //NOPMD suppressed as it is a false positive due to multithreading.

    /**
     * Constructs a new game loop instance with the specified main controller.
     *
     * @param controller the main controller that provides access to game logic
     */
    public Gameloop(final MainControllerExample controller) {
        this.controller = controller;
        this.running = false;
    }

    /**
     * Starts the game loop. Continuously updates the match controller
     * and maintains a consistent update rate defined by the PERIOD.
     */
    @Override
    public void run() {
        this.running = true;
        long lastTime = System.currentTimeMillis();
        while (this.running) {
            final long current = System.currentTimeMillis();
            this.controller.getMatchController().update((int) (current - lastTime));
            waitForNextFrame(current);
            lastTime = current;
        }
    }

    /**
     * Waits until the next frame should be processed, based on the defined PERIOD.
     *
     * @param current the timestamp when the last frame was processed
     */
    private void waitForNextFrame(final long current) {
        final long dt = System.currentTimeMillis() - current;
        if (dt < PERIOD) {
            try {
                sleep(PERIOD - dt);
            } catch (final InterruptedException ex) {
                currentThread().interrupt(); // senza 'Thread.' davanti
                LOGGER.log(Level.WARNING, "Thread interrotto durante l'attesa", ex);
            }
        }
    }

    /**
     * Stops the game loop by setting the running flag to false and interrupting the thread.
     */
    @Override
    public void interrupt() {
        this.running = false;
        super.interrupt();
    }
}
