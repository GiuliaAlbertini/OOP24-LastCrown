package it.unibo.oop.lastcrown.audio;

/**
 * Enumeration that contains different sound effects.
 */
public enum SoundEffect {
    /**
     * Played when the player wins the game.
     */
    WIN("win"),

    /**
     * Played when the player loses the game.
     */
    LOSE("lose");

    private final String value;

    SoundEffect(final String value) {
        this.value = value;
    }

    /**
     * @return the String value associated with this sound effect.
     */
    public String get() {
        return this.value;
    }
}
