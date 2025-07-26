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
    private final String ACCOUNT_PATH = getAccountPath();

    private final FileHandler<Account> fileHandler;
    private Account account;

    /**
     * Constructs an {@code AccountControllerImpl} and initializes the account file handler.
     * 
     * @param username the username of related to the account to use
     */
    public AccountControllerImpl(final String username) {
        this.fileHandler = new FileHandlerImpl<>(
            new AccountParser(),
            new AccountSerializer(),
            ACCOUNT_PATH
        );
        this.setAccount(loadOrCreateAccount(username));
    }

    @Override
    public final void setAccount(Account account) {
        this.account = defensiveCopy(account);
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

    private static String getAccountPath() {
        return System.getProperty("user.home")
                + SEP + ".lastcrown"
                + SEP + "accounts";
    }

    private static Account defensiveCopy(final Account src) {
        final Account copy = new AccountImpl(src.getUsername());
        copy.removeCoins(copy.getCoins());
        copy.addCoins(src.getCoins());
        copy.addPlaytime(src.getPlaytime());

        IntStream.range(0, src.getBossesDefeated())
                 .forEach(i -> copy.increaseBossesDefeated());

        IntStream.range(0, src.getPlayedMatches())
                 .forEach(i -> copy.increasePlayedMatches());

        src.getUserCollection()
           .getCollection()
           .stream()
           .forEach(copy::addCard);

        return copy;
    }
}
