package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.file_handling.api.ReadOnlyFileHandler;

/**
 * A read-only file handler for reading objects of type T using a Parser.
 *
 * @param <T> the type of object to read.
 */
public class ReadOnlyFileHandlerImpl<T> implements ReadOnlyFileHandler<T> {

    private static final Logger LOGGER = Logger.getLogger(ReadOnlyFileHandlerImpl.class.getName());

    private final Parser<T> parser;
    private final String baseDirectory;

    /**
     * Constructs a {@link ReadOnlyFileHandlerImpl} with the given parser and base directory.
     *
     * @param parser the parser to convert file lines into an object.
     * @param baseDirectory the base directory where files are stored.
     */
    public ReadOnlyFileHandlerImpl(final Parser<T> parser, final String baseDirectory) {
        this.parser = parser;
        this.baseDirectory = baseDirectory;
    }

    @Override
    public final Optional<T> readFromFile(final String name) {
        final String fileName = name + ".txt";
        final File file = new File(baseDirectory, fileName);
        if (!file.exists()) {
            return Optional.empty();
        }
        final List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String line = reader.readLine();
                    while (line != null) {
                        lines.add(line);
                        line = reader.readLine();
                    }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading from file: " + file.getAbsolutePath(), e);
            return Optional.empty();
        }
        try {
            return Optional.of(parser.parse(lines));
        } catch (final IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Error parsing file: " + file.getAbsolutePath(), e);
            return Optional.empty();
        }
    }
}
