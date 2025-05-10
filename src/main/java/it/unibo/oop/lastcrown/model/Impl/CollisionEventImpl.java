package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;

public class CollisionEventImpl implements CollisionEvent{
    private final Collidable collidable1;
    private final Collidable collidable2;
    
    public CollisionEventImpl(Collidable collidable1, Collidable collidable2) {
        this.collidable1 = collidable1;
        this.collidable2 = collidable2;
        
    }
    
    public Collidable getCollidable1() {
        return collidable1;
    }
    
    public Collidable getCollidable2() {
        return collidable2;
    }
    
}

