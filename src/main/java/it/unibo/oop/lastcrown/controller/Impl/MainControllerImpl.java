package it.unibo.oop.lastcrown.controller.impl;
import it.unibo.oop.lastcrown.view.impl.MainViewImpl;
import it.unibo.oop.lastcrown.controller.api.GameController;
import it.unibo.oop.lastcrown.controller.api.GamePhaseController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.view.MainView;

/**
 * Concrete implementation of the MainController interface.
 * Manages the main components of the game including the view, game logic,
 * match controller, and game phase flow.
 */
public final class MainControllerImpl implements MainController {
    private final MainView view;
    private final GameController game;
    private final MatchControllerimpl match;
    private final GamePhaseController flow;

    /**
     * Initializes the main view,match controller, game controller, and game phase controller.
     * Automatically starts a new game upon creation.
     */
    public MainControllerImpl() {
        this.view = new MainViewImpl(this);
        this.match = new MatchControllerimpl(this);
        this.game = new GameControllerImpl(this);
        this.flow = new GamePhaseControllerImpl(this);
        startNewGame();
    }

    @Override
    public MainView getMainView() {
        return this.view;
    }

    @Override
    public void startNewGame() {
        flow.changeState(GameState.GAME);
    }

    @Override
    public GameController getGameController() {
        return this.game;
    }

    @Override
    public MatchController getMatchController() {
        return this.match;
    }
}
