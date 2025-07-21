package it.unibo.oop.lastcrown.view.characters;

/**
 * Enumeration useful to mantain all the animation keywords.
 */
public enum Keyword {

    /**
     * running animation to the right.
     */
    RUN_RIGHT("run"),

    /**
     * running animation to the left.
     */
    RUN_LEFT("run_left"),

    /**
     * retreat animation (designed for the enemies).
     */
    RETREAT("retreat"),

    /**
     * idle animation to the right (the character will stand in a position).
     */
    STOP("stop"),

    /**
     * idle animation to the left (designed for the hero).
     */
    STOP_LEFT("stop_left"),

    /**
     * death animation.
     */
    DEATH("death"),

    /**
     * attack animation.
     */
    ATTACK("attack"),

    /**
     * jump up animation (designed for playable characters).
     */
    JUMPUP("jumpUp"),

    /**
     * jump down animation (designed for playable characters).
     */
    JUMPDOWN("jumpDown"),

    /**
     * jump forward animation (designed for playable characters).
     */
    JUMPFORWARD("jumpForward");

    private final String value;

    Keyword(final String value) {
        this.value = value;
    }

    /**
     * @return the String value associated with the specified keyword.
     */
    public String get() {
        return this.value;
    }
}
