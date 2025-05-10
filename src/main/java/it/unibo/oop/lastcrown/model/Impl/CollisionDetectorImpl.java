package it.unibo.oop.lastcrown.model.impl;

import java.util.ArrayList;
import java.util.List;
import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionManager;

public class CollisionDetectorImpl {
    List<Collidable> object = new ArrayList<>();
    CollisionManager collisionManager;

    public CollisionDetectorImpl(CollisionManager collisionManager) {
        this.collisionManager= collisionManager;
    }

    public void loaderEntityCollision(){
        for (int i=0; i< object.size(); i++){
            for (int j=i+1; j< object.size()-1; j++){
                Collidable a= object.get(i);
                Collidable b= object.get(j);
                if (a.getData().getHitbox().checkCollision(b.getData().getHitbox())){
                    CollisionEvent collisionevent = new CollisionEventImpl(a, b);
                    collisionManager.notify(collisionevent);
                }
            }
        }
    }
}
