package it.unibo.oop.lastcrown.view.characters;

import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 * A custom JProgressBar that provides graphic improvements and a flashing system
 * when the percentage level is lower that the limit.
 */
public final class CharacterHealthBar extends JProgressBar {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CharacterHealthBar.class.getName());
    private static final int HEIGHT_DIVISOR = 20;
    private static final int LIMIT = 30;
    private static final int TIME = 100;
    private static final int BORDER_THICKNESS = 1;
    private volatile int percentage;
    private Color originalColor;

    private CharacterHealthBar() { }
    /**
     * Creates a new instance of CharacterHealthBar.
     * @param width the horizontal size of this health bar
     * @param height the vertical size of this health bar
     * @param color the color of this health bar
     * @return new CharacterHealthBar
     */
    public static CharacterHealthBar create(final int width, final int height, final Color color) {
        final CharacterHealthBar instance = new CharacterHealthBar();
        instance.init(width, height, color);
        return instance;
    }

    private void init(final int width, final int height, final Color color) {
        this.originalColor = color;
        this.setOpaque(false);
        this.setMinimum(0);
        this.setMaximum(100);
        this.setValue(100);
        this.setForeground(this.originalColor);
        final Border customBorder = BorderFactory.createLineBorder(color, BORDER_THICKNESS);
        this.setBorder(customBorder);
        this.setSize(width, height / HEIGHT_DIVISOR);
    }

    /**
     * Set this health bar new percentage.
     * @param newPercentage new percentage of this health bar
     */
    public void setPercentage(final int newPercentage) {
        this.percentage = newPercentage;
        this.setValue(this.percentage);
        if (0 < this.percentage && this.percentage <= LIMIT) {
            new Thread(this::startFlashing).start();
        }
    }

    /**
     * Begins to flash alternating the brighter and the darker version
     * of this health bar original color. When the percentage becomes 
     * greater that the limit, the health bar stops flashing.
     */
    private void startFlashing() {
        while (this.percentage <= LIMIT) {
            this.darkerColor();
            this.brighterColor();
        }
        this.setForeground(this.originalColor);
    }

    /**
     * Set this health bar color the brighter version of its original.
     */
    private void brighterColor() {
        this.setForeground(this.getForeground().brighter());
            try {
                Thread.sleep(TIME);
            } catch (final InterruptedException e) {
                LOG.fine("Error during brighterColor() method");
            }
        this.repaint();
    }

    /**
     * Set this health bar color the darker version of its original.
     */
    private void darkerColor() {
        this.setForeground(this.getForeground().darker());
            try {
                Thread.sleep(TIME);
            } catch (final InterruptedException e) {
                LOG.fine("Error during darkerColor() method");
            }
        this.repaint();
    }
}

