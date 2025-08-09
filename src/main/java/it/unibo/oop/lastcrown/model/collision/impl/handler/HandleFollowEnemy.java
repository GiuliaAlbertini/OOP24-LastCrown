package it.unibo.oop.lastcrown.model.collision.impl.handler;
import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.FollowEnemy;
import it.unibo.oop.lastcrown.utility.Constant;
import it.unibo.oop.lastcrown.utility.api.Point2D;
import it.unibo.oop.lastcrown.utility.impl.Point2DImpl;

/** Implementation for the enemy following movement handler. */
public final class HandleFollowEnemy implements FollowEnemy {
    private final Collidable character;
    private final Collidable enemy;
    private double t;
    private final boolean stopped;
    private boolean active;
    private Point2D lastDelta = new Point2DImpl(0, 0);

    /**
     * Instantiates an enemy following movement handler.
     * @param event the collision event triggering the movement.
     */
    public HandleFollowEnemy(final CollisionEvent event) {
        this.stopped = false;
        this.character = event.getCollidable1();
        this.enemy = event.getCollidable2();
    }

    /**
     * Starts the following motion.
     */
    @Override
    public void startFollowing() {
        if (!active && !stopped) {
            this.active = true;
            this.t = 0;
        }
    }

    /**
     * Performs a single algorithm step.
     * @param deltaMs the amount of time since last step.
     * @return false if the following movement is complete, true otherwise.
     */
    @Override
    public boolean update(final long deltaMs) {
        if (active && !stopped) {
            final Point2D p0 = character.getHitbox().getPosition();
            final Point2D p2 = calculateTargetPosition();
            final Point2D p1 = computeControlPoint(p0, p2);

            final double totalDistance = estimateBezierLength(p0, p1, p2);
            final double distanceToTravel = deltaMs / Constant.TIME_DIVIDER * Constant.SPEED;
            t = Math.min(Constant.BEZIER_LIMIT, t + (distanceToTravel / totalDistance));

            final Point2D newPos = quadraticBezier(p0, p1, p2, t);
            final double dx = newPos.x() - p0.x();
            final double dy = newPos.y() - p0.y();

            lastDelta = new Point2DImpl(dx, dy);

            character.getHitbox().setPosition(newPos);
            return !(t >= Constant.BEZIER_LIMIT || character.getHitbox().checkCollision(enemy.getHitbox()));
        } else {
            return false;
        }
    }

    private Point2D quadraticBezier(final Point2D p0, final Point2D p1, final Point2D p2, final double t) {
        final double oneMinusT = Constant.BEZIER_LIMIT - t;
        final double x = oneMinusT * oneMinusT * p0.x()
                + 2 * oneMinusT * t * p1.x()
                + t * t * p2.x();
        final double y = oneMinusT * oneMinusT * p0.y()
                + 2 * oneMinusT * t * p1.y()
                + t * t * p2.y();
        return new Point2DImpl(x, y);
    }

    private Point2D computeControlPoint(final Point2D p0, final Point2D p2) {
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
        for (int t = Constant.STEP_START; t <= Constant.STEP_LIMIT; t += Constant.STEP_SIZE) {
            final Point2D current = quadraticBezier(p0, p1, p2, t / Constant.BEZIER_DIVIDER);
            length += distance(prev, current);
            prev = current;
        }
        return length;
    }

    private double distance(final Point2D a, final Point2D b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    /**
     * Getter for the player's current position.
     * @return a Point2D representing the current player's position.
     */
    @Override
    public Point2D getCurrentPosition() {
        return character.getHitbox().getPosition();
    }

    /**
     * Getter for the character.
     * @return the Collidable representing the character.
     */
    @Override
    public Collidable getCharacter() {
        return character;
    }

    /**
     * Getter for the enemy.
     * @return the Collidable representing the currently followed enemy.
     */
    @Override
    public Collidable getEnemy() {
        return enemy;
    }

    /**
     * Getter for the last position variation.
     * @return the Point2D representation of how much the player has moved since last step.
     */
    @Override
    public Point2D getDelta() {
        return lastDelta;
    }
}
