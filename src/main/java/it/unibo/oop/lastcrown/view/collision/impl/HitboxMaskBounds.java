package it.unibo.oop.lastcrown.view.collision.impl;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.model.collision.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.collision.api.HitboxMask;
import it.unibo.oop.lastcrown.view.collision.api.HitboxPanel;

/**
 * Utility class responsible for calculating and updating the position and size of a hitbox
 * based on a character's graphical component and its mask image.
 */
@SuppressFBWarnings(
    value = "EI",
    justification = """
        This class acts as a mediator, directly manipulating the state of external 'live' objects 
        (Hitbox, JComponent, HitboxPanel). Defensive copies can't be used because we need to act on the
        original objects.
        The exposure of these mutable references is a consequence of having to manually
        extract the hitbox from the image, rather than having a getter method in the model classes.
        """
)
public final class HitboxMaskBounds implements HitboxMask {
    private int offsetX;
    private int offsetY;
    private int hitboxHeight;
    private int hitboxWidth;
    private final Hitbox hitbox;
    private final JComponent charComponent;
    private final HitboxPanel hitboxPanel;

    /**
     * Constructs a new HitboxMaskBounds object.
     *
     * @param hitbox the hitbox model to update
     * @param charComponent the Swing component representing the character
     * @param hitboxPanel the panel responsible for displaying the hitbox
     */
    public HitboxMaskBounds(final Hitbox hitbox, final JComponent charComponent, final HitboxPanel hitboxPanel) {
        this.charComponent = charComponent;
        this.hitbox = hitbox;
        this.hitboxPanel = hitboxPanel;
        this.offsetX = 0;
        this.offsetY = 0;
        this.hitboxWidth = 0;
        this.hitboxHeight = 0;
    }

    /**
     * Calculates the hitbox's center and dimensions by analyzing the alpha channel of the given image.
     * Updates the hitbox's size and position accordingly.
     *
     * @param image the image used to determine the opaque area of the hitbox
     */
    @Override
    public void calculateHitboxCenter(final BufferedImage image) {
        int minX = image.getWidth();
        int minY = image.getHeight();
        int maxX = 0;
        int maxY = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = image.getRGB(x, y);
                final int alpha = (pixel >> 24) & 0xff;
                if (alpha > 0) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        this.hitboxWidth = maxX - minX + 1;
        this.hitboxHeight = maxY - minY + 1;
        this.offsetX = minX;
        this.offsetY = minY;

        updateHitboxPosition(charComponent.getX(), charComponent.getY());

        hitbox.setWidth(hitboxWidth);
        hitbox.setHeight(hitboxHeight);
        hitboxPanel.updatePanel();
    }

    /**
     * Updates the hitbox's position based on the graphical component's current coordinates.
     *
     * @param componentX the X coordinate of the character's component
     * @param componentY the Y coordinate of the character's component
     */
    @Override
    public void updateHitboxPosition(final int componentX, final int componentY) {
        final int globalX = componentX + offsetX;
        final int globalY = componentY + offsetY;
        hitbox.setPosition(new Point2DImpl(globalX, globalY));
        hitboxPanel.setPanelPosition(globalX, globalY);
        hitboxPanel.updatePanel();
    }

    /**
     * Returns the center point of the hitbox relative to the character component.
     *
     * @return the center of the hitbox as a {@link Point2D} object
     */
    @Override
    public Point2D getCenter() {
        // centro relativo alla hitbox (offset + metà dimensioni)
        final double centerX = offsetX + (hitboxWidth / 2.0);
        final double centerY = offsetY + (hitboxHeight / 2.0);
        return new Point2DImpl(centerX, centerY);
    }

    /**
     * Returns the character's graphical component.
     *
     * @return the {@link JComponent} representing the character
     */
    @Override
    public JComponent getCharComponent() {
        return this.charComponent;
    }
}
