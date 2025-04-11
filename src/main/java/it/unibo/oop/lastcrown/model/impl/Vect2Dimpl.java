package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Vect2D;

public class Vect2Dimpl implements Vect2D {
    private final double x;
    private final double y;

    public Vect2Dimpl(double x, double y){
        this.x=x;
        this.y=y;
    }
    
    @Override
    public Vect2D sum(Vect2D v) {
        return new Vect2Dimpl(this.x + v.x(), this.y +v.y());
    }

    @Override
    public Vect2D subtract(Vect2D v) {
       return new Vect2Dimpl(this.x - v.x(), this.y - v.y());
    }

    @Override
    public Vect2D mul(final double factor) {
        return new Vect2Dimpl(this.x * factor, this.y * factor);
    }

    @Override
    public Vect2D normalized() {
        double module=(double)Math.sqrt(x*x+y*y);
        return new Vect2Dimpl(x/module,y/module);
    }

    @Override
    public double module() {
        return (double)Math.sqrt(x*x+y*y);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }
    
    @Override
    public String toString(){
        return "Vect2d("+x+", "+y+")";
    }

}
