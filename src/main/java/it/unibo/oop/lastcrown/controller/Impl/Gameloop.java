package it.unibo.oop.lastcrown.controller.impl;
import it.unibo.oop.lastcrown.controller.api.MainController;

public class Gameloop extends Thread{
    MainController controller;
    private boolean running;
    private long period = 64;

    public Gameloop(MainController controller) {
        this.controller=controller;
        this.running=false;
    }
    
    public void run() {
        this.running=true;
        long lastTime = System.currentTimeMillis();
        while (this.running) {
            long current = System.currentTimeMillis();
            this.controller.getMatchController().update((int) (current - lastTime));
            waitForNextFrame(current);
            lastTime = current;
        }
    }

    private void waitForNextFrame(long current) {
        long dt = System.currentTimeMillis() - current;
        if (dt < period) {
            try {
                sleep(period - dt);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void interrupt() {
        this.running = false;
        super.interrupt();
    }

}
