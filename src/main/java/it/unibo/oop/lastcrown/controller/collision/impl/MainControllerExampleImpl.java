package it.unibo.oop.lastcrown.controller.collision.impl;
import it.unibo.oop.lastcrown.controller.collision.api.GameController;
import it.unibo.oop.lastcrown.controller.collision.api.GamePhaseController;
import it.unibo.oop.lastcrown.controller.collision.api.MainControllerExample;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.view.collision.api.MainViewExample;
import it.unibo.oop.lastcrown.view.collision.impl.MainViewExampleImpl;

/**
 * Concrete implementation of the MainController interface.
 * Manages the main components of the game including the view, game logic,
 * match controller, and game phase flow.
 */
public final class MainControllerExampleImpl implements MainControllerExample {
    private final MainViewExample view;
    private final GameController game;
    private final MatchControllerimpl match;
    private final GamePhaseController flow;
    //private final InGameCards cards;

    /**
     * Initializes the main view,match controller, game controller, and game phase controller.
     * Automatically starts a new game upon creation.
     */
    public MainControllerExampleImpl() {
        this.view = new MainViewExampleImpl(this);
        this.match = new MatchControllerimpl(this);
        this.game = new GameControllerImpl(this);
        this.flow = new GamePhaseControllerImpl(this);
        //this.cards= new InGameCards(this);
        startNewGame();
    }

    @Override
    public MainViewExample getMainView() {
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
