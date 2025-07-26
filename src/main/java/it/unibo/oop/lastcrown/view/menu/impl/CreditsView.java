package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import it.unibo.oop.lastcrown.controller.file_handling.api.CreditsController;
import it.unibo.oop.lastcrown.controller.file_handling.impl.CreditsControllerImpl;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.view.SceneName;

/**
 * Simple view that shows the list of credits.
 */
public final class CreditsView extends AbstractScene {
    private static final long serialVersionUID = 1L;
    private static final int VERTICAL_SPACING = 20;
    private static final String FONT_NAME_DIALOG_INPUT = "DialogInput";
    private static final Font LABEL_FONT = getResponsiveFont(new Font(FONT_NAME_DIALOG_INPUT, Font.BOLD, 30));
    private static final Color LABEL_COLOR = new Color(144, 238, 144);

    private final transient CreditsController creditsController = new CreditsControllerImpl();
    private final transient SceneManager sceneManager;

    private CreditsView(final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        setLayout(new BorderLayout());
    }

    /**
     * Factory method to create a CreditsView.
     *
     * @param sceneManager the SceneManager to use
     * @return the created CreditsView instance
     */
    public static CreditsView create(final SceneManager sceneManager) {
        final CreditsView view = new CreditsView(sceneManager);
        view.initializeUI();
        return view;
    }

    private void initializeUI() {
        final JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));

        final List<String> creditLines = this.creditsController.getCreditsList();
        creditsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        for (final String line : creditLines) {
            final JLabel label = createLabel(line);
            label.setBorder(new EmptyBorder(VERTICAL_SPACING, 0, VERTICAL_SPACING, 0));
            creditsPanel.add(label);
        }

        final JScrollPane scrollPane = new HideableScrollPane(creditsPanel);
        add(scrollPane, BorderLayout.CENTER);

        final JButton backButton = BackButton.create(SceneName.CREDITS, SceneName.MENU, sceneManager);
        final JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(backButton);
        add(southPanel, BorderLayout.SOUTH);

        setComponentsOpacity(backButton);
    }

    private JLabel createLabel(final String text) {
        final JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(LABEL_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setFocusable(false);
        return label;
    }

    private void setComponentsOpacity(final JButton backButton) {
        makeComponentsTransparent(this);
        backButton.setOpaque(true);
    }

    @Override
    public SceneName getSceneName() {
        return SceneName.CREDITS;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
