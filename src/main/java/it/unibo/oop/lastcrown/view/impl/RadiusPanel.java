package it.unibo.oop.lastcrown.view.impl;


import javax.swing.JPanel;

/**
 * Interfaccia che rappresenta un pannello grafico per la visualizzazione
 * di un raggio attorno a un'entità o punto specifico nella UI.
 */
public interface RadiusPanel {

    /**
     * Restituisce il pannello Swing che visualizza il raggio.
     *
     * @return il JPanel che rappresenta graficamente il raggio
     */
    JPanel getRadiusPanel();

    /**
     * Aggiorna la posizione del pannello sulla base della posizione corrente
     * del centro e del raggio. Questo metodo dovrebbe essere chiamato ogni
     * volta che l'entità monitorata cambia posizione.
     */
    void updatePosition();
}
