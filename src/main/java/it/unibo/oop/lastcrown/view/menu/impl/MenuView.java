package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;

/**
 * MenuView represents the menu scene of the application.
 */
public final class MenuView extends AbstractScene {
    private static final String NAME_PLAY = "PLAY";
    private static final String NAME_DECK = "DECK";
    private static final String NAME_COLLECTION = "COLLECTION";
    private static final String NAME_STATS = "STATS";
    private static final String NAME_CREDITS = "CREDITS";
    private static final String NAME_EXIT = "EXIT";
    private static final long serialVersionUID = 1L;
    private static final double SPACING_FACTOR = 0.05;
    private static final double WEIGHT_Y = 0.9;
    private static final String PANEL_NAME = "MENU";
    private static final Color BTN_FG_COLOR = new Color(15, 35, 65);
    private static final Font BTN_FONT = getResponsiveFont(new Font("DialogInput", Font.BOLD, 30));
    private static final Color BTN_BG_COLOR = new Color(255, 215, 0);
    private static final int BTN_HEIGHT = (int) (SCREEN_HEIGHT * 0.075);
    private static final int BTN_WIDTH = (int) (SCREEN_WIDTH * 0.2);

    private final transient SceneManager sceneManager; 
    private final transient MainController mainContoller;

    /**
     * Constructs a new MenuView and sets up its layout and components.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param mainController the {@link MainController} to use
     */
    private MenuView(final SceneManager sceneManager, final MainController mainController) {
        this.sceneManager = sceneManager;
        this.mainContoller = mainController;
        this.setLayout(new GridBagLayout());

        final JButton playBtn = createButton(NAME_PLAY);
        final JButton deckBtn = createButton(NAME_DECK);
        final JButton collectionBtn = createButton(NAME_COLLECTION);
        final JButton statsBtn = createButton(NAME_STATS);
        final JButton creditsBtn = createButton(NAME_CREDITS);
        final JButton exitBtn = createButton(NAME_EXIT);

        final ActionListener buttonListener = this::handleButtonClick;
        playBtn.addActionListener(buttonListener);
        deckBtn.addActionListener(buttonListener);
        collectionBtn.addActionListener(buttonListener);
        statsBtn.addActionListener(buttonListener);
        creditsBtn.addActionListener(buttonListener);
        exitBtn.addActionListener(buttonListener);
        setUpButtonsPositions(playBtn, deckBtn, collectionBtn, statsBtn, creditsBtn, exitBtn);
    }

    private void setUpButtonsPositions(final JButton playBtn, final JButton deckBtn,
            final JButton collectionBtn, final JButton statsBtn, final JButton creditsBtn, final JButton exitBtn) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(
            (int) (SCREEN_HEIGHT * SPACING_FACTOR),
            (int) (SCREEN_WIDTH * SPACING_FACTOR),
            (int) (SCREEN_HEIGHT * SPACING_FACTOR),
            (int) (SCREEN_WIDTH * SPACING_FACTOR)
        );
        gbc.weighty = WEIGHT_Y;
        gbc.gridy = 0;
        this.add(Box.createVerticalGlue(), gbc);
        gbc.gridy = 1;
        this.add(playBtn, gbc);
        gbc.gridx++;
        this.add(deckBtn, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(collectionBtn, gbc);
        gbc.gridx++;
        this.add(statsBtn, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(creditsBtn, gbc);
        gbc.gridx++;
        this.add(exitBtn, gbc);

        gbc.weighty = 1;
        gbc.gridy++;
        this.add(Box.createVerticalGlue(), gbc);
    }

    /**
     * Factory method to create an instance of a MenuView.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param mainController the {@link MainController} to use
     * @return the created instance of MenuView
     */
    public static MenuView create(final SceneManager sceneManager, final MainController mainController) {
        final MenuView menu = new MenuView(sceneManager, mainController);
        menu.setOpaque(false);
        return menu;
    }

    private void handleButtonClick(final ActionEvent e) {
        final JButton sourceBtn = (JButton) e.getSource();
        switch (sourceBtn.getText()) {
            case NAME_PLAY:
            //////
                break;
            case NAME_DECK:
                this.sceneManager.switchScene(NAME_DECK);
                break;
            case NAME_COLLECTION:
                this.sceneManager.switchScene(NAME_COLLECTION);
                break;
            case NAME_STATS:
                this.sceneManager.switchScene(NAME_STATS);
                break;
            case NAME_CREDITS:
                this.sceneManager.switchScene(NAME_CREDITS);
                break;
            case NAME_EXIT:
                final int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    this.mainContoller.closeAll();
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected button: " + sourceBtn.getText());
        }
    }

    private JButton createButton(final String name) {
        final JButton btn = new JButton(name);
        final Dimension size = new Dimension(BTN_WIDTH, BTN_HEIGHT);
        btn.setPreferredSize(size);
        btn.setBackground(BTN_BG_COLOR);
        btn.setFont(BTN_FONT);
        btn.setForeground(BTN_FG_COLOR);
        btn.setBorder(BorderFactory.createBevelBorder(0));
        return btn;
    }

    @Override
    public String getSceneName() {
        return PANEL_NAME;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}
