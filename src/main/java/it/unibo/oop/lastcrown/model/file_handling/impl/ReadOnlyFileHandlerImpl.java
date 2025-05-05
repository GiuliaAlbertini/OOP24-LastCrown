package it.unibo.oop.lastcrown.model.file_handling.impl;

import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * A read-only file handler for reading objects of type T using a Parser.
 *
 * @param <T> the type of object to read.
 */
public class ReadOnlyFileHandlerImpl<T> {
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
}
