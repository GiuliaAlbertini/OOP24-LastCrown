package it.unibo.oop.lastcrown.controller.impl;
import it.unibo.oop.lastcrown.controller.api.GameController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.view.MainView;

public class GameControllerImpl implements GameController{
    MainController controller;
    MainView view;
    Thread gameLoop;
    public GameControllerImpl(final MainController controller){
        this.controller= controller;
        this.view= this.controller.getMainView();
    }
    
    @Override
    public void run(final boolean exploration) {
        if(this.gameLoop == null || !this.gameLoop.isAlive()){
            if(!exploration){
                this.endGame();
            }else{
                 System.out.println("Il gioco è iniziato!");
                this.startGameLoop();
            }
        }
    }
    
    public void startGameLoop(){
        this.view.showPanel(GameState.GAME);
        this.gameLoop= new Gameloop(controller);
        this.gameLoop.start();
    }
    
    
    public void endGame(){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initPanel'");
    }
}
