package it.unibo.oop.lastcrown.model.card;

/**
 * Enumeration useful to mantain all the card types.
 */
public enum CardType {

    /**
     * All the characters that the player controls (hero + playable characters).
     */
    FRIENDLY("friendly characters"),

    /**
     * the Hero, if he dies the match is lost.
     */
    HERO("hero"),

    /**
     * close combat friendly characters.
     */
    MELEE("melee"),

    /**
     * friendly characters that attack from a great distance.
     */
    RANGED("ranged"),

    /**
     * regular enemies, their purpose is to destroy the player defences.
     */
    ENEMY("enemy"),

    /**
     * The greatest foe, if it dies the match is won.
     */
    BOSS("boss"),

    /**
     * the spell that the player can cast during the match in order
     * to help their characters.
     */
    SPELL("spell"),

    /**
     * the Wall that defends the Hero and the ranged trups.
     */
    WALL("wall");

    private final String value;

    CardType(final String value) {
        this.value = value;
    }

    /**
     * @return the string associated with the specified card type.
     */
    public String get() {
        return this.value;
    }
}
