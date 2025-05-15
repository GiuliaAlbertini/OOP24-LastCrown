package it.unibo.oop.lastcrown.view.characters.api;

import java.awt.Image;

import it.unibo.oop.lastcrown.view.AnimationPanel;

/**
 * An interface modeling an animation panel of one specific character.
 */
public interface CharacterAnimationPanel extends AnimationPanel, ReadOnlyAnimationPanel {

    /**
     * Set the next frame to be shown.
     * @param image the next frame to be shown
     */
    void setCharacterImage(Image image);

    /**
     * Set the initial alignment of the characterHealthBar.
     */
    void setHealthBarAlignment();

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
