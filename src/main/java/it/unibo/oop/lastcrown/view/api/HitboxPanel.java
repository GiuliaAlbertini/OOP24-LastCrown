package it.unibo.oop.lastcrown.view.api;
import javax.swing.JPanel;

public interface HitboxPanel {
    
    /**
     * Restituisce il pannello grafico associato all'hitbox.
     * 
     * @return il JPanel che rappresenta l'hitbox
     */
    JPanel getHitboxPanel();

    /**
     * Aggiorna la posizione e le dimensioni del pannello sulla base dell'hitbox.
     */
    void updatePanel();

    /**
     * Imposta la nuova posizione del pannello.
     * 
     * @param x coordinata x
     * @param y coordinata y
     */
    void setPanelPosition(int x, int y);
}
