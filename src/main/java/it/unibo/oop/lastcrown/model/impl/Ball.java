package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.api.Vect2D;

public class Ball {
    private Point2D position;
    private Vect2D velocity;
    private final int radius;
    
    public Ball(Point2D position, Vect2D velocity, int radius){
        this.position=position;
        this.velocity=velocity;
        this.radius=radius;
    }

    public Point2D getPosition(){
        return this.position;
    }

    public int getRadius(){
        return this.radius;
    }

    public void update(double period, int screenWidth, int screenHeight){
         double newx= position.x() + velocity.x() * period;
         double newy= position.y()+velocity.y()*period;
         
         if (newx - radius < 0) {
            newx = radius; // Riposiziono esattamente sul bordo
            velocity = new Vect2Dimpl(-velocity.x(), velocity.y()); // Inversione direzione
        } else if (newx + radius > screenWidth) {
            newx = screenWidth - radius;
            velocity = new Vect2Dimpl(-velocity.x(), velocity.y());
        }
    
        // Controllo bordi verticali
        if (newy - radius < 0) {
            newy = radius;
            velocity = new Vect2Dimpl(velocity.x(), -velocity.y());
        } else if (newy + radius > screenHeight) {
            newy = screenHeight - radius;
            velocity = new Vect2Dimpl(velocity.x(), -velocity.y());
        }

        position= new Point2DImpl(newx, newy);
    }

}
