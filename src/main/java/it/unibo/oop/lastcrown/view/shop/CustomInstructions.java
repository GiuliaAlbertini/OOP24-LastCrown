package it.unibo.oop.lastcrown.view.shop;

import java.awt.Font;

import javax.swing.JTextArea;

/**
 * A simple class that extends JTextArea and contains the command instructions for the shop.
 */
public final class CustomInstructions extends JTextArea {
    private static final long serialVersionUID = 1L;
    private static final String INSTRUCTIONS = "Click on the different buttons in order to open your COLLECTION, \n"
     + "ESCAPE from the MATCH or to start a new one.\n"
     + "Click on a TRADER to start a shopping session \n"
     + "BE CAREFUL: once you buy a card by a trader, that specific trader\n"
     + "will be unavailable until the next time you reach the shop.\n"
     + "CHOOSE CAREFULLY your new cards";
     private static final int TEXT_SIZE = 20;

    /**
     * @param width the width of the instructions
     * @param height the height of the instructions
     */
    public CustomInstructions(final int width, final int height) {
        this.setSize(width, height);
        this.setText(INSTRUCTIONS);
        this.setFont(new Font("Limelight", Font.LAYOUT_LEFT_TO_RIGHT, TEXT_SIZE));
        this.setEditable(false);
        this.setFocusable(false);
        this.setOpaque(false);
        this.setVisible(true);
    }

}
