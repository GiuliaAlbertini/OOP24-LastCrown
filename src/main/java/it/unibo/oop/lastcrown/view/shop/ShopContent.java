package it.unibo.oop.lastcrown.view.shop;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * The main Panel of the shop JFrame.
 */
public final class ShopContent extends JPanel {
    private static final long serialVersionUID = 1L;
    private final ContainerObserver contObs;
    private final CustomInstructions instructions;
    private final JButton escape;
    private final JButton collection;
    private final JButton deck;
    private final JButton match;

    /**
     * @param contObs the observer of the JFrame of the shop
     * @param width the width of this panel
     * @param height the height of this panel
     */
    public ShopContent(final ContainerObserver contObs, final int width, final int height) {
        this.contObs = contObs;
        final int  buttonWidth = (int) (width * DimensionResolver.JBUTTON.width());
        final int buttonHeight = (int) (height * DimensionResolver.JBUTTON.height());
        this.escape = new JButton("ESCAPE");
        this.escape.setSize(buttonWidth, buttonHeight);
        this.escape.addActionListener(act -> this.contObs.notifyEscape());
        this.collection = new JButton("COLLECTION");
        this.collection.setSize(buttonWidth, buttonHeight);
        this.collection.addActionListener(act -> this.contObs.notifyCollection());
        this.deck = new JButton("DECK");
        this.deck.setSize(buttonWidth, buttonHeight);
        this.deck.addActionListener(act -> this.contObs.notifyDeck());
        this.match = new JButton("MATCH");
        this.match.setSize(buttonWidth, buttonHeight);
        this.match.addActionListener(act -> this.contObs.notifyMatch());
        this.setSize(width, height);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.instructions = new CustomInstructions((int) (width * DimensionResolver.INSTRUCTIONS.width()),
         (int) (height * DimensionResolver.INSTRUCTIONS.height()));
        this.add(instructions);
        this.instructions.setLocation(width / 10, height / 10);
        this.add(deck);
        this.add(collection);
        this.add(escape);
        this.add(match);
        final int betweenSpace = buttonWidth / 3;
        deck.setBounds(width / 2, height - buttonHeight * 2, buttonWidth, buttonHeight);
        collection.setBounds(width / 2 + buttonWidth + betweenSpace, height - buttonHeight * 2,
         buttonWidth, buttonHeight);
        escape.setBounds(width / 2 + (buttonWidth + betweenSpace) * 2, height - buttonHeight * 2, 
         buttonWidth, buttonHeight);
        match.setBounds(width / 2 + (buttonWidth + betweenSpace) * 3, height - buttonHeight * 2,
         buttonWidth, buttonHeight);
    }
}
