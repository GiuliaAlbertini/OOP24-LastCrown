package it.unibo.oop.lastcrown.model.impl;

import java.util.HashMap;
import java.util.Map;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionManager;
import it.unibo.oop.lastcrown.model.api.CollisionObserver;

/**
 * Manages a set of CollisionObservers, each with an active/inactive state.
 * Only active observers are notified of CollisionEvent.
 */
public final class CollisionManagerImpl implements CollisionManager {
    private Map<CollisionObserver, Boolean> map = new HashMap<>();

    @Override
    public void addObserver(final CollisionObserver observer) {
        map.put(observer, true);
    }

    @Override
    public void removeObserver(final CollisionObserver observer) {
        map.remove(observer);
    }

    @Override
    public boolean getState(final CollisionObserver observer) {
       return map.get(observer);
    }

    @Override
    public void setState(final CollisionObserver observer) {
        if (getState(observer)) {
            map.replace(observer, false);
        } else {
            map.replace(observer, true);
        }
    }

    @Override
    public void notify(final CollisionEvent event) {
        for (final Map.Entry<CollisionObserver, Boolean> elem : map.entrySet()) {
            if (elem.getValue()) {
                elem.getKey().notify(event);
            }
        }
    }
}
