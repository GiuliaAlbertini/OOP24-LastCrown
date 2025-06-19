package it.unibo.oop.lastcrown;

import it.unibo.oop.lastcrown.controller.impl.MainControllerImpl;

/**
 * The entry point for the LastCrown application.
 */
public final class App {

    /**
     * Private constructor to prevent instantiation.
     */
    private App() { }

    /**
     * Main method that starts the application.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args) {
        new MainControllerImpl();
    }
}
