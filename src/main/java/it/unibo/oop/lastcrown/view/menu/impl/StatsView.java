package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.SceneName;

/**
 * The {@code StatsView} class represents the statistics scene of the application.
 */
public final class StatsView extends AbstractScene {

    private static final int BORDER_RIGHT = 20;
    private static final int BORDER_BOTTOM = 15;
    private static final int BORDER_LEFT = 20;
    private static final int BORDER_TOP = 15;
    private static final long serialVersionUID = 1L;
    private static final int VERTICAL_SPACING = 20;
    private static final String FONT_NAME = "DialogInput";
    private static final Font INFO_FONT  = getResponsiveFont(new Font(FONT_NAME, Font.PLAIN, 50));
    private static final Font VALUE_FONT = getResponsiveFont(new Font(FONT_NAME, Font.BOLD, 45));
    private static final Color STAT_COLOR      = new Color(100, 215, 0);
    private static final Color CARD_BACKGROUND = new Color(30, 30, 30);

    private StatsView(final SceneManager sceneManager, final AccountController accountController) {
        this.setLayout(new BorderLayout());

        final JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        this.add(centerPanel, BorderLayout.CENTER);

        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);

        final Account account = accountController.getAccount();
        statsPanel.add(createStatPanel("Coins",              String.valueOf(account.getCoins())));
        statsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        statsPanel.add(createStatPanel("Bosses Defeated",    String.valueOf(account.getBossesDefeated())));
        statsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        statsPanel.add(createStatPanel("Games Played",       String.valueOf(account.getPlayedMatches())));
        statsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        statsPanel.add(createStatPanel("Total Playtime (in minutes)",     String.format("%.2f", account.getPlaytime())));
        statsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        statsPanel.add(createStatPanel("Bosses/Match Ratio", String.format("%.2f", account.computeBossesPerMatch())));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor  = GridBagConstraints.CENTER;
        centerPanel.add(statsPanel, gbc);

        final BackButton backButton = BackButton.create(SceneName.STATS, SceneName.MENU, sceneManager);
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        southPanel.setOpaque(false);
        southPanel.add(backButton);
        this.add(southPanel, BorderLayout.SOUTH);

        this.setOpaque(false);
    }

    /**
     * Factory method to create a new {@code StatsView} instance.
     *
     * @param sceneManager the {@link SceneManager} to use
     * @param accountController the {@link AccountController} to use
     * @return the {@code StatsView} created
     */
    public static StatsView create(final SceneManager sceneManager,
                                   final AccountController accountController) {
        return new StatsView(sceneManager, accountController);
    }

    /**
     * Creates a panel containing a name label and a value label with distinct fonts,
     * styled to look like a card.
     *
     * @param name  the name of the statistic
     * @param value the value of the statistic
     * @return a {@link JPanel} with horizontally arranged labels
     */
    private JPanel createStatPanel(final String name, final String value) {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(new EmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT));

        final JLabel nameLabel  = new JLabel(name + ": ");
        nameLabel.setFont(INFO_FONT);
        nameLabel.setForeground(STAT_COLOR);

        final JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(VALUE_FONT);
        valueLabel.setForeground(Color.WHITE);

        panel.add(nameLabel);
        panel.add(valueLabel);
        return panel;
    }

    @Override
    public SceneName getSceneName() {
        return SceneName.STATS;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
