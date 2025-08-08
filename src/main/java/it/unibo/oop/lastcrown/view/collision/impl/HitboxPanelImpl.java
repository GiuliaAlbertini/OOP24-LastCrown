package it.unibo.oop.lastcrown.view.collision.impl;

import java.awt.Color;
import javax.swing.JPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.view.collision.api.HitboxPanel;
import it.unibo.oop.lastcrown.model.collision.impl.Point2DImpl;

/**
 * Implementation of HitboxPanel that visually represents a hitbox using a
 * JPanel
 * The panel is shown as a semi-transparent red rectangle, useful for debugging
 * purposes.
 */

@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = """
                This class is a View component designed to visually represent a 'live' Hitbox model.
                It requires a direct reference to the original Hitbox to stay synchronized. A defensive copy would break this fundamental link.
        """)
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
                (int) this.hitbox.getPosition().x(),
                (int) this.hitbox.getPosition().y(),
                this.hitbox.getWidth(),
                this.hitbox.getHeight());
    }

    @Override
    public void setPanelPosition(final int x, final int y) {
        this.hitbox.setPosition(new Point2DImpl(x, y));
        updatePanel();
    }

    /**
     * Returns the center point of the hitbox.
     *
     * @return the (x, y) center of the hitbox as Point2DImpl
     */
    public Point2D getCenter() {
        return this.hitbox.getCenter();
    }

}
