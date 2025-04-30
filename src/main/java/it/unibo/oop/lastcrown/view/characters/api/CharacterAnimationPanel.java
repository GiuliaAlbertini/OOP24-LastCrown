package it.unibo.oop.lastcrown.view.characters.api;

import java.awt.Image;

/**
 * An interface modeling an animation panel of one specific character.
 */
public interface CharacterAnimationPanel {

    /**
     * Set the next frame to be shown.
     * @param image the next frame to be shown
     */
    void setCharacterImage(Image image);

    /**
     * Realign this character health bar position after moving the animation panel.
     */
    void setHealthBarPosition();

    /**
     * @return the horizontal position of the top left corner of this animation panel
     */
    int getX();

    /**
     * @return the vertical position of the top left corner of this animation panel
     */
    int getY();

    /**
     * Set the location (coordinates x, y) of this animation panel.
     * @param x horizontal coordinate of the top left corner 
     * @param y vertical coordinate of the top left corner
     */
    void setLocation(int x, int y);

    /**
     * Set new percentage value of this character health bar.
     * @param percentage
     */
    void setHealthBarImage(int percentage);

    /**
     * Clear this animation panel image and remove the health bar.
     */
    void disposeClosing();
}
