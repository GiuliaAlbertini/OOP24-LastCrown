package it.unibo.oop.lastcrown;

import it.unibo.oop.lastcrown.controller.impl.GameControllerImpl;
import it.unibo.oop.lastcrown.controller.api.GameController;
import it.unibo.oop.lastcrown.view.MainView;

public final class App {
    private App() {
        throw new UnsupportedOperationException();
    }

    public static void main(final String[] args) {
        //creo la vista
        MainView view = new MainView();

        //Creo il controller, passando la vista come parametro
        GameController engine = new GameControllerImpl(view);
        //avvia il game loop
		engine.run();
    }
}