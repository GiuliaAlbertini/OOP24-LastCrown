package it.unibo.oop.lastcrown.model.file_handling.api;

import java.util.List;

/**
 * Interface for parsing a list of strings into an object of type T.
 *
 * @param <T> the type of the object to be parsed.
 */
public interface Parser<T> {
    /**
     * Parses a list of strings into an object of type T.
     *
     * @param lines the file content as a list of strings.
     * @return the parsed object.
     */
    T parse(List<String> lines);
}
