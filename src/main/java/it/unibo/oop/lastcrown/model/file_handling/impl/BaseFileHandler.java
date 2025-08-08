package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * Base class providing common read‐from‐file logic for all handlers.
 *
 * @param <T> the type handled by this file handler.
 */
public class BaseFileHandler<T> {
    private static final Logger LOGGER = Logger.getLogger(BaseFileHandler.class.getName());
    private static final String SEP = File.separator;
    private final Parser<T> parser;
    private final String baseDirectory;

    /**
     * Initializes the parser and directory to use.
     * 
     * @param parser the parser to use
     * @param baseDirectory the directory to use
     */
    protected BaseFileHandler(final Parser<T> parser, final String baseDirectory) {
        this.parser = parser;
        this.baseDirectory = baseDirectory;
    }

    /**
     * Reads name+".txt" from baseDirectory, parses lines via Parser.
     * Logs and returns empty on I/O or parse errors.
     * 
     * @param name the name of the file
     * @return an optional of type T
     */
    protected Optional<T> read(final String name) {
        final File file = new File(baseDirectory, name + ".txt");
        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            System.out.println("Il file " + file.getAbsolutePath() + " esiste");
            final List<String> lines = new ArrayList<>();
            try (var reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    System.out.println("Aggiungo linea leggendo il file " + file.getAbsolutePath());
                    line = reader.readLine();
                }
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE,
                        "Error reading from file: " + file.getAbsolutePath(),
                        e);
                return Optional.empty();
            }
            try {
                return Optional.of(parser.parse(lines));
            } catch (final IllegalArgumentException e) {
                LOGGER.log(Level.WARNING,
                        "Error parsing file: " + file.getAbsolutePath(),
                        e);
                return Optional.empty();
            }            
        } else {        
            final String resourcePath = baseDirectory.replace(SEP, "/") + "/" + name + ".txt";
            try (InputStream is = getClass()
                .getResourceAsStream(resourcePath)) {
                if (is == null) {
                    System.out.println("Non ho trovato il file " + resourcePath);
                    return Optional.empty();
                }
                try (var reader = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    final List<String> lines = reader.lines().toList();
                    return Optional.of(parser.parse(lines));
                }
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE,
                        "Error reading resource: " + resourcePath, e);
                return Optional.empty();
            } catch (final IllegalArgumentException e) {
                LOGGER.log(Level.WARNING,
                        "Error parsing resource: " + resourcePath, e);
                return Optional.empty();
            }
        }
    }

    /**
     * Getter for the directory.
     * 
     * @return the base directory to use
     */
    protected String getBaseDirectory() {
        return baseDirectory;
    }
}
