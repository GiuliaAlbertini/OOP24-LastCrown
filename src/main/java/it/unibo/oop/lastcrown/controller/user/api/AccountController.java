package it.unibo.oop.lastcrown.controller.user.api;

import it.unibo.oop.lastcrown.model.user.api.Account;

/**
 *  Provides operations to validate and manage user account creation.
 */
public interface AccountController {

    /**
     * Getter for the account controlled.
     * 
     * @return the account controlled by the controller
     */
    Account getAccount();
}
