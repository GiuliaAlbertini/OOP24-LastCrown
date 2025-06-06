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

    private Point2D currentPosition;
    private Point2D targetPosition;

    private static final int BASE_STEP_SIZE = 5;           // Passo fisso 5 come richiesto
    private static final double CURVE_FACTOR = 0.3;        // Intensità curva
    //private static final double INITIAL_IMMEDIATE_STEPS = 1; // Passi immediati all'inizio
    //private static final long STEP_DELAY_MS = 30;          // 200ms di attesa tra i passi

    private long elapsedTimeSinceLastStep = 0;
    private int immediateStepsTaken = 0;

    public HandleFollowEnemy(CollisionEvent event, CharacterMovementObserver observer, CharacterMovementStop stop) {
        this.character = event.getCollidable1();
        this.enemy = event.getCollidable2();
        this.observer = observer;
        this.stop = stop;
        this.currentPosition = character.getHitbox().getPosition();
        this.targetPosition = enemy.getHitbox().getPosition();
    }

    /**
     * Aggiorna il movimento solo se è passato abbastanza tempo (STEP_DELAY_MS)
     * @param deltaMs millisecondi trascorsi dall'ultimo update chiamato
     */
    public void updateMovement(long deltaMs) {
        if (!isComplete()) {
            elapsedTimeSinceLastStep += deltaMs;
            //if (elapsedTimeSinceLastStep >= STEP_DELAY_MS) {
                elapsedTimeSinceLastStep = 0;

                // Fase iniziale: primi passi immediati
                /*if (immediateStepsTaken < INITIAL_IMMEDIATE_STEPS) {
                    executeImmediateStep();
                    immediateStepsTaken++;
                    return;
                }*/

                // Fase normale: movimento regolare
                executeNormalStep();
            //}
        }
    }

    private void executeImmediateStep() {
        targetPosition = enemy.getHitbox().getPosition();
        Point2D stepDirection = calculateStepDirection();

        Point2D newPosition = new Point2DImpl(
            currentPosition.x() + stepDirection.x(),
            currentPosition.y() + stepDirection.y()
        );

        updatePosition(newPosition);
        currentPosition = newPosition;
    }

    private void executeNormalStep() {
        targetPosition = enemy.getHitbox().getPosition();
        Point2D stepDirection = calculateStepDirection();

        Point2D newPosition = new Point2DImpl(
            currentPosition.x() + 0.4 * stepDirection.x(),
            currentPosition.y() + 0.4 * stepDirection.y()
        );

        updatePosition(newPosition);
        currentPosition = newPosition;
    }

    private Point2D calculateStepDirection() {
        double dx = targetPosition.x() - currentPosition.x();
        double dy = targetPosition.y() - currentPosition.y();
        double distance = distance(currentPosition, targetPosition);

        if (distance > 0) {
            dx /= distance;
            dy /= distance;
        }

        double curveEffect = CURVE_FACTOR * Math.min(1.0, distance / 100.0);
        dy -= curveEffect;

        return new Point2DImpl(
            dx * BASE_STEP_SIZE,
            dy * BASE_STEP_SIZE
        );
    }

    private void updatePosition(Point2D newPos) {
        double dx = newPos.x() - character.getHitbox().getPosition().x();
        double dy = newPos.y() - character.getHitbox().getPosition().y();

        character.getHitbox().setPosition(newPos);

        if (observer != null) {
            observer.notifyMovement(character.getCardidentifier(), (int) dx, (int) dy);
        }
    }

    public boolean isComplete() {
        if(character.getHitbox().checkCollision(enemy.getHitbox())){
            stop.notifyMovementStop(character.getCardidentifier().number(), enemy.getCardidentifier().number());
        }
        return false;

    }

    private double distance(Point2D a, Point2D b) {
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Point2D getCurrentPosition() {
        return character.getHitbox().getPosition();
    }

    public Collidable getCharacter() {
        return character;
    }
}
