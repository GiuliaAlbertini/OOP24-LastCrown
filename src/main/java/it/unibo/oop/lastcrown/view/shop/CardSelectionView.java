package it.unibo.oop.lastcrown.view.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.oop.lastcrown.controller.shop.impl.ShopCardsSelectionControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.scenes_utilities.CardPanel;

/**
 * Panel displaying a dynamic selection of cards side by side,
 * based on a card type provided at construction, each with a select button
 * that prompts a purchase confirmation dialog.
 */
public final class CardSelectionView extends JFrame {
    private static final Font LABEL_FONT = getResponsiveFont(Font.PLAIN, 24);
    private static final Font TITLE_FONT = getResponsiveFont(Font.BOLD, 28);
    private static final int MAX_CARDS = 4;
    private static final long serialVersionUID = 1L;
    private static final int H_GAP = 10;
    private static final int V_GAP = 10;
    private static final Color HEADER_BG = new Color(40, 20, 40);
    private static final Color BG_COLOR = new Color(15, 35, 65);
    private final int id;
    private final ContainerObserver obs;
    private transient CardIdentifier selectedCard;

    /**
     * Create a selection view for cards of the given type.
     * @param width the horizontal size of this shopping view
     * @param height the vertical size of this shopping view 
     * @param id the id of the associated trader
     * @param type the CardType to display (HERO, MELEE/RANGED as friendly, SPELL)
     * @param shopController the controller providing the card list
     * @param obs the container observer
     * @param currentCoins 
     */
    public CardSelectionView(final int width, final int height, final int id, final CardType type,
     final ShopCardsSelectionControllerImpl shopController, final ContainerObserver obs, final int currentCoins) {
        this.setPreferredSize(new Dimension(width, height));
        this.setResizable(false);
        this.id = id;
        this.obs = obs;
        final JPanel header = initHeader(type, currentCoins);
        this.add(header, BorderLayout.NORTH);

        final List<CardIdentifier> cards = shopController.getRandomCardsByType(type);
        final int maxCols = MAX_CARDS;
        final JPanel content = initContent(width, height, cards, maxCols);
        this.add(content, BorderLayout.CENTER);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                obs.notifyEndInteraction(Optional.empty(), id);
            }
        });
    }

    private JPanel initContent(final int width, final int height, final List<CardIdentifier> cards,
            final int maxCols) {
        final JPanel content = new JPanel(new GridLayout(1, maxCols, H_GAP, V_GAP));
        content.setPreferredSize(new Dimension(width, height));
        content.setOpaque(true);
        content.setBackground(BG_COLOR);
        for (final CardIdentifier card : cards) {
            content.add(this.createCardWithButton(card));
        }
        final int placeholders = maxCols - cards.size();
        for (int i = 0; i < placeholders; i++) {
            final JPanel empty = new JPanel();
            empty.setOpaque(false);
            content.add(empty);
        }
        return content;
    }

    private JPanel initHeader(final CardType type, final int currentCoins) {
        final JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        final JLabel title = new JLabel(type.name().toUpperCase(Locale.ROOT) + "'s shop", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(Color.YELLOW);
        header.add(title, BorderLayout.CENTER);
        final JLabel coinsLabel = new JLabel("You have " + currentCoins + " coins", SwingConstants.RIGHT);
        coinsLabel.setFont(LABEL_FONT);
        coinsLabel.setForeground(Color.YELLOW);
        header.add(coinsLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel createCardWithButton(final CardIdentifier card) {
        final JPanel panel = CardPanel.create(card);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                selectedCard = card;
                final int result = JOptionPane.showConfirmDialog(
                        panel,
                        "You want to buy this card?",
                        "Purchase confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    obs.notifyEndInteraction(Optional.of(selectedCard), id);
                }
            }
        });
        return panel;
    }

    /**
     * Getter for the current selected card.
     * 
     * @return the card identifier of the selected card
     */
    public CardIdentifier getSelectedCard() {
        return this.selectedCard;
    }

    private static Font getResponsiveFont(final int style, final int size) {
        final int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
        final double scale = screenW / 1920.0;
        return new Font("SansSerif", style, (int) (size * scale));
    }
}
