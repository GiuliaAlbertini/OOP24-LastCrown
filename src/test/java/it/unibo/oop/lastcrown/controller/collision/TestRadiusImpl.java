package it.unibo.oop.lastcrown.controller.collision;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.impl.HitboxImpl;
import it.unibo.oop.lastcrown.model.collision.impl.RadiusImpl;
import it.unibo.oop.lastcrown.utility.impl.Point2DImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link RadiusImpl} class.
 */
final class TestRadiusImpl {

    private Hitbox originHitbox;
    private RadiusImpl radius;
    private static final double TEST_RADIUS = 50.0;

    @BeforeEach
    void setUp() {
        originHitbox = new HitboxImpl(10, 10, new Point2DImpl(95, 95)); // Centro a (100, 100)
        radius = new RadiusImpl(originHitbox, TEST_RADIUS);
    }

    @Test
    void testNoEnemiesFoundWhenOutsideOrBehind() {
        final Hitbox enemyFar = new HitboxImpl(10, 10, new Point2DImpl(200, 200)); // Lontano
        final Hitbox enemyBehind = new HitboxImpl(10, 10, new Point2DImpl(80, 100)); // Dietro, anche se vicino

        final List<Hitbox> enemies = List.of(enemyFar, enemyBehind);

        assertFalse(radius.hasEnemyInRadius(enemies), "Should not find any enemies.");
        assertTrue(radius.getEnemiesInRadius(enemies).isEmpty(), "The list of enemies in radius should be empty.");
        assertTrue(radius.getClosestEnemyInRadius(enemies).isEmpty(), "Optional of closest enemy should be empty.");
    }

    @Test
    void testFindsEnemiesInRadiusAndInFront() {
        final Hitbox enemyInRange = new HitboxImpl(10, 10, new Point2DImpl(115, 95));
        final Hitbox enemyBehind = new HitboxImpl(10, 10, new Point2DImpl(75, 95));
        final Hitbox enemyTooFar = new HitboxImpl(10, 10, new Point2DImpl(195, 95));

        final List<Hitbox> enemies = List.of(enemyInRange, enemyBehind, enemyTooFar);

        assertTrue(radius.hasEnemyInRadius(enemies), "Should find at least one enemy.");
        final List<Hitbox> found = radius.getEnemiesInRadius(enemies);
        assertEquals(1, found.size(), "Should find exactly one enemy.");
        assertEquals(enemyInRange, found.get(0), "The found enemy should be the correct one.");
    }

    @Test
    void testGetClosestEnemy() {
        final Hitbox enemyClose = new HitboxImpl(10, 10, new Point2DImpl(105, 95));
        final Hitbox enemyMid = new HitboxImpl(10, 10, new Point2DImpl(125, 95));
        final Hitbox enemyFar = new HitboxImpl(10, 10, new Point2DImpl(145, 95));

        final List<Hitbox> enemies = List.of(enemyMid, enemyFar, enemyClose);

        final Optional<Hitbox> closestOpt = radius.getClosestEnemyInRadius(enemies);
        assertTrue(closestOpt.isPresent(), "Should find a closest enemy.");
        assertEquals(enemyClose, closestOpt.get(), "The closest enemy is not the correct one.");
    }
}