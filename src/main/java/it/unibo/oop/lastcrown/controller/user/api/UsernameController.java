package it.unibo.oop.lastcrown.controller.user.api;

/**
 * Defines methods to check the validity of a username.
 */
public interface UsernameController {
    /**
     * Checks if a username is valid.
     * 
     * @param username the username to check
     * @return {@code true} if it's valid, {@code false} otherwise
     */
    boolean checkValidity(String username);
    /**
     * Checks if a username has already been used.
     * 
     * @param username the username to check
     * @return {@code true} if it's been used, {@code false} otherwise
     */
    boolean checkExistance(String username);
}

