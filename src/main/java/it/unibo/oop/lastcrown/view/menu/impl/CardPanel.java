package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;

public final class CardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color BG_COLOR      = new Color(60, 10, 10);
    private static final Color HEADER_COLOR  = new Color(120, 30, 30);
    private static final Color LABEL_FG      = Color.WHITE;
    private static final int LABEL_FONT_SIZE = 28;
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final String FONT_NAME      = "SansSerif";
    private static final int SCREEN_WIDTH      = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

    private CardPanel(final CardIdentifier card) {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createBevelBorder(0));

        final CompleteCollection cc = new CompleteCollectionImpl();
        String titleText = switch (card.type()) {
            case HERO -> cc.getHero(card).map(h -> h.getName()).orElse(card.number() + " (Hero)");
            case MELEE,
                 RANGED -> cc.getPlayableCharacter(card).map(pc -> pc.getName()).orElse(card.number() + " (Character)");
            case SPELL -> cc.getSpell(card).map(s -> s.getName()).orElse(card.number() + " (Spell)");
            default -> card.number() + " (Unknown)";
        };

        JLabel title = createLabel(titleText);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(true);
        title.setBackground(HEADER_COLOR);
        title.setForeground(LABEL_FG);
        add(title, BorderLayout.NORTH);

        IconPanel iconPanel = new IconPanel(card, false);
        JPanel iconContainer = new JPanel(new BorderLayout());
        iconContainer.setOpaque(true);
        iconContainer.setBackground(BG_COLOR);
        iconContainer.add(iconPanel, BorderLayout.CENTER);
        add(iconContainer, BorderLayout.CENTER);

        JPanel info = new JPanel(new GridLayout(0, 1));
        info.setOpaque(true);
        info.setBackground(BG_COLOR);
        info.setForeground(LABEL_FG);
        add(info, BorderLayout.SOUTH);
        addCardInfo(card, info);
    }

    public static CardPanel create(final CardIdentifier card) {
        return new CardPanel(card);
    }

    private void addCardInfo(final CardIdentifier card, final JPanel info) {
        final CompleteCollection cc = new CompleteCollectionImpl();
        switch (card.type()) {
            case HERO -> cc.getHero(card).ifPresent(h -> {
                info.add(createLabel("Attack: "      + h.getAttackValue()));
                info.add(createLabel("Health: "      + h.getHealthValue()));
                info.add(createLabel("Wall Attack: " + h.getWallAttack()));
                info.add(createLabel("Wall Health: " + h.getWallHealth()));
                info.add(createLabel("Melee slots: " + h.getMeleeCards()));
                info.add(createLabel("Ranged slots: "+ h.getRangedCards()));
                info.add(createLabel("Spell slots: " + h.getSpellCards()));
            });
            case MELEE,
                 RANGED -> cc.getPlayableCharacter(card).ifPresent(pc -> {
                info.add(createLabel("Range: "        + pc.getActionRange()));
                info.add(createLabel("Attack: "       + pc.getAttackValue()));
                info.add(createLabel("Health: "       + pc.getHealthValue()));
                info.add(createLabel("Copies/round: "+ pc.getCopiesPerMatch()));
                info.add(createLabel("Energy: "       + pc.getEnergyToPlay()));
                info.add(createLabel("Cost: "         + pc.getCost()));
            });
            case SPELL -> cc.getSpell(card).ifPresent(s -> {
                info.add(createLabel("Effect category: " + s.getSpellEffect().category()));
                info.add(createLabel("Target: "          + s.getSpellEffect().target().get()));
                info.add(createLabel("Energy: "          + s.getEnergyToPlay()));
                info.add(createLabel("Cost: "            + s.getCost()));
            });
            default -> info.add(createLabel("Unknown type"));
        }
    }

    private JLabel createLabel(final String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(responsiveFont(Font.BOLD, LABEL_FONT_SIZE));
        lbl.setOpaque(true);
        lbl.setBackground(BG_COLOR);
        lbl.setForeground(LABEL_FG);
        return lbl;
    }

    private static Font responsiveFont(final int style, final int size) {
        double scale = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        return new Font(FONT_NAME, style, (int) (size * scale));
    }
}
