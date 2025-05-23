package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.api.Vect2D;



public class Point2DImpl implements Point2D{
    private final double x;
    private final double y;

    public Point2DImpl(double x, double y){
        this.x= x;
        this.y= y;
    }

    @Override
    public Point2D sum(Vect2D v) {
        return new Point2DImpl(x+v.x(), y+v.y());
    }

    @Override
    public double getDistance(Point2D p) {
        return Math.sqrt((this.x - p.x()) * (this.x-p.x()) + (this.y-p.y()) * (this.y)-p.y());
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    public String toString(){
        return "P2d("+x+", "+y+")";
    }

}
