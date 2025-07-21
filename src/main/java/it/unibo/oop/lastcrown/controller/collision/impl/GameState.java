package it.unibo.oop.lastcrown.controller.collision.impl;
/**
 * Represents the various states the game can be in.
 */
public enum GameState {
    /**
     * The main menu state.
     */
    MENU("Menu"),

    /**
     * The menu shown while the game is paused or in progress.
     */
    MENU_IN_GAME("Menu in Game"),

    /**
     * The main gameplay state.
     */
    GAME("Game"),

    /**
     * The combat phase of the game.
     */
    COMBAT("Combat"),

    /**
     * The shop phase between rounds.
     */
    SHOP_BETWEEN_ROUNDS("Shop Between Rounds"),

    /**
     * The shop phase outside of combat or rounds (e.g., before the game starts).
     */
    SHOP_OUTSIDE("Shop Outside"),

    /**
     * The state indicating the start of a new round.
     */
    ROUND_START("Round Start"),

    /**
     * The state indicating the end of a round.
     */
    ROUND_END("Round End"),

    /**
     * The victory state when the player wins the game.
     */
    VICTORY("Victory"),

    /**
     * The game over state when the player loses the game.
     */
    GAME_OVER("Game Over"),

    /**
     * A default or undefined state.
     */
    NONE("None");

    /**
     * A human-readable description of the game state.
     */
    private final String state;

    /**
     * Constructs a GameState enum with the given description.
     *
     * @param state A string representing the human-readable name of the state.
     */
    GameState(final String state) {
        this.state = state;
    }

    /**
     * Returns the human-readable name of the game state.
     *
     * @return the string representation of the state.
     */
    public String getState() {
        return this.state;
    }
}

