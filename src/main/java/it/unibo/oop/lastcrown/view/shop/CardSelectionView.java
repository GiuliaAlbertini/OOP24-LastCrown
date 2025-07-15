package it.unibo.oop.lastcrown.view.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.shop.impl.ShopCardsSelectionControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.menu.impl.CardPanel;

/**
 * Panel displaying a dynamic selection of cards side by side,
 * based on a card type provided at construction, each with a select button
 * that prompts a purchase confirmation dialog.
 */
public final class CardSelectionView extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int H_GAP = 10;
    private static final int V_GAP = 10;
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
     */
    public CardSelectionView(final int width, final int height, final int id, final CardType type,
     final ShopCardsSelectionControllerImpl shopController, final ContainerObserver obs) {
        this.setPreferredSize(new Dimension(width, height));
        this.id = id;
        this.obs = obs;
        final List<CardIdentifier> cards = shopController.getRandomCardsByType(type);
        final int columns = cards.size();
        final JPanel content = new JPanel(new GridLayout(1, columns, H_GAP, V_GAP));
        content.setPreferredSize(new Dimension(width, height));
        content.setOpaque(true);
        content.setBackground(Color.CYAN);
        content.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        for (final CardIdentifier card : cards) {
            content.add(this.createCardWithButton(card));
        }
        this.setContentPane(content);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                obs.notifyEndInteraction(Optional.empty(), id);
            }
        });
    }

    /**
     * Creates a panel combining the card view and a select button.
     * @param card the speicific CardIdentifier of the card shown
     * @return a new JPanel that contains the specified card
     */
    private JPanel createCardWithButton(final CardIdentifier card) {
        final JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(true);
        container.setBackground(Color.ORANGE);
        container.add(CardPanel.create(card), BorderLayout.CENTER);

        final JButton selectButton = new JButton("SELECT");
        selectButton.addActionListener(e -> {
            this.selectedCard = card;
            final int result = JOptionPane.showConfirmDialog(
                    this,
                    "You want to buy this card?",
                    "Purchase confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
                this.obs.notifyEndInteraction(Optional.of(this.selectedCard), this.id);
            }
        });

        container.add(selectButton, BorderLayout.SOUTH);
        container.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        selectButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 
        return container;
    }

    /**
     * @return the CardIdentifier of the specific selected card
     */
    public CardIdentifier getSelectedCard() {
        return selectedCard;
    }
}
