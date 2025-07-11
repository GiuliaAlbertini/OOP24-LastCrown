package it.unibo.oop.lastcrown.model.file_handling.api;

/**
 * A generic file handler interface responsible for reading and writing objects of type T
 * to and from files. Read capability is inherited from {@link ReadOnlyFileHandler}.
 *
 * @param <T> the type of objects handled by this file handler.
 */
public interface FileHandler<T> extends ReadOnlyFileHandler<T> {
    /**
     * Writes an object of type T to the file named "fileName.txt" in the base directory.
     *
     * @param fileName the key used to build the file name.
     * @param object   the object to write.
     */
    void writeToFile(String fileName, T object);
}
