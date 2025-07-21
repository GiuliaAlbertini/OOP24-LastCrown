package it.unibo.oop.lastcrown.model.user.impl;

import java.io.File;
import java.util.regex.Pattern;

import it.unibo.oop.lastcrown.model.file_handling.impl.AccountParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.AccountSerializer;
import it.unibo.oop.lastcrown.model.file_handling.impl.FileHandlerImpl;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.model.user.api.UsernameValidator;

/**
 * Implementation of {@link UsernameValidator}.
 */
public class UsernameValidatorImpl implements UsernameValidator {
    private static final String SEP = File.separator;
    private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final String PATH = getPath();
    private final FileHandlerImpl<Account> fileHandler;

    /**
     * Constructs a new {@link UsernameValidatorImpl}. It sets the {@link FileHandler} to use.
     */
    public UsernameValidatorImpl() {
        this.fileHandler =
            new FileHandlerImpl<>(new AccountParser(), new AccountSerializer(), PATH);
    }

    @Override
    public final boolean isNameValid(final String username) {
        return VALID_USERNAME_PATTERN.matcher(username).matches()
           && username.trim().chars().noneMatch(Character::isWhitespace);
    }

    @Override
    public final boolean isUsernameNew(final String username) {
        return this.fileHandler.readFromFile(username).isEmpty();
    }

    private static String getPath() {
        return "OOP24-LastCrown"
             + SEP + "src"
             + SEP + "main"
             + SEP + "resources"
             + SEP + "accounts";
    }
}
