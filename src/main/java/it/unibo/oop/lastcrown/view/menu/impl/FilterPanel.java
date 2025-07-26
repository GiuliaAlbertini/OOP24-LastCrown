package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JPanel;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * A filter bar panel for card type selection.
 */
public final class FilterPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String FONT_NAME = "SansSerif";
    private static final int BTN_FONT_SIZE = 16;
    static final int BUTTON_HEIGHT = 36;
    private static final CardType[] TYPES = {
        CardType.HERO, CardType.MELEE, CardType.RANGED, CardType.SPELL,
    };


    /**
     * Callback interface for filter selection.
     */
    @FunctionalInterface
    public interface FilterListener {
        void onFilter(Optional<CardType> type);
    }

    /**
     *  * Constructs a FilterPanel with given listener.
     * @param listener called when a filter button is pressed
     */
    public FilterPanel(final FilterListener listener) {
        super(new FlowLayout(FlowLayout.CENTER, BUTTON_HEIGHT / 4, 0));
        setMinimumSize(new Dimension(0, BUTTON_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_HEIGHT));

        addButton("ALL", Optional.empty(), listener);
        for (final CardType type : TYPES) {
            addButton(type.get().toUpperCase(), Optional.of(type), listener);
        }
    }

    private void addButton(final String label,
                           final Optional<CardType> type,
                           final FilterListener listener) {
        final JButton btn = new JButton(label);
        btn.setFont(new Font(FONT_NAME, Font.BOLD, BTN_FONT_SIZE));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, BUTTON_HEIGHT));
        btn.addActionListener(e -> listener.onFilter(type));
        add(btn);
    }
}
