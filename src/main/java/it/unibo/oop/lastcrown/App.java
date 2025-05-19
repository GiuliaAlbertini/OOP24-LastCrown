package it.unibo.oop.lastcrown;
import it.unibo.oop.lastcrown.controller.impl.MainControllerImpl;

public final class App {
    private App() { }

    public static void main(final String[] args) {
       new MainControllerImpl();              
    }
}