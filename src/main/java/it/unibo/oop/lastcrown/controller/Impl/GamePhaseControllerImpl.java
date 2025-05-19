package it.unibo.oop.lastcrown.controller.impl;
import it.unibo.oop.lastcrown.controller.api.GamePhaseController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.view.MainView;

public class GamePhaseControllerImpl implements GamePhaseController{
    MainController controller;
    MainView view;
    GameState currenState;

    public GamePhaseControllerImpl(final MainController controller){
        this.controller=controller;
        this.view= controller.getMainView();
        this.currenState= GameState.GAME;
    }

    @Override
    public void changeState(final GameState newState){
        this.currenState= newState;
        view.showPanel(newState);
        switch (newState) {
            case GAME: controller.getGameController().run(true);
            case MENU:
            default: break;
        }
    }

    @Override
    public GameState getCurrentState(){
        return this.currenState;
    }
}
