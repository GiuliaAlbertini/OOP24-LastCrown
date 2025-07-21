package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;

/**
 * Extension of {@link JPanel} representing a full card.
 */
public final class CardPanel extends JPanel {
    private static final int LABEL_FONT_SIZE = 28;

    private static final long serialVersionUID = 1L;

    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final String FONT_NAME = "SansSerif";
    private static final int SCREEN_WIDTH =
        java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

    private CardPanel(final CardIdentifier card) {
        super(new BorderLayout());
        setOpaque(false);
        this.setBackground(Color.CYAN);
        setBorder(BorderFactory.createBevelBorder(0));
        final JLabel title = createLabel(card.number() + " (" + card.type() + ")");
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, BorderLayout.NORTH);
        final IconPanel iconPanel = new IconPanel(card, false);
        final JPanel iconContainer = new JPanel(new BorderLayout());
        iconContainer.setOpaque(false);
        iconContainer.add(iconPanel, BorderLayout.CENTER);
        add(iconContainer, BorderLayout.CENTER);
        final JPanel info = new JPanel(new GridLayout(0, 1));
        info.setBackground(Color.CYAN);
        add(info, BorderLayout.SOUTH);
        addCardInfo(card, info);
    }

    /**
     * Factory method to create a new CardPanel.
     * 
     * @param card the card to represent
     * @return the CardPanel created
     */
    public static CardPanel create(final CardIdentifier card) {
        return new CardPanel(card);
    }

    private void addCardInfo(final CardIdentifier card, final JPanel info) {
        final CompleteCollection cc = new CompleteCollectionImpl();
        switch (card.type()) {
            case CardType.HERO -> cc.getHero(card).ifPresent(h -> {
                info.add(createLabel("Attack: "      + h.getAttackValue()));
                info.add(createLabel("Health: "      + h.getHealthValue()));
                info.add(createLabel("Wall Attack: " + h.getWallAttack()));
                info.add(createLabel("Wall Health: " + h.getWallHealth()));
                info.add(createLabel("Melee slots: " + h.getMeleeCards()));
                info.add(createLabel("Ranged slots: " + h.getRangedCards()));
                info.add(createLabel("Spell slots: " + h.getSpellCards()));
            });
            case CardType.MELEE, CardType.RANGED -> cc.getPlayableCharacter(card)
                .ifPresent(pc -> {
                    final PlayableCharacter p = pc;
                    info.add(createLabel("Name: "         + p.getName()));
                    info.add(createLabel("Range: "        + p.getActionRange()));
                    info.add(createLabel("Attack: "       + p.getAttackValue()));
                    info.add(createLabel("Health: "       + p.getHealthValue()));
                    info.add(createLabel("Copies/round: " + p.getCopiesPerMatch()));
                    info.add(createLabel("Energy: "       + p.getEnergyToPlay()));
                    info.add(createLabel("Cost: "         + p.getCost()));
                });
            case CardType.SPELL -> cc.getSpell(card).ifPresent(s -> {
                info.add(createLabel("Name: "   + s.getName()));
                info.add(createLabel("Effect category: " + s.getSpellEffect().category()));
                info.add(createLabel("Target: " + s.getSpellEffect().target().get()));
                info.add(createLabel("Energy: " + s.getEnergyToPlay()));
                info.add(createLabel("Cost: "   + s.getCost()));
            });
            default -> info.add(createLabel("Unknown type"));
        }
    }

    private JLabel createLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setFont(responsiveFont(Font.BOLD, LABEL_FONT_SIZE));
        return lbl;
    }

    private static Font responsiveFont(final int style, final int size) {
        final double scale = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        return new Font(FONT_NAME, style, (int) (size * scale));
    }
}
