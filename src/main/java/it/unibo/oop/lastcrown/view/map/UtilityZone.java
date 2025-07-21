package it.unibo.oop.lastcrown.view.map;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.GameController;
import it.unibo.oop.lastcrown.view.Dialog;

/**
 * A Zone of the map that contains utilities of the match.
 */
public final class UtilityZone extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH_DIVISOR = 20;
    /**
     * @param obs the container exit observer
     * @param gameContr the game controller interface
     * @param width the width of the utility zone
     * @param height the height of the utility zone
     * @param wallHealthBar the wall health bar
     * @param eventWriter the graphic component that containes the event messages
     * @param coinsWriter the graphic component that contains the number of coins
     */
    public UtilityZone(final MatchExitObserver obs, final GameController gameContr, final int width, final int height,
     final JComponent wallHealthBar, final JComponent eventWriter, final JComponent coinsWriter) {
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);
        this.setLayout(flowLayout);
        this.setPreferredSize(new Dimension(width, height));
        final JLabel label = new JLabel("Wall Health");
        label.setPreferredSize(new Dimension(width / WIDTH_DIVISOR, height / 3));
        this.add(eventWriter);
        this.add(coinsWriter);
        this.add(label);
        this.add(wallHealthBar);

        final String title = "Pausing...";
        final String message = "Select an option: \n"
        + "CLOSE: close the pause menu\n"
        + "EXIT: exit the match and return to the menu\n"
        + "INSTRUCTIONS: check the instructions";
        final Dialog pauseDialog = new Dialog(title, message, false);
        pauseDialog.setLocationRelativeTo(this);

        final String insTitle = "Instructions";
        final String insMessage = "Select a card from your deck to the left\n"
        + "And play it in the highlighted zone\n"
        + "Choose carefully your moves in order to be prepared "
        + "when the BOSS comes";
        final JButton instructions = new JButton("INSTRUCTIONS");
        instructions.addActionListener(act -> {
            final Dialog instructionsDialog = new Dialog(insTitle, insMessage, true);
            instructionsDialog.setVisible(true);
            instructionsDialog.setLocationRelativeTo(pauseDialog);
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
            gameContr.notifyPause();
            pauseDialog.setVisible(true);
        });

        this.add(pause);
        pause.setPreferredSize(new Dimension(width / WIDTH_DIVISOR, height));
        pause.setBounds(width - width / WIDTH_DIVISOR, 0, pause.getPreferredSize().width,
         pause.getPreferredSize().height);
    }
}
