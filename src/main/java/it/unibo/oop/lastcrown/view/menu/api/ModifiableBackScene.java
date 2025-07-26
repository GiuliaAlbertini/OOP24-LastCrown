package it.unibo.oop.lastcrown.view.menu.api;

import it.unibo.oop.lastcrown.view.SceneName;

/**
 * An extension of Scene interface.
 */
public interface ModifiableBackScene extends Scene {

    /**
     * Sets the back destination of the static back button.
     * @param destination the destination of the back action
     */
    void setBackDestination(SceneName destination);
}
