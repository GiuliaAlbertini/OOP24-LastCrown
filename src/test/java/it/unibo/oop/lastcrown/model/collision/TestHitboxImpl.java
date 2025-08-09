package it.unibo.oop.lastcrown.model.collision;

import it.unibo.oop.lastcrown.model.collision.impl.HitboxImpl;
import it.unibo.oop.lastcrown.utility.api.Point2D;
import it.unibo.oop.lastcrown.utility.impl.Point2DImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class TestHitboxImpl {
    private static final int INITIAL_WIDTH = 100;
    private static final int INITIAL_HEIGHT = 50;
    private HitboxImpl hitbox;
    private final Point2D initialPosition = new Point2DImpl(10, 20);


    @BeforeEach
    void setUp() {
        hitbox = new HitboxImpl(INITIAL_WIDTH, INITIAL_HEIGHT, initialPosition);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(INITIAL_WIDTH, hitbox.getWidth(), "Width should be correctly initialized.");
        assertEquals(INITIAL_HEIGHT, hitbox.getHeight(), "Height should be correctly initialized.");
        assertEquals(initialPosition, hitbox.getPosition(), "Position should be correctly initialized.");
    }

    @Test
    void testSetters() {
        final Point2D newPosition = new Point2DImpl(30, 40);
        final int newWidth = 120;
        final int newHeight = 60;

        hitbox.setPosition(newPosition);
        hitbox.setWidth(newWidth);
        hitbox.setHeight(newHeight);

        assertEquals(newWidth, hitbox.getWidth(), "Width should be updatable.");
        assertEquals(newHeight, hitbox.getHeight(), "Height should be updatable.");
        assertEquals(newPosition, hitbox.getPosition(), "Position should be updatable.");
    }

    @Test
    void testGetCenter() {
        final double expectedCenterX = initialPosition.x() + INITIAL_WIDTH / 2.0;
        final double expectedCenterY = initialPosition.y() + INITIAL_HEIGHT / 2.0;
        final Point2D expectedCenter = new Point2DImpl(expectedCenterX, expectedCenterY);

        final Point2D calculatedCenter = hitbox.getCenter();

        assertEquals(expectedCenter.x(), calculatedCenter.x(), "Center X coordinate is incorrect.");
        assertEquals(expectedCenter.y(), calculatedCenter.y(), "Center Y coordinate is incorrect.");
    }

    @Test
    void testCheckCollisionWhenOverlapping() {
        final HitboxImpl other = new HitboxImpl(50, 50, new Point2DImpl(50, 40));
        assertTrue(hitbox.checkCollision(other), "Hitboxes should be colliding.");
        assertTrue(other.checkCollision(hitbox), "Collision check should be symmetric.");
    }

    @Test
    void testCheckCollisionWhenNotOverlapping() {
        final HitboxImpl other = new HitboxImpl(50, 50, new Point2DImpl(200, 200));

        assertFalse(hitbox.checkCollision(other), "Hitboxes should not be colliding.");
        assertFalse(other.checkCollision(hitbox), "Collision check should be symmetric.");
    }

    @Test
    void testCheckCollisionWithBuffer() {
        final HitboxImpl other = new HitboxImpl(10, 10, new Point2DImpl(111, 20));

        assertTrue(hitbox.checkCollision(other), "Hitboxes should collide due to the +2 buffer.");

        final HitboxImpl other2 = new HitboxImpl(10, 10, new Point2DImpl(112, 20));
        assertTrue(hitbox.checkCollision(other2), "Hitboxes should collide when exactly at buffer distance.");

        final HitboxImpl other3 = new HitboxImpl(10, 10, new Point2DImpl(113, 20));
        assertFalse(hitbox.checkCollision(other3), "Hitboxes should not collide when outside the buffer.");
    }
}
