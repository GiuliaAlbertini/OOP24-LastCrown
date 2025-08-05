package it.unibo.oop.lastcrown.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Utility class that creates a document modal JDialog with a default CANCEL option
 * and the possibility to add more custom option with specific effects.
 */
public final class Dialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private final JPanel jbPanel;

    /**
     * @param title the title of the JDialog
     * @param message the message to be written in the JDialog
     * @param choice true to consider multiple possible options, false otherwise
     */
    public Dialog(final String title, final String message, final boolean choice) {
        this.setTitle(title);
        this.setSize(WIDTH, HEIGHT);
        this.setFocusableWindowState(false);
        this.setResizable(false);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        final JPanel content = new JPanel(new BorderLayout());

        final JTextArea area = new JTextArea(message);
        area.setFont(new Font("Arial", Font.BOLD, 24));
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setBorder(null);

        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);

        this.jbPanel = new JPanel(flowLayout);

        if (choice) {
            final JButton cancel = new JButton("CANCEL");
            cancel.addActionListener(act -> this.dispose());
            this.jbPanel.add(cancel);
        }

        content.add(area, BorderLayout.CENTER);
        content.add(jbPanel, BorderLayout.SOUTH);

        this.setContentPane(content);
    }

    /**
     * Adds a JButton to grant another closing option.
     * @param button the JButton to add
     */
    public void addButton(final JButton button) {
        this.jbPanel.add(button, FlowLayout.LEFT);
    }
}
