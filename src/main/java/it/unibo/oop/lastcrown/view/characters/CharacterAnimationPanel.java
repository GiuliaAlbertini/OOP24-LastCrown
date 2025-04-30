package it.unibo.oop.lastcrown.view.characters;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * A custom JPanel that contains the animation frames and the healthbar of a specific character.
 */
final class CharacterAnimationPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int BAR_HEIGHT_DIVISOR = 9;
    private CharacterHealthBar healthBar;
    private transient Image currentImage;

    private CharacterAnimationPanel() { }

    /**
     * Creates a new instance of CharacterAnimationPanel.
     * @param width the horizontal size of this animation panel
     * @param height the vertical size of this animation panel
     * @param color the color of the health bar associated with this panel
     * @return new CharacterAnimationPanel
     */
    public static CharacterAnimationPanel create(final int width, final int height, final Color color) {
        final CharacterAnimationPanel instance = new CharacterAnimationPanel();
        instance.init(width, height, color);
        return instance;
    }

    private void init(final int width, final int height, final Color color) {
        this.healthBar = CharacterHealthBar.create(width, height, color);
        this.setOpaque(false);
        this.setSize(width, height);
        this.add(this.healthBar);
    }
    /**
     * Set the next frame to be shown.
     * @param image the next frame to be shown
     */
    public void setCharacterImage(final Image image) {
        this.currentImage = image;
        this.repaint();
    }

    /**
     * Realign this character health bar position after moving the animation panel.
     */
    public void setHealthBarPosition() {
        this.healthBar.setBounds(0, this.getHeight() / BAR_HEIGHT_DIVISOR,
         this.healthBar.getWidth(), this.healthBar.getHeight());
        this.healthBar.setVisible(true);
    }

    /**
     * Set new percentage value of this character health bar.
     * @param percentage
     */
    public void setHealthBarImage(final int percentage) {
        this.healthBar.setPercentage(percentage);
    }

    /**
     * Clear this animation panel image and remove the health bar.
     */
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
}
