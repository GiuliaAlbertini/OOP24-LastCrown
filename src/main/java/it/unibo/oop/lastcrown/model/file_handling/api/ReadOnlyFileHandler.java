package it.unibo.oop.lastcrown.model.file_handling.api;

import java.util.Optional;

/**
 * A read-only file handler interface for reading objects of type T.
 *
 * @param <T> the type of object to read.
 */
public interface ReadOnlyFileHandler<T> {
    /**
     * Reads an object of type T from the file named "fileName.txt" in the base directory.
     *
     * @param fileName the key used to build the file name.
     * @return an Optional of the object read from the file, or an empty Optional if the file does not exist.
     */
    Optional<T> readFromFile(String fileName);
}
