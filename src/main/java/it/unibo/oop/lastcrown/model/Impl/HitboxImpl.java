package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Point2D;

//dò per scontato che i rettangoli abbiano tutti la stessa altezza
public class HitboxImpl implements Hitbox{
    private Point2D position; //coordinate x,y angolo in alto a sinistra
    private int width;
    private int height;

    public HitboxImpl(int width, int height, Point2D position){
        this.height=height;
        this.width=width;
        this.position=position;
    }

    @Override
    public Point2D getPosition(){
       return this.position;
    }

    @Override
    public void setPosition(Point2D newPos) {
        this.position = newPos;
    }

    @Override
    public int getWidth(){
        return this.width;
    }

    @Override
    public int getHeight(){
        return this.height;
    }

    @Override
    public Point2D getCenter() {
        double centerX = this.position.x() + this.width / 2.0;
        double centerY = this.position.y() + this.height / 2.0;
        return new Point2DImpl(centerX, centerY);
    }
    
    @Override
    public void setWidth(int width) {
        this.width = width;
    }
    
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    //implementazione Axis-Aligned Bounding Box 
    public boolean checkCollision(Hitbox other){
        double ax1= this.getPosition().x();
        double ay1= this.getPosition().y();
        double ax2= ax1+this.width;
        double ay2= ay1+this.height;

        double bx1= other.getPosition().x();
        double by1= other.getPosition().y();
        double bx2= bx1+other.getWidth();
        double by2= by1+other.getHeight();

        //se tutte le condizioni di non sovrapposizioni sono vere allora non c'è collisione
        return !(ax2 < bx1 || bx2 < ax1 || ay2 < by1 || by2 < ay1);

        
    }
}
