package it.unibo.oop.lastcrown.controller.user.impl;

import it.unibo.oop.lastcrown.controller.user.api.UsernameController;
import it.unibo.oop.lastcrown.model.user.api.UsernameValidator;
import it.unibo.oop.lastcrown.model.user.impl.UsernameValidatorImpl;

/**
 * Implementation of {@link UsernameController}.
 */
public class UsernameControllerImpl implements UsernameController {
    private final UsernameValidator validator = new UsernameValidatorImpl();

    @Override
    public final boolean checkValidity(final String username) {
        return this.validator.isNameValid(username);
    }

    @Override
    public final boolean checkExistance(final String username) {
        return !this.validator.isUsernameNew(username);
    }

}
