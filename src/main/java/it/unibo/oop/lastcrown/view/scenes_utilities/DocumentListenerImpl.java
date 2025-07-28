package it.unibo.oop.lastcrown.view.scenes_utilities;

import java.awt.Color;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import it.unibo.oop.lastcrown.controller.user.api.UsernameController;
import it.unibo.oop.lastcrown.controller.user.impl.UsernameControllerImpl;

/**
 * Listens to changes in a username of a {@link JTextField} and validates
 * the current text checking the application rules to provide an instant visual feedback.
 */
public class DocumentListenerImpl implements DocumentListener {
    private final JTextField usernameField;
    private final UsernameController usernameController = new UsernameControllerImpl();

    /**
     * Binds this listener to the given text field.
     *
     * @param usernameField the {@link JTextField} to monitor.
     * @throws NullPointerException if {@code usernameField} is null.
     */
    public DocumentListenerImpl(final JTextField usernameField) {
        this.usernameField = Objects.requireNonNull(usernameField, "username must not be null!");
    }

    @Override
    public final void insertUpdate(final DocumentEvent e) {
        validateUsername();
    }

    @Override
    public final void removeUpdate(final DocumentEvent e) {
        validateUsername();
    }

    @Override
    public final void changedUpdate(final DocumentEvent e) {
        validateUsername();
    }

    private void validateUsername() {
        final String username = this.usernameField.getText().trim().toLowerCase(Locale.ROOT);
        if (usernameController.checkValidity(username)) {
            usernameField.setBackground(Color.GREEN);
        } else {
            usernameField.setBackground(Color.RED);
        }
    }
}
