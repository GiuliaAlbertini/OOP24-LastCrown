package it.unibo.oop.lastcrown.controller.Impl;


import java.util.logging.*;


import it.unibo.oop.lastcrown.controller.api.GameController;
import it.unibo.oop.lastcrown.model.impl.Ball;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.model.impl.Vect2Dimpl;
import it.unibo.oop.lastcrown.view.MainView;


public class GameControllerImpl implements GameController, Runnable{
    private boolean running = false;
    private long period= 20; //periodi tra frame in millisecondi (framerate di 50 fotogrammi al secondo (50 FPS))
    private Logger logger = Logger.getLogger("GameEngine");
    private MainView view;
	private Ball ball;
    Thread gameThread;
    


    public GameControllerImpl(MainView view){
        this.view= view;
		ball= new Ball(new Point2DImpl(200, 200), new Vect2Dimpl(300, 300), 20);
		view.setBall(ball);
    }

    @Override
    public void run() {
        long lastTime=System.currentTimeMillis(); //tempo di partenza
        while(true){
            long current= System.currentTimeMillis(); //tempo attuale
            int elapse= (int)(current-lastTime);//tempo trascorso tra due frame, espresso in millisecondi
			updateGame(elapse);
			render();
			waitForNextFrame(current);
			lastTime = current;
        }
    }
 
    /**
	 * current-> tempo inizio frame
	 * dt-> È il tempo già trascorso da quando il frame è iniziato
	 * period-> tempo massimo
	 */
    protected void waitForNextFrame(long current){
		long dt = System.currentTimeMillis() - current; // Calcola il tempo trascorso dall'inizio del frame corrente
		if (dt < period){
			try {
				Thread.sleep(period-dt);
			} catch (Exception ex){}
		}
	}
	
	protected void updateGame(int elapse){
		ball.update(elapse/1000.0, 800, 600);
	}
	
	protected void render(){
		view.render();
	}
}
