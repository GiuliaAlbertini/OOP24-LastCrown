package it.unibo.oop.lastcrown.audio;

/**
 * Enumeration that contains different soundtracks.
 */
public enum SoundTrack {
    /**
     * Played when the player starts a new battle.
     */
    BATTLE("battle"),

    /**
     * Played when the bossfight starts.
     */
    BOSSFIGHT("bossfight"),

    /**
     * Played when the player is located in the menu.
     */
    MENU("menu"),

    /**
     * Played when the player is checking their collection.
     */
    COLLECTION("collection"),

    /**
     * Played when the player is located in the shop.
     */
    SHOP("shop");

    private final String value;
    SoundTrack(final String value) {
        this.value = value;
    }

    /**
     * @return the String value associated with this soundtrack.
     */
    public String get() {
        return this.value;
    }
}
