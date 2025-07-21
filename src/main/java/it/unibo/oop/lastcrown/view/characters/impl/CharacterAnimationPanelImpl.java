package it.unibo.oop.lastcrown.view.characters.impl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
    private static final int BAR_HEIGHT_DIVISOR = 5;
    private static final double BAR_HEIGHT_MUL = 0.05;
    private static final double BAR_WIDTH_RESIZE = 0.75;
    private int panelWidth;
    private int panelHeight;
    private String charType;
    private CharacterHealthBar healthBar;
    private transient BufferedImage currentImage;

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
        this.panelWidth = width;
        this.panelHeight = height;
        final int barWidth = (int) (panelWidth * BAR_WIDTH_RESIZE);
        this.healthBar = CharacterHealthBar.create(barWidth, (int) (height * BAR_HEIGHT_MUL), color);
        this.setPreferredSize(new Dimension(width, height));
        this.add(this.healthBar);
        this.setOpaque(false);
        this.setHealthBarAlignment();
    }

    @Override
    public void setCharacterImage(final BufferedImage image) {
        this.currentImage = image;
        this.repaint();
    }

    @Override
    public void setHealthBarAlignment() {
        final int barWidth = (int) (panelWidth * BAR_WIDTH_RESIZE);
        final int barHeight = this.healthBar.getPreferredSize().height;
        final int barY = panelHeight / BAR_HEIGHT_DIVISOR;
        final int barX;
        if (CardType.ENEMY.get().equals(this.charType) || CardType.BOSS.get().equals(this.charType)) {
            barX = panelWidth - barWidth;
        } else {
            barX = 0;
        }
        this.healthBar.setBounds(barX, barY, barWidth, barHeight);
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
        public Dimension getPreferredSize() {
        return new Dimension(this.panelWidth, this.panelHeight);
    }

    @Override
    protected void paintChildren(final Graphics g) {
        super.paintChildren(g);
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, this);
        }
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
