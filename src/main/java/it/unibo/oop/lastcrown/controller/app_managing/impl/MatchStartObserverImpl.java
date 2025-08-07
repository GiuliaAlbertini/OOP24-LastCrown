package it.unibo.oop.lastcrown.controller.app_managing.impl;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.app_managing.api.MatchStartObserver;
import it.unibo.oop.lastcrown.controller.collision.impl.Gameloop;
import it.unibo.oop.lastcrown.controller.collision.impl.MatchControllerimpl;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.map.MatchView;
import it.unibo.oop.lastcrown.view.menu.api.MainView;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;

/** Implementation for the {@link MatchStartObserver}. */
public final class MatchStartObserverImpl implements MatchStartObserver {

    private final MainController mainController;
    private MatchController matchController;
    private Thread gameLoopThread;

    /**
     * Instantiates the match start observer.
     * @param mainController the main controller.
     */
    public MatchStartObserverImpl(final MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onMatchStart(final int width, final int height, final CardIdentifier id,
                            final CollectionController collectionController, final MainView mainView, final int enemyList) {
        this.matchController = new MatchControllerimpl(
            this.mainController,
            width,
            height,
            id,
            collectionController,
            mainView,
            enemyList
        );

        if (this.gameLoopThread == null || !this.gameLoopThread.isAlive()) {
            this.gameLoopThread = new Gameloop(this.mainController);
            this.gameLoopThread.start();
        }
    }

    @Override
    public void stopMatchLoop() {
        if (this.gameLoopThread != null && this.gameLoopThread.isAlive()) {
            this.gameLoopThread.interrupt();
            this.gameLoopThread = null;
        }
    }

    @Override
    public void resumeMatchLoop() {
        if (this.matchController == null) {
            throw new IllegalStateException("MatchController non inizializzato.");
        }

        if (this.gameLoopThread == null || !this.gameLoopThread.isAlive()) {
            this.gameLoopThread = new Gameloop(this.mainController);
            this.gameLoopThread.start();
        }
    }

    @Override
    public MatchController getMatchControllerReference() {
        return this.matchController;
    }

    @Override
    public void setMatchView(final MatchView matchView) {
        this.matchController.newMatchView(matchView);
    }
}
