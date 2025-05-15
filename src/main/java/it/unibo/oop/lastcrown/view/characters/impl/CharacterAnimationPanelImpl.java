package it.unibo.oop.lastcrown.view.characters.impl;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.characters.CharacterHealthBar;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAnimationPanel;

/**
 * A custom JPanel that contains the animation frames and the healthbar of a specific character.
 */
final class CharacterAnimationPanelImpl extends JPanel implements CharacterAnimationPanel {
    private static final long serialVersionUID = 1L;
    private static final int BAR_HEIGHT_DIVISOR = 9;
    private static final double BAR_WIDTH_RESIZE = 0.75;
    private String charType;
    private CharacterHealthBar healthBar;
    private transient Image currentImage;

    private CharacterAnimationPanelImpl() { }

    /**
     * Creates a new instance of CharacterAnimationPanel.
     * @param width the horizontal size of this animation panel
     * @param height the vertical size of this animation panel
     * @param charType the type of the linked character
     * @param color the color of the health bar associated with this panel
     * @return new CharacterAnimationPanel
     */
    public static CharacterAnimationPanelImpl create(final int width, final int height,
     final String charType, final Color color) {
        final CharacterAnimationPanelImpl instance = new CharacterAnimationPanelImpl();
        instance.init(width, height, charType, color);
        return instance;
    }

    private void init(final int width, final int height, final String charType, final Color color) {
        this.setLayout(null);
        this.charType = charType;
        this.healthBar = CharacterHealthBar.create(width, height, color);
        this.setOpaque(false);
        this.setSize(width, height);
        this.add(this.healthBar);
    }

    @Override
    public void setCharacterImage(final Image image) {
        this.currentImage = image;
        this.repaint();
    }

    @Override
    public void setHealthBarAlignment() {
        if (CardType.ENEMY.get().equals(this.charType) || CardType.BOSS.get().equals(this.charType)) {
            this.healthBar.setBounds((int) (this.getWidth() * (1 - BAR_WIDTH_RESIZE)),
             this.getHeight() / BAR_HEIGHT_DIVISOR,
            (int) (this.getWidth() * BAR_WIDTH_RESIZE), this.healthBar.getHeight());
        } else {
            this.healthBar.setBounds(0, this.getHeight() / BAR_HEIGHT_DIVISOR,
            (int) (this.getWidth() * BAR_WIDTH_RESIZE), this.healthBar.getHeight());
        }
        this.healthBar.setVisible(true);
    }

    @Override
    public void setHealthBarImage(final int percentage) {
        this.healthBar.setPercentage(percentage);
    }

    @Override
    public void disposeClosing() {
        this.setCharacterImage(null);
        this.remove(this.healthBar);
        this.repaint();
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.drawImage(currentImage, 0, 0, this);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
