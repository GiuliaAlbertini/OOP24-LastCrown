package it.unibo.oop.lastcrown.view;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.impl.Ball;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.model.impl.Vect2Dimpl;

public class MainView {
    private JFrame frame;
    private GamePanel panel;
    public Ball ball;

    public MainView(){
        frame=new JFrame("Simple Game window"); //crea la finestra di gioco
        frame.setSize(800,600); //dimensioni
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //chiusura della finestra
        frame.setResizable(false); //non ridimensionabile
        panel= new GamePanel(); //crea il pannello di gioco
        frame.add(panel); //aggiunge il pannello alla finestra
        frame.setVisible(true); //rende visibile la finestra
        ball = new Ball(new Point2DImpl(200, 200), new Vect2Dimpl(2, 3), 20);

    }

    public void render() {
        panel.repaint(); // Richiede il ridisegno del pannello (cioè, ridisegna la palla)
    }
    
    public Ball getBall() {
        return ball; // Restituisce la palla per poterla aggiornare
    }

    public void setBall(Ball ball) {
        this.ball=ball; // Restituisce la palla per poterla aggiornare
    }
    private class GamePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Pulisce il pannello prima di ridisegnare

            // Impostiamo il colore del disegno
            g.setColor(Color.RED);

            g.fillOval((int) ball.getPosition().x() - ball.getRadius(),
            (int) ball.getPosition().y() - ball.getRadius(),
            ball.getRadius() * 2, ball.getRadius() * 2);
        }
    }
    
}
