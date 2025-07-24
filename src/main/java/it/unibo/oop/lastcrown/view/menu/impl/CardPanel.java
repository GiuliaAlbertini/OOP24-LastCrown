package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;

/**
 * Representation of a Card with its info.
 */
public final class CardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color BG_COLOR = new Color(60, 10, 10);
    private static final Color HEADER_COLOR = new Color(120, 30, 30);
    private static final Color LABEL_FG = Color.WHITE;
    private static final int LABEL_FONT_SIZE = 28;
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final String FONT_NAME = "SansSerif";
    private static final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

    private CardPanel(final CardIdentifier card) {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createBevelBorder(0));

        final CompleteCollection cc = new CompleteCollectionImpl();
        final String titleText = switch (card.type()) {
            case HERO -> cc.getHero(card).map(Hero::getName).orElse(card.number() + " (Hero)");
            case MELEE,
                 RANGED -> cc.getPlayableCharacter(card).map(PlayableCharacter::getName).orElse(card.number() + " (Character)");
            case SPELL -> cc.getSpell(card).map(Spell::getName).orElse(card.number() + " (Spell)");
            default -> card.number() + " (Unknown)";
        };

        final JLabel title = createLabel(titleText);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(true);
        title.setBackground(HEADER_COLOR);
        title.setForeground(LABEL_FG);
        add(title, BorderLayout.NORTH);

        final IconPanel iconPanel = new IconPanel(card, false);
        final JPanel iconContainer = new JPanel(new BorderLayout());
        iconContainer.setOpaque(true);
        iconContainer.setBackground(BG_COLOR);
        iconContainer.add(iconPanel, BorderLayout.CENTER);
        add(iconContainer, BorderLayout.CENTER);

        final JPanel info = new JPanel(new GridLayout(0, 1));
        info.setOpaque(true);
        info.setBackground(BG_COLOR);
        info.setForeground(LABEL_FG);
        add(info, BorderLayout.SOUTH);
        addCardInfo(card, info);
    }

    /**
     * Create a new CardPanel.
     * @param card the card to represent
     * @return the new CardPanel
     */
    public static CardPanel create(final CardIdentifier card) {
        return new CardPanel(card);
    }

    private void addCardInfo(final CardIdentifier card, final JPanel info) {
        final CompleteCollection cc = new CompleteCollectionImpl();
        switch (card.type()) {
            case HERO -> cc.getHero(card).ifPresent(h -> {
                info.add(createLabel("Attack: " + h.getAttackValue()));
                info.add(createLabel("Health: " + h.getHealthValue()));
                info.add(createLabel("Wall Attack: " + h.getWallAttack()));
                info.add(createLabel("Wall Health: " + h.getWallHealth()));
                info.add(createLabel("Melee slots: " + h.getMeleeCards()));
                info.add(createLabel("Ranged slots: " + h.getRangedCards()));
                info.add(createLabel("Spell slots: " + h.getSpellCards()));
                info.add(createLabel("Cost: " + h.getRequirement().amount()));
            });
            case MELEE,
                 RANGED -> cc.getPlayableCharacter(card).ifPresent(pc -> {
                info.add(createLabel("Range: " + pc.getActionRange()));
                info.add(createLabel("Attack: " + pc.getAttackValue()));
                info.add(createLabel("Health: " + pc.getHealthValue()));
                info.add(createLabel("Copies/round: " + pc.getCopiesPerMatch()));
                info.add(createLabel("Energy: " + pc.getEnergyToPlay()));
                info.add(createLabel("Cost: " + pc.getCost()));
            });
            case SPELL -> cc.getSpell(card).ifPresent(s -> {
                info.add(createLabel("Effect category: " + s.getSpellEffect().category()));
                info.add(createLabel("Target: " + s.getSpellEffect().target().get()));
                info.add(createLabel("Energy: " + s.getEnergyToPlay()));
                info.add(createLabel("Cost: " + s.getCost()));
            });
            default -> info.add(createLabel("Unknown type"));
        }
    }

    private JLabel createLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setFont(responsiveFont(Font.BOLD, LABEL_FONT_SIZE));
        lbl.setOpaque(true);
        lbl.setBackground(BG_COLOR);
        lbl.setForeground(LABEL_FG);
        return lbl;
    }

    private static Font responsiveFont(final int style, final int size) {
        final double scale = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        return new Font(FONT_NAME, style, (int) (size * scale));
    }
}
