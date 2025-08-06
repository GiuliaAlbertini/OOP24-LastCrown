package it.unibo.oop.lastcrown.utility;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

/**
 * Utility class for creating WindowListeners that handle dialog closing.
 */
public final class DialogCloseHandler {

    private DialogCloseHandler() {
        // Utility class - private constructor
    }

    /**
     * Creates a WindowListener that performs a specific action when the dialog is closed.
     *
     * @param dialog the dialog to close
     * @param onClose the action to perform before closing the dialog
     * @return a configured WindowListener
     */
    public static WindowAdapter createCloseListener(final JDialog dialog, final Runnable onClose) {
        return new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (onClose != null) {
                    onClose.run();
                }
                dialog.dispose();
            }
        };
    }

    /**
     * Creates a WindowListener that simply closes the dialog without additional actions.
     *
     * @param dialog the dialog to close
     * @return a configured WindowListener
     */
    public static WindowAdapter createSimpleCloseListener(final JDialog dialog) {
        return createCloseListener(dialog, null);
    }
}