package it.unibo.oop.lastcrown.controller.app_managing.impl;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.app_managing.api.MatchStartObserver;
import it.unibo.oop.lastcrown.controller.collision.impl.Gameloop;
import it.unibo.oop.lastcrown.controller.collision.impl.MatchControllerimpl;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;


public class MatchStartObserverImpl implements MatchStartObserver {

    private final MainController mainController;
    private MatchController matchController; // Questo verrà inizializzato in onMatchStart la prima volta
    private Thread gameLoopThread;

    public MatchStartObserverImpl(final MainController mainController) {
        this.mainController = mainController;
        // Il MatchController NON viene istanziato qui.
        // Sarà istanziato in onMatchStart la prima volta che serve.
    }

    @Override
    public void onMatchStart() {
        // Step 1: Istanzia il MatchController la PRIMA VOLTA o resettalo se esiste già
        if (this.matchController == null) {
            this.matchController = new MatchControllerimpl(this.mainController);
        }

        // Step 2: Avvia il Gameloop (solo se non è già attivo)
        if (this.gameLoopThread == null || !this.gameLoopThread.isAlive()) {
            this.gameLoopThread = new Gameloop(this.mainController);
            this.gameLoopThread.start();
            System.out.println("Game Loop avviato da MatchStartObserver!");
        }
    }

    @Override
    public void onMatchEnd() {
        // Step 1: Ferma il Gameloop
        if (this.gameLoopThread != null && this.gameLoopThread.isAlive()) {
            this.gameLoopThread.interrupt();
            this.gameLoopThread = null;
            System.out.println("Game Loop fermato da MatchStartObserver!");
        }

    }


    public MatchController getMatchControllerReference() {
        return this.matchController;
    }
}