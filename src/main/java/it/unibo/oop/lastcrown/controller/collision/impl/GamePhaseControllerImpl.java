package it.unibo.oop.lastcrown.controller.collision.impl;
import it.unibo.oop.lastcrown.controller.collision.api.GamePhaseController;
import it.unibo.oop.lastcrown.controller.collision.api.MainController;
import it.unibo.oop.lastcrown.view.collision.api.MainView;

/**
 * Manages transitions between different game states and updates the view accordingly.
 * Delegates control to the appropriate controller based on the current GameState
 */
public final class GamePhaseControllerImpl implements GamePhaseController {
    private MainController controller;
    private MainView view;
    private GameState currenState;

    /**
     * Initializes the current game state to GameState.GAME and retrieves the main view.
     * @param controller the main controller of the application
     */
    public GamePhaseControllerImpl(final MainController controller) {
        this.controller = controller;
        this.view = controller.getMainView();
        this.currenState = GameState.GAME;
    }

    @Override
    public void changeState(final GameState newState) {
        this.currenState = newState;
        view.showPanel(newState);
        switch (newState) {
            case GAME: controller.getGameController().run(true);
            case MENU:
            default: break;
        }
    }

    @Override
    public GameState getCurrentState() {
        return this.currenState;
    }
}
