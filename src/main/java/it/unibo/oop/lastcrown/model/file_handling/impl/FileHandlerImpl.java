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

import it.unibo.oop.lastcrown.model.file_handling.api.FileHandler;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.file_handling.api.Serializer;

/**
 * A generic file handler for reading and writing objects of type T using a Parser and Serializer.
 *
 * @param <T> the type of object to read and write.
 */
public class FileHandlerImpl<T> implements FileHandler<T> {

    private static final Logger LOGGER = Logger.getLogger(FileHandlerImpl.class.getName());

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

    /**
     * Reads an object of type T from the file named "name.txt" in the base directory.
     *
     * @param name the key used to build the file name.
     * @return an Optional of the object read from the file, or an empty Optional if the file doesn't exist or an error occurs.
     */
    @Override
    public Optional<T> readFromFile(final String name) {
        final String fileName = name + ".txt";
        final File file = new File(baseDirectory, fileName);
        if (!file.exists()) {
            return Optional.empty();
        }
        final List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
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

    @Override
    public void writeToFile(final String fileName, final Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeToFile'");
    }

}
