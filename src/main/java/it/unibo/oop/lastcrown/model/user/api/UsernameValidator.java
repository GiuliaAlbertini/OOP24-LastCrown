package it.unibo.oop.lastcrown.model.user.api;

/**
 * Provides methods to check if a username is correct.
 */
public interface UsernameValidator {
    /**
     * Validates whether the given username is properly formatted.
     * A valid username must not be null, must not be empty (ignoring spaces), and can only contain
     * alphanumeric characters and underscores.
     *
     * @param username the username to validate.
     * @return {@code true} if the username is valid. {@code false} otherwise.
     */
    boolean isNameValid(String username);

    /**
     * Checks if the given username is new, based on the absence of an associated account file.
     *
     * @param username the username to check for existence.
     * @return {@code true} if no account data is found for the username. {@code false} otherwise.
     */
    boolean isUsernameNew(String username);
}
