package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.file_handling.api.ReadOnlyFileHandler;

/**
 * Read‐only handler: reuses {@link BaseFileHandler} for all reads.
 * 
 * @param <T> the type of object this handler reads and writes
 */
public class ReadOnlyFileHandlerImpl<T> extends BaseFileHandler<T> implements ReadOnlyFileHandler<T> {
    /**
     * Constructs a new {@code ReadOnlyFileHandler}.
     * 
     * @param parser the parser to use
     * @param baseDirectory the directory to use
     */
    public ReadOnlyFileHandlerImpl(final Parser<T> parser,
                                   final String baseDirectory) {
        super(parser, baseDirectory);
    }

    @Override
    public final Optional<T> readFromFile(final String fileName) {
        return read(fileName);
    }
}
