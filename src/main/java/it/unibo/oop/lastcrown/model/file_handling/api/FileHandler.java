package it.unibo.oop.lastcrown.model.file_handling.api;

import java.util.Optional;

/**
 * A generic file handler interface responsible for reading and writing objects of type {@code T}
 * to and from files. Each implementation should handle the file operations related to the specific type.
 *
 * @param <T> the type of objects handled by this file handler.
 */
public interface FileHandler<T> {
    /**
     * Reads an object of type T from the file named "fileName.txt" in the base directory.
     *
     * @param fileName the key used to build the file.
     * @return an Optional of the object read from the file, or an empty Optional if the file does not exist.
     */
    Optional<T> readFromFile(String fileName);
    /**
     * Writes an object of type T to the file named "fileName.txt" in the base directory.
     *
     * @param fileName the key used to build the file name.
     * @param object the object to write.
     */
    void writeToFile(String fileName, T object);
}
