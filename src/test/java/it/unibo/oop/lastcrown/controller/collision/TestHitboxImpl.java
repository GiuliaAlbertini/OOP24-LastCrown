package it.unibo.oop.lastcrown.controller.collision;

import it.unibo.oop.lastcrown.model.collision.impl.HitboxImpl;
import it.unibo.oop.lastcrown.utility.api.Point2D;
import it.unibo.oop.lastcrown.utility.impl.Point2DImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class TestHitboxImpl {

    private HitboxImpl hitbox;
    private final Point2D initialPosition = new Point2DImpl(10, 20);
    private final int initialWidth = 100;
    private final int initialHeight = 50;

    @BeforeEach
    void setUp() {
        hitbox = new HitboxImpl(initialWidth, initialHeight, initialPosition);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(initialWidth, hitbox.getWidth(), "Width should be correctly initialized.");
        assertEquals(initialHeight, hitbox.getHeight(), "Height should be correctly initialized.");
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
        final double expectedCenterX = initialPosition.x() + initialWidth / 2.0; // 10 + 50 = 60
        final double expectedCenterY = initialPosition.y() + initialHeight / 2.0; // 20 + 25 = 45
        final Point2D expectedCenter = new Point2DImpl(expectedCenterX, expectedCenterY);

        final Point2D calculatedCenter = hitbox.getCenter();

        assertEquals(expectedCenter.x(), calculatedCenter.x(), "Center X coordinate is incorrect.");
        assertEquals(expectedCenter.y(), calculatedCenter.y(), "Center Y coordinate is incorrect.");
    }

    @Test
    void testCheckCollisionWhenOverlapping() {
        final HitboxImpl other = new HitboxImpl(50, 50, new Point2DImpl(50, 40)); // (50,40) si sovrappone a (10,20) con w=100,h=50

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