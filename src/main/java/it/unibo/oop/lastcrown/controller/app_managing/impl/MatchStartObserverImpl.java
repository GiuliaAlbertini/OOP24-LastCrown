package it.unibo.oop.lastcrown.controller.app_managing.impl;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.app_managing.api.MatchStartObserver;
import it.unibo.oop.lastcrown.controller.collision.impl.Gameloop;
import it.unibo.oop.lastcrown.controller.collision.impl.MatchControllerimpl;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.map.MatchView;
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
    public void onMatchStart(final int width, final int height, final CardIdentifier id, final CollectionController collectionController) {
        // Step 1: Istanzia il MatchController la PRIMA VOLTA o resettalo se esiste già
        if (this.matchController == null) {
            this.matchController = new MatchControllerimpl(this.mainController, width, height, id, collectionController);
        }

        // Step 2: Avvia il Gameloop (solo se non è già attivo)
        if (this.gameLoopThread == null || !this.gameLoopThread.isAlive()) {
            this.gameLoopThread = new Gameloop(this.mainController);
            this.gameLoopThread.start();
            System.out.println("Game Loop avviato da MatchStartObserver!");
        }
    }

    @Override
    public void stopMatchLoop() {
        // Step 1: Ferma il Gameloop
        if (this.gameLoopThread != null && this.gameLoopThread.isAlive()) {
            this.gameLoopThread.interrupt();
            this.gameLoopThread = null;
            System.out.println("Game Loop fermato da MatchStartObserver!");
        }
    }

    public void resumeMatchLoop() {
        if (this.matchController == null) {
            throw new IllegalStateException("MatchController non inizializzato.");
        }

        if (this.gameLoopThread == null || !this.gameLoopThread.isAlive()) {
            this.gameLoopThread = new Gameloop(this.mainController);
            this.gameLoopThread.start();
            System.out.println("Game Loop riavviato (resumeMatchLoop)!");
        } else {
            System.out.println("Game Loop già attivo.");
        }
    }



    public MatchController getMatchControllerReference() {
        return this.matchController;
    }

    public void getMatchView(final MatchView matchView){
        this.matchController.newMatchView(matchView);
    }
}