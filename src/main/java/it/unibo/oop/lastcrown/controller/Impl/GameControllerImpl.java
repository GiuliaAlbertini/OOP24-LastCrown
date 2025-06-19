package it.unibo.oop.lastcrown.controller.impl;
import it.unibo.oop.lastcrown.controller.api.GameController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.view.MainView;

/**
 * Implementation of the GameController interface.
 * Manages the main game loop and handles starting and stopping the game.
 */
public final class GameControllerImpl implements GameController {
    private MainController controller;
    private MainView view;
    private Thread gameLoop;

    /**
     * Creates a new GameControllerImpl linked to the given main controller.
     * @param controller the main controller that manages overall game flow and view
     */
    public GameControllerImpl(final MainController controller) {
        this.controller = controller;
        this.view = this.controller.getMainView();
    }

    @Override
    public void run(final boolean exploration) {
        if (this.gameLoop == null || !this.gameLoop.isAlive()) {
            if (!exploration) {
                this.endGame();
            } else {
                this.startGameLoop();
            }
        }
    }

    /**
     * Starts the game loop thread and shows the game panel in the view.
     */
    public void startGameLoop() {
        this.view.showPanel(GameState.GAME);
        this.gameLoop = new Gameloop(controller);
        this.gameLoop.start();
    }

    /**
     * Ends the current game.
     *
     * @throws UnsupportedOperationException indicates the method is not yet implemented
     */
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initPanel'");
    }
}
