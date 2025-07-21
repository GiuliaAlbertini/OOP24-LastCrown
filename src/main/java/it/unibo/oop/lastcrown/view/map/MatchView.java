package it.unibo.oop.lastcrown.view.map;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.view.menu.api.Scene;

/**
 * An interface that models the match view.
 */
public interface MatchView extends Scene {

    /**
     * Notifies the match view that it must show the Defeat Dialog.
     */
    void disposeDefeat();

    /**
     * Notifies the match view that it must show the Victory Dialog.
     */
    void disposeVictory();

    /**
     * Add a generic JComponent to the map, centered in the specified coordinates.
     * @param id the numerical id of the new JComponent
     * @param component the generic JComponent
     * @param x x coordinate
     * @param y y coordinate
     */
    void addGenericGraphics(int id, JComponent component, int x, int y);

    /**
     * Add a generic hero graphic component. The position is already known.
     * @param heroGraphics the generic hero graphic component
     */
    void addHeroGraphics(JComponent heroGraphics);

    /**
     * Remove safely a graphic component associated with the specified id from the map.
     * @param id the id linked to the component to eliminate
     */
    void removeGraphicComponent(int id);

    /**
     * Removes all the new graphic components from the map.
     */
    void clearNewGraphicsComponent();

    /**
     * @return the vertical limit of the trups zone
     */
    int getTrupsZoneLimit();

    /**
     * @return the wall size
     */
    Dimension getWallSize();

    /**
     * @return the upper left corner coordinates of the wall
     */
    Point getWallCoordinates();
}
