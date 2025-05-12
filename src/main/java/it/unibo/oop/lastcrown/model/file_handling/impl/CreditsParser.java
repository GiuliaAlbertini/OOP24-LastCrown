package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * A parser that converts a list of strings into a list of credits.
 * The expected format is that each line of the file is one credit entry.
 */
public class CreditsParser implements Parser<List<String>> {

    @Override
    public final List<String> parse(final List<String> lines) {
        return new ArrayList<>(lines);
    }
}
