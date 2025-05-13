package it.unibo.oop.lastcrown.controller.impl;
import it.unibo.oop.lastcrown.view.impl.MainViewImpl;
import it.unibo.oop.lastcrown.controller.api.CombatController;
import it.unibo.oop.lastcrown.controller.api.GameController;
import it.unibo.oop.lastcrown.controller.api.GamePhaseController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.view.MainView;

public class MainControllerImpl implements MainController{
    private final MainView view;
    private final GameController game;
    private final CombatController combat;
    private final GamePhaseController flow;
    
    public MainControllerImpl(){
        this.view= new MainViewImpl(this);
        this.combat = new CombatControllerImpl(this);
        this.game= new GameControllerImpl(this);
        this.flow= new GamePhaseControllerImpl(this);
        startNewGame();
    }

    @Override
    public MainView getMainView() {
        return this.view;
    }

    @Override
    public void startNewGame() {
        flow.changeState(GameState.MENU);
    }

    @Override
    public GameController getGameController() {
        return this.game;
    }

    @Override
    public CombatController getCombatController() {
        return this.combat;
    }
    
}
