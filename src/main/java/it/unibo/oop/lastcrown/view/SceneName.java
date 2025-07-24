package it.unibo.oop.lastcrown.view;

public enum SceneName {
    /*
     * The Menu scene
     */
    MENU("MENU"),

    /*
     * The Deck scene
     */
    DECK("DECK"),

    /*
     * The Shop scene
     */
    SHOP("SHOP"),

    /*
     * The Collection scene
     */
    COLLECTION("COLLECTION"),

    /*
     * The Stats scene
     */
    STATS("STATS"),

    /*
     * The Match scene
     */
    MATCH("MATCH"),

    /*
     * The Credits scene
     */
    CREDITS("CREDITS");

    private final String value;

    SceneName(final String value) {
        this.value = value;
    }

    /**
     * @return the string associated with the specified scene.
     */
    public String get() {
        return this.value;
    }
}
