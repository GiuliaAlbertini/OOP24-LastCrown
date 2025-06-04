package it.unibo.oop.lastcrown.controller.app_managing.api;

/**
 * Provides operations to handle the main phases of the application.
 */
public interface MainController {

    /**
     * Closes the whole application.
     */
    void closeAll();

    /**
     * Sets the necessary aspects to go in the Menu section.
     * 
     * @param username the username representing the account to use
     */
    void goOverLogin(String username);
}
