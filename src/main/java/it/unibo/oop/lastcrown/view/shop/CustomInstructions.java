package it.unibo.oop.lastcrown.view.shop;

import java.awt.Font;

import javax.swing.JTextArea;

/**
 * A simple class that extends JTextArea and contains the command instructions for the shop.
 */
public final class CustomInstructions extends JTextArea {
    private static final long serialVersionUID = 1L;
    private static final String INSTRUCTIONS = "Press and hold RIGHT to make the hero run to the right\n"
     + "Press and hold LEFT to make the hero run to the left\n"
     + "Press UP to talk to the Traders \n"
     + "Whenever you want, press SPACE to start a new battle";
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
