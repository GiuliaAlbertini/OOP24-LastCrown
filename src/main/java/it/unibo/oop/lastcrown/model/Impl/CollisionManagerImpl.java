package it.unibo.oop.lastcrown.model.impl;

import java.util.HashMap;
import java.util.Map;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionManager;
import it.unibo.oop.lastcrown.model.api.CollisionObserver;

public class CollisionManagerImpl implements CollisionManager{
    Map<CollisionObserver, Boolean> map= new HashMap<>();


    @Override
    public void addObserver(CollisionObserver observer) {
        map.put(observer, true);
    }

    @Override
    public void removeObserver(CollisionObserver observer) {
        map.remove(observer);
    }

    @Override
    public boolean getState(CollisionObserver observer) {
       return map.get(observer);
    }

    @Override
    public void setState(CollisionObserver observer) {
        //se è true
        if (getState(observer)){
            map.replace(observer, false);
        }else{
            map.replace(observer, true);
        }
    }

    @Override
    public void notify(CollisionEvent event) {
        for (Map.Entry<CollisionObserver, Boolean> elem : map.entrySet()) {
            if (elem.getValue()){
                elem.getKey().notify(event);
            }
        }
    }
    
}
