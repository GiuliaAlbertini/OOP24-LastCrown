package it.unibo.oop.lastcrown.controller.file_handling.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.file_handling.api.CreditsController;
import it.unibo.oop.lastcrown.model.file_handling.api.ReadOnlyFileHandler;
import it.unibo.oop.lastcrown.model.file_handling.impl.CreditsParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.ReadOnlyFileHandlerImpl;

/**
 * Implementation of {@link CreditsController} for retrieving credits data.
 */
public class CreditsControllerImpl implements CreditsController {

    private static final String CREDITS = "credits";

    private static final String PATH = getPath();

    private final ReadOnlyFileHandler<List<String>> creditsFileHandler;

    /**
     * Constructs a CreditsControllerImpl with a read-only file handler configured to read the credits file.
     */
    public CreditsControllerImpl() {
        this.creditsFileHandler = new ReadOnlyFileHandlerImpl<>(new CreditsParser(), PATH);
    }

    @Override
    public final List<String> getCreditsList() {
        final Optional<List<String>> result = this.creditsFileHandler.readFromFile(CREDITS);
        return result.orElseThrow(() -> new IllegalStateException("Credits file not found or is empty"));
    }

    private static String getPath() {
        return "src"
        + File.separator
        + "main"
        + File.separator
        + "resources"
        + File.separator
        + CREDITS;
    }
}
