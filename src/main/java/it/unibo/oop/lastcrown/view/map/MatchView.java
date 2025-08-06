package it.unibo.oop.lastcrown.view.map;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.menu.api.Scene;

/**
 * Represents the view for an active match, managing all in‑game graphical components
 * and updates during gameplay.
 */
public interface MatchView extends Scene {

    /**
     * Displays the defeat dialog and performs cleanup after a defeat.
     */
    void disposeDefeat();

    /**
     * Displays the victory dialog and performs cleanup after a victory.
     */
    void disposeVictory();

    /**
     * Adds a generic graphical component to the map, centered at the specified coordinates.
     *
     * @param id         the unique numeric identifier for the component
     * @param component  the graphical component to add
     * @param x          the x-coordinate for placement
     * @param y          the y-coordinate for placement
     * @param typefolder the string representing the asset folder type
     * @param name       the name of the entity (for identification or display)
     * @return the HitboxController associated with the added component
     */
    HitboxController addGenericGraphics(int id, JComponent component, int x, int y, String typefolder, String name);

    /**
     * Adds a hero graphical component to the view using a predefined position.
     *
     * @param id           the hero’s unique numeric identifier
     * @param heroGraphics the graphical component representing the hero
     * @param typefolder   the asset folder string indicating hero type
     * @param name         the hero’s name for identification
     * @return the HitboxController associated with the added hero
     */
    HitboxController addHeroGraphics(int id, JComponent heroGraphics, String typefolder, String name);

    /**
     * Adds a spell graphical component to the view at the specified position.
     *
     * @param id        the unique numeric identifier for the spell
     * @param component the graphical component of the spell
     * @param x         the x-coordinate for placement
     * @param y         the y-coordinate for placement
     */
    void addSpellGraphics(int id, JComponent component, int x, int y);

    /**
     * Adds an enemy graphical component to the map at the given coordinates.
     *
     * @param id         the unique identifier of the enemy
     * @param component  the graphical component representing the enemy
     * @param x          the x-coordinate where the enemy should appear
     * @param y          the y-coordinate where the enemy should appear
     * @param typefolder string indicating enemy type folder for loading assets
     * @param name       the name of the enemy for display
     * @return the HitboxController associated with the added enemy
     */
    HitboxController addEnemyGraphics(int id, JComponent component, int x, int y, String typefolder, String name);

    /**
     * Adds a wall (obstacle) to the game view.
     *
     * @param panel the hitbox controller representing the wall
     */
    void addWallPanel(HitboxController panel);

    /**
     * Notifies the view about the start or end of a boss fight.
     *
     * @param start true if the boss fight has begun, false if it has ended
     */
    void notifyBossFight(boolean start);

    /**
     * Removes the graphical component associated with the specified identifier.
     *
     * @param id the identifier of the component to remove
     */
    void removeGraphicComponent(int id);

    /**
     * Clears all dynamically added graphical components from the view.
     */
    void clearNewGraphicsComponent();

    /**
     * Returns the vertical limit of the troop deployment zone.
     *
     * @return the vertical boundary coordinate for the troop deployment zone
     */
    int getTrupsZoneLimit();

    /**
     * Returns the size (width and height) of the wall obstacle in the view.
     *
     * @return the wall’s dimensions
     */
    Dimension getWallSize();

    /**
     * Returns the upper-left corner coordinates of the wall obstacle.
     *
     * @return the wall’s top-left coordinate
     */
    Point getWallCoordinates();

    /**
     * Updates the set of in‑game card buttons displayed in the match.
     *
     * @param newDeck the new set of card identifiers to display
     */
    void updateInGameDeck(Set<CardIdentifier> newDeck);

    /**
     * Updates the text shown in the event display area of the UI.
     *
     * @param text the new event message to display
     */
    void setEventText(String text);

    /**
     * Updates the displayed amount of coins in the UI.
     *
     * @param coins the current number of coins to show
     */
    void setCoins(int coins);
}
