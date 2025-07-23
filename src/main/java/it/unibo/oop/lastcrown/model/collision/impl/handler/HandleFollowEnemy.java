package it.unibo.oop.lastcrown.model.collision.impl.handler;
import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.model.collision.impl.Point2DImpl;

/**
 * Handles the logic for making a character follow an enemy using a quadratic Bezier curve.
 * The movement is smooth and progressively updates based on delta time.
 */
public final class HandleFollowEnemy {
    private final Collidable character;
    private final Collidable enemy;
    private static final double SPEED = 20.0;
    //private final double baseCurveIntensity = 2;
    private double t;
    private final boolean stopped; //false
    private boolean active; //false
    private Point2D lastDelta = new Point2DImpl(0, 0);
    private static final double BEZIERSTEP = 0.01;

    //private static final double FIXED_CURVE_HEIGHT = 50.0; // altezza fissa per la curva

    /**
     * Constructs a new handler to make the first collidable follow the second.
     *
     * @param event the collision event containing the character and the enemy
     */
    public HandleFollowEnemy(final CollisionEvent event) {
        this.stopped = false;
        this.character = event.getCollidable1();
        this.enemy = event.getCollidable2();
    }

    /**
     * Starts the following behavior if it hasn't already started and is not stopped.
     */
    public void startFollowing() {
        if (!active && !stopped) {
            this.active = true;
            this.t = 0.0;
        }
    }

    /**
     * Updates the character's position based on Bezier curve progression.
     *
     * @param deltaMs time passed since last update in milliseconds
     * @return true if the movement continues, false if the movement ends
     */
    public boolean update(final long deltaMs) {
        if (active && !stopped) {
            final Point2D p0 = character.getHitbox().getPosition();
            final Point2D p2 = calculateTargetPosition();
            final Point2D p1 = computeControlPoint(p0, p2);

            final double totalDistance = estimateBezierLength(p0, p1, p2);
            final double distanceToTravel = deltaMs / 1000.0 * SPEED;
            t = Math.min(1.0, t + (distanceToTravel / totalDistance));

            final Point2D newPos = quadraticBezier(p0, p1, p2, t);
            final double dx = newPos.x() - p0.x();
            final double dy = newPos.y() - p0.y();

            // aggiorno lastDelta
            lastDelta = new Point2DImpl(dx, dy);

            character.getHitbox().setPosition(newPos);

            return !(t >= 1.0 || character.getHitbox().checkCollision(enemy.getHitbox()));

        } else {
            return false;
        }
    }


    private Point2D quadraticBezier(final Point2D p0, final Point2D p1, final Point2D p2, final double t) {
        final double oneMinusT = 1 - t;
        final double x = oneMinusT * oneMinusT * p0.x()
                + 2 * oneMinusT * t * p1.x()
                + t * t * p2.x();
        final double y = oneMinusT * oneMinusT * p0.y()
                + 2 * oneMinusT * t * p1.y()
                + t * t * p2.y();
        return new Point2DImpl(x, y);
    }

    private Point2D computeControlPoint(final Point2D p0, final Point2D p2) {
        // Usa un punto medio semplice senza offset per testare:
        final double midX = (p0.x() + p2.x()) / 2;
        final double midY = (p0.y() + p2.y()) / 2;
        return new Point2DImpl(midX, midY);
    }

    private Point2D calculateTargetPosition() {
        final double offsetX = -enemy.getHitbox().getWidth() / 2.0
                - character.getHitbox().getWidth() / 2.0;
        return new Point2DImpl(
                enemy.getHitbox().getPosition().x() + offsetX,
                enemy.getHitbox().getPosition().y());
    }

    private double estimateBezierLength(final Point2D p0, final Point2D p1, final Point2D p2) {
        double length = 0.0;
        Point2D prev = p0;
        for (double t = BEZIERSTEP; t <= 1.0; t += BEZIERSTEP) {
            final Point2D current = quadraticBezier(p0, p1, p2, t);
            length += distance(prev, current);
            prev = current;
        }
        return length;
    }

    private double distance(final Point2D a, final Point2D b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    /**
     * Returns the current position of the character.
     *
     * @return current character position
     */
    public Point2D getCurrentPosition() {
        return character.getHitbox().getPosition();
    }

    /**
     * Returns the character that is following.
     *
     * @return the character collidable
     */
    public Collidable getCharacter() {
        return character;
    }

    /**
     * Returns the enemy being followed.
     *
     * @return the enemy collidable
     */
    public Collidable getEnemy() {
        return enemy;
    }

    /**
     * Returns whether the following has been stopped.
     *
     * @return false by default (not dynamically stoppable yet)
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Returns the last computed movement delta.
     *
     * @return the last movement delta vector
     */
    public Point2D getDelta() {
        return lastDelta;
    }
}

/*
 * private Point2D computeControlPoint(Point2D p0, Point2D p2, double speed,
 * boolean curveUp) {
 * double dx = p2.x() - p0.x();
 * double dy = p2.y() - p0.y();
 * double dist = Math.hypot(dx, dy);
 * double dynamicCurveIntensity = baseCurveIntensity + (30.0 - speed) * 0.05;
 *
 * double baseFactor = 0.2 + Math.pow(dist / 500.0, 1.5);
 * baseFactor = Math.min(baseFactor, 1.5);
 *
 * double factor = baseFactor * dynamicCurveIntensity;
 *
 * double midX = (p0.x() + p2.x()) / 2;
 * double midY = (p0.y() + p2.y()) / 2;
 *
 * double heightDiff = p2.y() - p0.y();
 * double heightAdjustment = heightDiff * 5;
 * double offsetY = curveUp
 * ? (midY - dist * factor + heightAdjustment)
 * : (midY + dist * factor + heightAdjustment);
 *
 * return new Point2DImpl(midX, offsetY);
 * }
 */
