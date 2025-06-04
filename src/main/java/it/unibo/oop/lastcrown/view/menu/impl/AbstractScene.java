package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.view.menu.api.Scene;

/**
 * Abstract base class for scenes in the application.
 * Provides common functionality for scene components such as making components transparent
 * and adjusting fonts responsively based on the screen size.
 */
public abstract class AbstractScene extends JPanel implements Scene {
    private static final long serialVersionUID = 1L;
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
    static final int SCREEN_WIDTH = (int) SCREENSIZE.getWidth();
    static final int SCREEN_HEIGHT = (int) SCREENSIZE.getHeight();

    /**
     * Recursively sets all JComponents within the given container to be non-opaque.
     *
     * @param container the container whose components should be made transparent
     */
    protected static void makeComponentsTransparent(final Container container) {
        if (container instanceof JComponent) {
            ((JComponent) container).setOpaque(false);
        }
        for (final Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setOpaque(false);
            }
            if (component instanceof Container) {
                makeComponentsTransparent((Container) component);
            }
        }
    }

    /**
     * Returns a Font instance adjusted to be responsive based on the screen width.
     * The font size is scaled by comparing the current screen width to a fixed base screen width.
     *
     * @param baseFont the base font to be scaled
     * @return a new Font instance with a size adjusted based on the screen width
     */
    protected static Font getResponsiveFont(final Font baseFont) {
        final double scaleFactor = ((double) SCREEN_WIDTH) / BASE_SCREEN_WIDTH;
        final int newSize = (int) (baseFont.getSize() * scaleFactor);
        return new Font(baseFont.getName(), baseFont.getStyle(), newSize);
    }
}
