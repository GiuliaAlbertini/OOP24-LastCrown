package it.unibo.oop.lastcrown.controller.file_handling.api;

import java.util.List;

/**
 *  Provides a method get a list of credits.
 */
public interface CreditsController {
    /**
     * Provides a list containing the credits.
     * 
     * @return the list containing the credits
     */
    List<String> getCreditsList();
}
