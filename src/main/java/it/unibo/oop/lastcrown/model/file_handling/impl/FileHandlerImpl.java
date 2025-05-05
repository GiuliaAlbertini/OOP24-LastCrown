package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.file_handling.api.FileHandler;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.file_handling.api.Serializer;

/**
 * A generic file handler for reading and writing objects of type T using a Parser and Serializer.
 *
 * @param <T> the type of object to read and write.
 */
public class FileHandlerImpl<T> implements FileHandler<T> {
    
    private final Parser<T> parser;
    private final Serializer<T> serializer;
    private final String baseDirectory;

    /**
     * Constructs a GenericFileHandlerImpl with the given parser, serializer, and base directory.
     *
     * @param parser the parser to convert file lines into an object.
     * @param serializer the serializer to convert an object into file lines.
     * @param baseDirectory the base directory where files are stored.
     */
    public FileHandlerImpl(final Parser<T> parser, final Serializer<T> serializer, final String baseDirectory) {
        this.parser = parser;
        this.serializer = serializer;
        this.baseDirectory = baseDirectory;
    }

    @Override
    public Optional<T> readFromFile(final String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readFromFile'");
    }

    @Override
    public void writeToFile(final String fileName, final Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeToFile'");
    }

}
