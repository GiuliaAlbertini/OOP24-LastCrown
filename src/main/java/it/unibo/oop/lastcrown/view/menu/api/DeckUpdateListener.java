package it.unibo.oop.lastcrown.view.menu.api;

/**
 * Callback invoked when deck is updated.
 */
@FunctionalInterface
public interface DeckUpdateListener {
    /**
     * Invoked when the deck is updated.
     */
    void onDeckUpdated();
}
