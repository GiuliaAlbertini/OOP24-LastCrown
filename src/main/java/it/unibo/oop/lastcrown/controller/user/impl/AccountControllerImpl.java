package it.unibo.oop.lastcrown.controller.user.impl;


import java.io.File;
import java.util.stream.IntStream;

import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.model.file_handling.api.FileHandler;
import it.unibo.oop.lastcrown.model.file_handling.impl.AccountParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.AccountSerializer;
import it.unibo.oop.lastcrown.model.file_handling.impl.FileHandlerImpl;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.model.user.impl.AccountImpl;

/**
 * Controller for an object of class {@link Account}.
 */
public class AccountControllerImpl implements AccountController {
    private static final String SEP = File.separator;
    private static final String PATH = getPath();

    private final FileHandler<Account> fileHandler;
    private final Account account;

    /**
     * Constructs an {@code AccountControllerImpl} and initializes the account file handler.
     * 
     * @param username the username of related to the account to use
     */
    public AccountControllerImpl(final String username) {
        this.fileHandler = new FileHandlerImpl<>(new AccountParser(), new AccountSerializer(), PATH);
        this.account = loadOrCreateAccount(username);
    }

    @Override
    public final Account getAccount() {
        return defensiveCopy(this.account);
    }

    private Account loadOrCreateAccount(final String username) {
        return this.fileHandler
            .readFromFile(username)
            .orElseGet(() -> {
                final Account fresh = new AccountImpl(username);
                this.fileHandler.writeToFile(username, fresh);
                return fresh;
            });
    }

    private static String getPath() {
        return "OOP24-LastCrown" + SEP
             + "src" + SEP
             + "main" + SEP
             + "resources" + SEP
             + "accounts";
    }

    private static Account defensiveCopy(final Account src) {
        final Account copy = new AccountImpl(src.getUsername());
        copy.addCoins(src.getCoins());
        copy.addPlaytime(src.getPlaytime());

        IntStream.range(0, src.getBossesDefeated())
                 .forEach(i -> copy.increaseBossesDefeated());

        IntStream.range(0, src.getPlayedGames())
                 .forEach(i -> copy.increasePlayedGames());

        src.getUserCollection()
           .getCollection()
           .stream()
           .forEach(copy::addCard);

        return copy;
    }
}
