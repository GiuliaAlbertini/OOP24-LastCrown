package it.unibo.oop.lastcrown.controller.api;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

public interface MatchController {

    /**
     * Adds a character to the match with the given controller and hitbox.
     *
     * @param ID to assign the character to
     * @param controller the character's controller logic
     * @param hitbox the character's hitbox for collision detection
     */
    void addCharacter(final int n, GenericCharacterController controller, HitboxController hitbox);

    /**
     * Called when the "Add Character" button is pressed in the UI.
     * This method should handle the logic to initiate character addition.
     */
    void onAddCharacterButtonPressed();

    /**
     * Updates the match model.
     * @param deltaTime how much time passed since last update.
     */
    void update(int deltaTime);

}
