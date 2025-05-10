package it.unibo.oop.lastcrown;

import it.unibo.oop.lastcrown.controller.impl.Gameloop;
import it.unibo.oop.lastcrown.view.api.MainView;

public final class App {
    private App() {
        throw new UnsupportedOperationException();
    }

    public static void main(final String[] args) {
        Gameloop controller = new Gameloop(); // 1. crea controller
        MainView view = new MainView(controller);                 // 2. crea vista
        controller.setView(view);                                 // 3. collega vista al controller
        controller.run();                                         // 4. avvia il gioco
    }
}
