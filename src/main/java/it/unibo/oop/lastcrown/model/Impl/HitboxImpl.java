package it.unibo.oop.lastcrown.model.Impl;

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
    //implementazione Axis-Aligned Bounding Box 
    public boolean checkCollision(Hitbox a, Hitbox b){
        double ax1= a.getPosition().x();
        double ay1= a.getPosition().y();
        double ax2= ax1+width;
        double ay2= ay1+height;

        double bx1= b.getPosition().x();
        double by1= b.getPosition().y();
        double bx2= bx1+width;
        double by2= by1+height;

        //se tutte le condizioni di non sovrapposizioni sono vere allora non c'è collisione
        return !(ax2 <= bx1 || bx2 <= ax1 || ay2 <= by1 || by2 <= ay1);
        
    }
}
