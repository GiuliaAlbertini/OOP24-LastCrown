package it.unibo.oop.lastcrown.model.file_handling.api;

import java.util.List;

/**
 * Interface for serializing an object of type T into a list of strings.
 *
 * @param <T> the type of the object to be serialized.
 */
public interface Serializer<T> {
    /**
     * Serializes an object of type T into a list of strings.
     *
     * @param object the object to serialize.
     * @return a list of strings representing the object.
     */
    List<String> serialize(T object);
}
