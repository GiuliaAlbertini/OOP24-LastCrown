package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.oop.lastcrown.model.file_handling.api.FileHandler;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.file_handling.api.Serializer;

/**
 * Full read/write handler: delegate reads to {@link BaseFileHandler}
 * and implement {@code writeToFile()} via the Serializer.
 * 
 * @param <T> the type of object this handler reads and writes
 */
public class FileHandlerImpl<T>
        extends BaseFileHandler<T>
        implements FileHandler<T> {

    private static final Logger LOGGER = Logger.getLogger(FileHandlerImpl.class.getName());
    private final Serializer<T> serializer;

    /**
     * Constructs a new {@link FileHandler}.
     * 
     * @param parser the parser to use
     * @param serializer the serializer to use
     * @param baseDirectory the directory to use
     */
    public FileHandlerImpl(final Parser<T> parser,
                           final Serializer<T> serializer,
                           final String baseDirectory) {
        super(parser, baseDirectory);
        this.serializer = serializer;
    }

    @Override
    public final Optional<T> readFromFile(final String fileName) {
        return read(fileName);
    }

    @Override
    public final void writeToFile(final String fileName, final T object) {
        final File file = new File(getBaseDirectory(), fileName + ".txt");
        final List<String> content = serializer.serialize(object);

        try (var writer = new BufferedWriter(
                 new OutputStreamWriter(
                     new FileOutputStream(file),
                     StandardCharsets.UTF_8))) {
            for (final var line : content) {
                writer.write(line);
                writer.newLine();
            }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE,
                       "Error writing to file: " + file.getAbsolutePath(),
                       e);
        }
    }
}
