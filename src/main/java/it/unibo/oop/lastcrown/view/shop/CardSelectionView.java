package it.unibo.oop.lastcrown.view.shop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.oop.lastcrown.controller.shop.impl.ShopCardsSelectionControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.menu.impl.CardPanel;

/**
 * Panel displaying a dynamic selection of cards side by side,
 * based on a card type provided at construction, each with a select button
 * that prompts a purchase confirmation dialog.
 */
public class CardSelectionView extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int H_GAP = 10;
    private static final int V_GAP = 10;
    private static final int PADDING = 10;

    private CardIdentifier selectedCard;

    /**
     * Constructs a selection view for cards of the given type.
     *
     * @param type the CardType to display (HERO, MELEE/RANGED as friendly, SPELL)
     * @param shopController the controller providing the card list
     */
    public CardSelectionView(final CardType type,
                             final ShopCardsSelectionControllerImpl shopController) {
        this.selectedCard = null;

        final List<CardIdentifier> cards = shopController.getRandomCardsByType(type);
        final int columns = cards.size();

        setLayout(new GridLayout(1, columns, H_GAP, V_GAP));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        setOpaque(false);

        for (final CardIdentifier card : cards) {
            add(createCardWithButton(card));
        }
    }

    /**
     * Creates a panel combining the card view and a select button.
     */
    private JPanel createCardWithButton(final CardIdentifier card) {
        final JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        container.add(CardPanel.create(card), BorderLayout.CENTER);

        final JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            this.selectedCard = card;
            final int result = JOptionPane.showConfirmDialog(
                    this,
                    "You want to buy this card?",
                    "Purchase confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            final boolean confirmed = result == JOptionPane.YES_OPTION;
            System.out.println(confirmed ? "Yes" : "No");
            final Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });
        container.add(selectButton, BorderLayout.SOUTH);

        return container;
    }

    public CardIdentifier getSelectedCard() {
        return selectedCard;
    }
}
