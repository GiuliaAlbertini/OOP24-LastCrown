package it.unibo.oop.lastcrown.model.api;

public interface Hitbox {
    Point2D getPosition();
    boolean checkCollision(Hitbox a, Hitbox b);
}
