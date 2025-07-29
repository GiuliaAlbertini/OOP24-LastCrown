package it.unibo.oop.lastcrown.controller.collision.impl;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Radius;
import it.unibo.oop.lastcrown.model.collision.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.collision.api.HitboxPanel;
import it.unibo.oop.lastcrown.view.collision.api.RadiusPanel;
import it.unibo.oop.lastcrown.view.collision.impl.HitboxMaskBounds;

/**
 * Implementation of HitboxController responsible for managing the logic
 * and view updates of a character's hitbox and optional radius indicator.
 */
public final class HitboxControllerImpl implements HitboxController {
    private final Hitbox hitbox;
    private final HitboxPanel view;
    private final HitboxMaskBounds bounds;
    private Radius radius;
    private RadiusPanel radiusPanel;

    /**
     * @param hitbox the model representing the hitbox geometry and position
     * @param panel the visual panel displaying the hitbox
     * @param bounds the bounds calculator for positioning based on the mask
     */
    public HitboxControllerImpl(final Hitbox hitbox, final HitboxPanel panel, final HitboxMaskBounds bounds) {
        this.hitbox = hitbox;
        this.view = panel;
        this.bounds = bounds;
    }

    @Override
    public void setnewPosition(final int x, final int y) {
        this.hitbox.setPosition(new Point2DImpl(x, y));

        bounds.updateHitboxPosition(x, y);
        if (radiusPanel != null) {
            radiusPanel.updatePosition();
        }
        //view.updatePanel();
    }

    @Override
    public void updateView() {
        view.updatePanel();
    }

    @Override
    public void setVisibile(final boolean visible) {
        view.getHitboxPanel().setVisible(visible);
    }

    @Override
    public JComponent getHitboxPanel() {
        return view.getHitboxPanel();
    }

    @Override
    public Radius getRadius() {
        return this.radius;
    }

    @Override
    public JPanel getRadiusPanel() {
        return radiusPanel.getRadiusPanel();
    }

    @Override
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    @Override
    public void setRadius(final Radius radius) {
        this.radius = radius;
    }

    @Override
    public void setRadiusPanel(final RadiusPanel radiusPanel) {
        this.radiusPanel = radiusPanel;
    }

    @Override
    public void removeFromPanel() {
        if (view != null && view.getHitboxPanel().getParent() != null) {
            view.getHitboxPanel().getParent().remove(view.getHitboxPanel());
        }

        if (radiusPanel != null && radiusPanel.getRadiusPanel().getParent() != null) {
            radiusPanel.getRadiusPanel().getParent().remove(radiusPanel.getRadiusPanel());
        }
    }
}
