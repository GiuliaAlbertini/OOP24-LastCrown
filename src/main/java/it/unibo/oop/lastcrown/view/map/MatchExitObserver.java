package it.unibo.oop.lastcrown.view.map;

/**
 * An interface that contains a method that can be called by the 
 * children panels of the MatchView and notifies the MatchView to return to the main menu.
 */
public interface MatchExitObserver {
    /**
     * Notifies that the player wants to interrupt the current match session and
     * to return to the main menu.
     */
    void notifyExitToMenu();
}
