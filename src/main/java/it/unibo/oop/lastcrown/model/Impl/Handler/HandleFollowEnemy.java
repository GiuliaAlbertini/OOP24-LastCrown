package it.unibo.oop.lastcrown.model.impl.Handler;

import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.impl.CharacterMovementStop;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;

public class HandleFollowEnemy {
    private final Collidable character;
    private final Collidable enemy;
    private final CharacterMovementObserver observer;
    private final CharacterMovementStop stop;
    private final double speed = 30.0; // pixels per second
    private double baseCurveIntensity = 3;
    private double t = 0.0;
    private boolean stopped = false;

    public HandleFollowEnemy(CollisionEvent event, CharacterMovementObserver observer, CharacterMovementStop stop) {
        this.character = event.getCollidable1();
        this.enemy = event.getCollidable2();
        this.observer = observer;
        this.stop = stop;
    }

    private Point2D quadraticBezier(Point2D p0, Point2D p1, Point2D p2, double t) {
        double oneMinusT = 1 - t;
        double x = oneMinusT * oneMinusT * p0.x()
                + 2 * oneMinusT * t * p1.x()
                + t * t * p2.x();
        double y = oneMinusT * oneMinusT * p0.y()
                + 2 * oneMinusT * t * p1.y()
                + t * t * p2.y();
        return new Point2DImpl(x, y);
    }

    private Point2D computeControlPoint(Point2D p0, Point2D p2, double speed, boolean curveUp) {
        double dx = p2.x() - p0.x();
        double dy = p2.y() - p0.y();
        double dist = Math.hypot(dx, dy);
        double dynamicCurveIntensity = baseCurveIntensity + (30.0 - speed) * 0.05;

        double baseFactor = 0.2 + Math.pow(dist / 500.0, 1.5);
        baseFactor = Math.min(baseFactor, 1.5);

        double factor = baseFactor * dynamicCurveIntensity;

        double midX = (p0.x() + p2.x()) / 2;
        double midY = (p0.y() + p2.y()) / 2;

        double heightDiff = p2.y() - p0.y();
        double heightAdjustment = heightDiff * 5;
        double offsetY = curveUp
                ? (midY - dist * factor + heightAdjustment)
                : (midY + dist * factor + heightAdjustment);

        return new Point2DImpl(midX, offsetY);
    }

    public void update(long deltaMs) {

        if (hasCollided() && stopped == true) {
            stopMovement();
            return;
        }

        Point2D p0 = character.getHitbox().getPosition();

        // Calcola offset orizzontale con margine extra per evitare sovrapposizione
        double margin = 40.0; // margine extra per stare più lontano (modifica questo valore)
        // calcolo punto p2 del nemico
        double offsetX = -enemy.getHitbox().getWidth() / 2.0 - character.getHitbox().getWidth() / 2.0 - margin;

        Point2D p2 = new Point2DImpl(
                enemy.getHitbox().getPosition().x() + offsetX,
                enemy.getHitbox().getPosition().y());

        Point2D control = computeControlPoint(p0, p2, speed, true);

        double distTotal = estimateBezierLength(p0, control, p2);
        double distToTravel = (deltaMs / 1000.0) * speed;
        double tIncrement = distToTravel / distTotal;
        t = Math.min(1.0, t + tIncrement);

        Point2D newPos = quadraticBezier(p0, control, p2, t);
        double dx = newPos.x() - p0.x();
        double dy = newPos.y() - p0.y();

        character.getHitbox().setPosition(newPos);

        if (observer != null) {
            observer.notifyMovement(character.getCardidentifier(), (int) dx, (int) dy);
        }
    }

    private double estimateBezierLength(Point2D p0, Point2D p1, Point2D p2) {
        double length = 0.0;
        Point2D prev = p0;
        for (double t = 0.01; t <= 1.0; t += 0.01) {
            Point2D current = quadraticBezier(p0, p1, p2, t);
            length += distance(prev, current);
            prev = current;
        }
        return length;
    }

    private double distance(Point2D a, Point2D b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    private boolean hasCollided() {
        boolean collided = character.getHitbox().checkCollision(enemy.getHitbox());
        if (character.getHitbox().checkCollision(enemy.getHitbox())) {
            System.out.println("Collision check: " + collided);
            stopped = true;
            return true;
        }
        return false;
    }

    private void stopMovement() {
        if (stop != null) {
            stop.notifyMovementStop(character.getCardidentifier().number(), enemy.getCardidentifier().number());
        }
    }

    public Point2D getCurrentPosition() {
        return character.getHitbox().getPosition();
    }

    public Collidable getCharacter() {
        return character;
    }
}
