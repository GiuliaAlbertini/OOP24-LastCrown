package it.unibo.oop.lastcrown.view.collision.impl;

import java.awt.Color;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.collision.api.HitboxPanel;

/**
 * Implementation of HitboxPanel that visually represents a hitbox using a JPanel
 * The panel is shown as a semi-transparent red rectangle, useful for debugging purposes.
 */
public final class HitboxPanelImpl implements HitboxPanel {
    private static final int RED = 255;
    private static final int GREEN = 0;
    private static final int BLUE = 0;
    private static final int ALPHA = 100;
    private final Hitbox hitbox;
    private final JPanel hitboxPanel;

    /**
     * Constructs a new HitboxPanelImpl associated with the given hitbox.
     * Initializes the panel with a semi-transparent red color.
     *
     * @param hitbox the hitbox model to represent visually
     */
    public HitboxPanelImpl(final Hitbox hitbox) {
        this.hitbox = hitbox;
        this.hitboxPanel = new JPanel();
        this.hitboxPanel.setBackground(new Color(RED, GREEN, BLUE, ALPHA)); // Rosso semi-trasparente per debug
        updatePanel();
    }

    @Override
    public JPanel getHitboxPanel() {
        return this.hitboxPanel;
    }

    @Override
    public void updatePanel() {
        hitboxPanel.setBounds(
                (int) hitbox.getPosition().x(),
                (int) hitbox.getPosition().y(),
                hitbox.getWidth(),
                hitbox.getHeight());
    }

    @Override
    public void setPanelPosition(final int x, final int y) {
        hitbox.setPosition(new Point2DImpl(x, y));
        updatePanel();
    }
}
