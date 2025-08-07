package it.unibo.oop.lastcrown.view.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.utility.DialogCloseHandler;
import it.unibo.oop.lastcrown.view.Dialog;

/**
 * A Zone of the map that contains utilities of the match.
 */
public final class UtilityZone extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH_DIVISOR = 20;
    private final List<JComponent> components;
    private final Dialog pauseDialog;
    private final Dialog instructionDialog;
    /**
     * @param obs the container exit observer
     * @param gameContr the game controller interface
     * @param width the width of the utility zone
     * @param height the height of the utility zone
     * @param wallHealthBar the wall health bar
     * @param eventWriter the graphic component that containes the event messages
     * @param coinsWriter the graphic component that contains the number of coins
     */
    public UtilityZone(final MatchExitObserver obs, final MatchController gameContr, final int width, final int height,
     final JComponent wallHealthBar, final JComponent eventWriter, final JComponent coinsWriter) {
        this.components = new ArrayList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(this.getPreferredSize());

        eventWriter.setPreferredSize(new Dimension(width / 4, height));
        eventWriter.setMaximumSize(eventWriter.getPreferredSize());
        eventWriter.setBackground(Color.CYAN);
        this.components.addLast(eventWriter);

        final JLabel label = new JLabel("Wall Health");
        label.setPreferredSize(new Dimension(width / WIDTH_DIVISOR, height));
        label.setMaximumSize(label.getPreferredSize());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        this.components.addLast(label);

        wallHealthBar.setMaximumSize(wallHealthBar.getPreferredSize());
        this.components.addLast(wallHealthBar);

        coinsWriter.setPreferredSize(new Dimension(width / WIDTH_DIVISOR, height));
        coinsWriter.setMaximumSize(coinsWriter.getPreferredSize());
        coinsWriter.setBackground(Color.ORANGE);
        this.components.addLast(coinsWriter);

        final String title = "Pausing...";
        final String message = "Select an option: \n"
        + "CLOSE: close the pause menu\n"
        + "EXIT: exit the match and return to the menu\n"
        + "INSTRUCTIONS: check the instructions";
        this.pauseDialog = new Dialog(title, message, false);
        this.pauseDialog.setLocationRelativeTo(this);
        this.pauseDialog.addWindowListener(DialogCloseHandler.createCloseListener(pauseDialog, gameContr::notifyPauseEnd));
        final String insTitle = "Instructions";
        final String insMessage = "Select a card from your deck to the left\n"
        + "And play it in the highlighted zone\n"
        + "Choose carefully your moves in order to be prepared "
        + "when the BOSS comes";
        this.instructionDialog = new Dialog(insTitle, insMessage, true);
        this.instructionDialog.setLocationRelativeTo(this);

        final JButton instructions = new JButton("INSTRUCTIONS");
        instructions.addActionListener(act -> {
            instructionDialog.setVisible(true);
        });

        final JButton exit = new JButton("EXIT");
        exit.addActionListener(act -> {
            pauseDialog.dispose();
            obs.notifyExitToMenu();
        });

        final JButton close = new JButton("CLOSE");
        close.addActionListener(act -> {
            pauseDialog.dispose();
            gameContr.notifyPauseEnd();
        });

        pauseDialog.addButton(instructions);
        pauseDialog.addButton(exit);
        pauseDialog.addButton(close);

        final JButton pause = new JButton("PAUSE");
        pause.addActionListener(act -> {
            gameContr.notifyPauseStart();
            pauseDialog.setVisible(true);
        });
        pause.setPreferredSize(new Dimension(width / WIDTH_DIVISOR, height));
        pause.setMaximumSize(pause.getPreferredSize());
        this.components.addLast(pause);

        for (final var component : this.components) {
            final JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.add(component, BorderLayout.CENTER);
            wrapper.setOpaque(false);
            this.add(wrapper);
        }
    }
}
