package it.unibo.oop.lastcrown.view.map;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import javax.swing.JComponent;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
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
     *
     * @param id        the numerical id of the new JComponent
     * @param component the generic JComponent
     * @param x         x coordinate
     * @param y         y coordinate
     */
    HitboxController addGenericGraphics(int id, JComponent component, int x, int y, String typefolder, String name);

    /**
     * Add a generic hero graphic component. The position is already known.
     * @param id the numerical id of the hero graphic component
     *
     * @param heroGraphics the generic hero graphic component
     */
    HitboxController addHeroGraphics(int id, JComponent heroGraphics, String typefolder, String name);

    /**
     * Adds the graphical representation of an enemy to the view at the specified
     * position.
     *
     * @param id the unique identifier of the enemy
     * @param component the graphical component representing the enemy
     * @param x the X coordinate where the enemy should be placed
     * @param y the Y coordinate where the enemy should be placed
     * @param typefolder the folder name indicating the enemy type used for loading assets
     * @param name the name of the enemy used for display and identification
     * @return the HitboxController associated with the added enemy
     */
    HitboxController addEnemyGraphics(int id, JComponent component, int x, int y, String typefolder, String name);

    /**
     * Adds a wall panel (obstacle) to the game view.
     *
     * @param panel the {@link HitboxController} representing the wall to be added
     */
    void addWallPanel(HitboxController panel);

    /**
     * Notifies the MatchView about the bossfight.
     * @param start True if it started, False if it ended
     */
    void notifyBossFight(boolean start);

    /**
     * Remove safely a graphic component associated with the specified id from the
     * map.
     *
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

    void updateInGameDeck(Set<CardIdentifier> newDeck);

}